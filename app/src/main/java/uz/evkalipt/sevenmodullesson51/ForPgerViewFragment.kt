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
import uz.evkalipt.sevenmodullesson51.adapters.AdapterRvHome
import uz.evkalipt.sevenmodullesson51.databinding.FragmentForPgerViewBinding
import uz.evkalipt.sevenmodullesson51.models.ForPager
import uz.evkalipt.sevenmodullesson51.models.Writer
import uz.evkalipt.sevenmodullesson51.utils.OnDataPass

private const val ARG_PARAM1 = "param1"

class ForPgerViewFragment : Fragment() {
    private var param1: ForPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as ForPager
        }
    }
    lateinit var binding: FragmentForPgerViewBinding
    lateinit var fireStore: FirebaseFirestore
    lateinit var writerList:ArrayList<Writer>
    lateinit var adapterRvHome: AdapterRvHome

    lateinit var gson: Gson
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var onDataPass: OnDataPass
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForPgerViewBinding.inflate(layoutInflater)
        fireStore = FirebaseFirestore.getInstance()
        writerList = ArrayList()



        fireStore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val result = it.result

                    result?.forEach {resultQuery->
                        val writer = resultQuery.toObject(Writer::class.java)
                        if (param1?.title == "Mumtoz adabiyoti"){
                            if (writer.type=="Mumtoz adabiyoti"){
                                writerList.add(writer)
                            }
                        }else if (param1?.title == "O'zbek adabiyoti"){
                            if (writer.type == "O'zbek adabiyoti"){
                                writerList.add(writer)
                            }
                        }else if (param1?.title == "Jahon adabiyoti"){
                            if (writer.type == "Jahon adabiyoti"){
                                writerList.add(writer)
                            }
                        }
                    }
                }
                adapterRvHome = AdapterRvHome(writerList, binding.root.context, object :AdapterRvHome.OnClick{
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
                            var type = object :TypeToken<ArrayList<String>>(){}.type
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
                        onDataPass.onDataPass(1)
                    }

                })
                binding.rvHome.adapter = adapterRvHome
            }

        return binding.root
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
        fun newInstance(param1: ForPager) =
            ForPgerViewFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}