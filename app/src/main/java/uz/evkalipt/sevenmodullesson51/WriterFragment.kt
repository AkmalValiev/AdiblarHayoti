package uz.evkalipt.sevenmodullesson51

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import uz.evkalipt.sevenmodullesson51.databinding.FragmentWriterBinding
import uz.evkalipt.sevenmodullesson51.models.Writer
import uz.evkalipt.sevenmodullesson51.utils.OnDataPass

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WriterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }
    lateinit var binding: FragmentWriterBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var writerList:ArrayList<Writer>
    lateinit var onDataPass: OnDataPass

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var gson: Gson
    var uid:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriterBinding.inflate(layoutInflater)
        sharedPreferences = requireActivity().getSharedPreferences("writers", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        gson = Gson()
        writerList = ArrayList()
        uid = arguments?.getString("uid")
        load()

        binding.toolbarWriter.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.cardSave.setOnClickListener {
            val uids = sharedPreferences.getString("uids", "")
            if (uids!=""){
                var type = object :TypeToken<ArrayList<String>>(){}.type
                val fromJson = gson.fromJson<ArrayList<String>>(uids, type)
                var boolean = false
                for (s in fromJson) {
                    if (s == uid){
                        boolean = true
                        break
                    }
                }
                if (boolean){
                    fromJson.remove(uid.toString())
                    val toJson = gson.toJson(fromJson)
                    editor.putString("uids", toJson)
                    editor.commit()
                    binding.cardSaveGreen.visibility = View.INVISIBLE
                }else{
                    fromJson.add(uid.toString())
                    val toJson = gson.toJson(fromJson)
                    editor.putString("uids", toJson)
                    editor.commit()
                    binding.cardSaveGreen.visibility = View.VISIBLE

                }
            }else{
                var list = ArrayList<String>()
                list.add(uid.toString())
                val toJson = gson.toJson(list)
                editor.putString("uids", toJson)
                editor.commit()
                binding.cardSaveGreen.visibility = View.VISIBLE
            }
            load()
        }

        binding.cardSaveTool.setOnClickListener {
            val uids = sharedPreferences.getString("uids", "")
            if (uids!=""){
                var type = object :TypeToken<ArrayList<String>>(){}.type
                val fromJson = gson.fromJson<ArrayList<String>>(uids, type)
                var boolean = false
                for (s in fromJson) {
                    if (s == uid){
                        boolean = true
                        break
                    }
                }
                if (boolean){
                    fromJson.remove(uid.toString())
                    val toJson = gson.toJson(fromJson)
                    editor.putString("uids", toJson)
                    editor.commit()
                    binding.cardSaveTool2.visibility = View.INVISIBLE
                }else{
                    fromJson.add(uid.toString())
                    val toJson = gson.toJson(fromJson)
                    editor.putString("uids", toJson)
                    editor.commit()
                    binding.cardSaveTool2.visibility = View.VISIBLE

                }
            }else{
                var list = ArrayList<String>()
                list.add(uid.toString())
                val toJson = gson.toJson(list)
                editor.putString("uids", toJson)
                editor.commit()
                binding.cardSaveTool2.visibility = View.VISIBLE
            }
            load()
        }

        binding.cardSearch.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("uid", uid)
            findNavController().navigate(R.id.search2Fragment, bundle)
        }

        binding.cardSearchTool.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("uid", uid)
            findNavController().navigate(R.id.search2Fragment, bundle)
        }

        return binding.root
    }

    fun load(){
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val result = it.result
                    result?.forEach { it ->
                        val toObject = it.toObject(Writer::class.java)
                        writerList.add(toObject)
                    }
                }
                var writer1 = Writer()
                for (writer in writerList) {
                    if (writer.uid.toString() == uid){
                        writer1 = writer
                        break
                    }
                }
                binding.toolbarWriter.title = writer1.fullName
                binding.authorName.text = writer1.fullName
                binding.authorYear.text = "(${writer1.yearOfBirth} - ${writer1.yearOfBeath})"
                binding.fullText.text = writer1.fullText
                binding.tvTool.text = writer1.fullName
                if (writer1.imageUrl!="") {
                    Picasso.with(binding.root.context).load(writer1.imageUrl).into(binding.ivWriter)
                }else{
                    binding.ivWriter.setImageResource(R.drawable.image2)
                }
                val uids = sharedPreferences.getString("uids", "")
                if (uids!=""){
                    var type = object :TypeToken<ArrayList<String>>(){}.type
                    val fromJson = gson.fromJson<ArrayList<String>>(uids, type)
                    var boolean = false
                    for (s in fromJson) {
                        if (s == uid){
                            boolean=true
                            break
                        }
                    }
                    if (boolean){
                        binding.cardSaveGreen.visibility = View.VISIBLE
                        binding.cardSaveTool2.visibility= View.VISIBLE
                    }else{
                        binding.cardSaveGreen.visibility = View.INVISIBLE
                        binding.cardSaveTool2.visibility= View.INVISIBLE
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDataPass.onDataPass(0)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onDataPass = context as OnDataPass
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WriterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}