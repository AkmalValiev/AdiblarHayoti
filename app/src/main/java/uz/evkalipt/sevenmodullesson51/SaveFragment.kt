package uz.evkalipt.sevenmodullesson51

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.evkalipt.sevenmodullesson51.adapters.SavedRvAdapter
import uz.evkalipt.sevenmodullesson51.databinding.FragmentSaveBinding
import uz.evkalipt.sevenmodullesson51.models.Writer

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SaveFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentSaveBinding
    lateinit var savedRvAdapter: SavedRvAdapter
    lateinit var firestore: FirebaseFirestore
    lateinit var writerList:ArrayList<Writer>
    lateinit var adapterList:ArrayList<Writer>
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var gson: Gson
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveBinding.inflate(layoutInflater)
        writerList = ArrayList()
        adapterList = ArrayList()
        sharedPreferences = requireActivity().getSharedPreferences("writers", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        gson = Gson()
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result
                    result?.forEach {
                        val toObject = it.toObject(Writer::class.java)
                        writerList.add(toObject)
                    }
                }
                val uids = sharedPreferences.getString("uids", "")
                if (uids != "") {
                    var type = object : TypeToken<ArrayList<String>>() {}.type
                    val fromJson = gson.fromJson<ArrayList<String>>(uids, type)
                    for (s in fromJson) {
                        for (writer12 in writerList) {
                            if (writer12.uid.toString() == s) {
                                adapterList.add(writer12)
                            }
                        }
                    }
                }

                savedRvAdapter = SavedRvAdapter(
                    adapterList,
                    binding.root.context,
                    object : SavedRvAdapter.OnClickMy {
                        override fun myClick(writer: Writer, position: Int) {
                            var type = object : TypeToken<ArrayList<String>>() {}.type
                            val fromJson = gson.fromJson<ArrayList<String>>(uids, type)
                            fromJson.remove(writer.uid.toString())
                            val toJson = gson.toJson(fromJson)
                            editor.putString("uids", toJson)
                            editor.commit()
                            adapterList.remove(writer)
                            savedRvAdapter.notifyItemRemoved(position)
                            savedRvAdapter.notifyItemRangeChanged(position, adapterList.size)
                        }

                        override fun onClickWriter(writer: Writer) {
                            var bundle = Bundle()
                            bundle.putString("uid", writer.uid.toString())
                            findNavController().navigate(R.id.writerFragment, bundle)
                        }

                    })
                binding.rvSaved.adapter = savedRvAdapter
            }
        binding.searchSave.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("save", "save")
            findNavController().navigate(R.id.searchFragment, bundle)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}