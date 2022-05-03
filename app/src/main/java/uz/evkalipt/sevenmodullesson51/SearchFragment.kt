package uz.evkalipt.sevenmodullesson51

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.evkalipt.sevenmodullesson51.adapters.AdapterRvHome
import uz.evkalipt.sevenmodullesson51.databinding.FragmentSearchBinding
import uz.evkalipt.sevenmodullesson51.models.Writer
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentSearchBinding
    lateinit var fireStore: FirebaseFirestore
    lateinit var writerList:ArrayList<Writer>
    lateinit var adapterRvHome: AdapterRvHome
    lateinit var writersSaved:ArrayList<Writer>

    lateinit var gson: Gson
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var tempArrayList: ArrayList<Writer>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        fireStore = FirebaseFirestore.getInstance()
        writerList = ArrayList()
        tempArrayList = ArrayList()
        writersSaved = ArrayList()
        val save = arguments?.getString("save")

        fireStore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result

                    result?.forEach { resultQuery ->
                        val writer = resultQuery.toObject(Writer::class.java)
                        writerList.add(writer)
                    }
                    if (save=="save") {
                        sharedPreferences =
                            requireActivity().getSharedPreferences("writers", Context.MODE_PRIVATE)
                        val uidsSave = sharedPreferences.getString("uids", "")
                        if (uidsSave != "") {
                            var type = object :TypeToken<ArrayList<String>>(){}.type
                            gson = Gson()
                            val fromJson = gson.fromJson<ArrayList<String>>(uidsSave, type)
                            for (s in fromJson) {
                                for (writer in writerList) {
                                    if (writer.uid.toString()==s){
                                        writersSaved.add(writer)
                                    }
                                }
                            }
                        }
                        tempArrayList.addAll(writersSaved)
                    }else{
                        tempArrayList.addAll(writerList)
                    }
                }
                adapterRvHome = AdapterRvHome(tempArrayList, binding.root.context, object :AdapterRvHome.OnClick{
                    override fun onSave(writer: Writer, position:Int) {
                        sharedPreferences = requireActivity().getSharedPreferences("writers", Context.MODE_PRIVATE)
                        editor = sharedPreferences.edit()
                        gson = Gson()
                        val uids = sharedPreferences.getString("uids", "")
                        if (uids==""){
                            var uidList = ArrayList<String>()
                            uidList.add(writer.uid.toString())
                            val toJson = gson.toJson(uidList)
                            editor.putString("uids", toJson)
                            editor.commit()
                        }else{
                            var type = object : TypeToken<ArrayList<String>>(){}.type
                            val fromJson = gson.fromJson<ArrayList<String>>(uids, type)
                            var boolean = false
                            var index = -1
                            for (i in 0 until fromJson.size){
                                if (fromJson[i] == writer.uid.toString()){
                                    index = i
                                    boolean = true
                                    break
                                }
                            }
                            if (boolean){
                                fromJson.remove(fromJson[index])
                            }else{
                                fromJson.add(writer.uid.toString())
                            }
                            val toJson = gson.toJson(fromJson)
                            editor.putString("uids", toJson)
                            editor.commit()
                        }
                        adapterRvHome.notifyItemRemoved(position)
                        adapterRvHome.notifyItemRangeChanged(position, writerList.size)
                    }

                    override fun onWriter(writer: Writer) {
                        var bundle = Bundle()
                        bundle.putString("uid", writer.uid.toString())
                        findNavController().navigate(R.id.writerFragment, bundle)
                    }

                })
                binding.rvSearch.adapter = adapterRvHome

            }
        binding.searchV.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText?.toLowerCase(Locale.getDefault())
                if (searchText!!.isNotEmpty()){
                    if (save=="saved"){
                        writersSaved.forEach {
                            if (it.fullName?.toLowerCase(Locale.getDefault())!!.contains(searchText)){
                                tempArrayList.add(it)
                            }
                        }
                    }else {
                        writerList.forEach {
                            if (it.fullName?.toLowerCase(Locale.getDefault())!!
                                    .contains(searchText)
                            ) {
                                tempArrayList.add(it)
                            }
                        }
                    }
                    adapterRvHome.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    if (save == "save") {
                        tempArrayList.addAll(writersSaved)
                    }else{
                        tempArrayList.addAll(writerList)
                    }
                    adapterRvHome.notifyDataSetChanged()
                }

                return false
            }

        })


        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}