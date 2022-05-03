package uz.evkalipt.sevenmodullesson51

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import uz.evkalipt.sevenmodullesson51.databinding.FragmentSearch2Binding
import uz.evkalipt.sevenmodullesson51.models.Writer
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Search2Fragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentSearch2Binding
    lateinit var firestore: FirebaseFirestore
    lateinit var writerList:ArrayList<Writer>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearch2Binding.inflate(layoutInflater)
        writerList = ArrayList()
        val uid = arguments?.getString("uid")
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("writers")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val result = it.result
                    result?.forEach {query ->
                        val toObject = query.toObject(Writer::class.java)
                        writerList.add(toObject)
                    }
                }
                var writer1 = Writer()
                for (writer in writerList) {
                    if (writer.uid.toString() == uid.toString()){
                        writer1 = writer
                        break
                    }
                }
                binding.fullNameWriter.text = writer1.fullName
                binding.year.text = "(${writer1.yearOfBirth} - ${writer1.yearOfBeath})"
                binding.tvWriter.text = writer1.fullText
                if (writer1.imageUrl!=""){
                    Picasso.with(binding.root.context).load(writer1.imageUrl).into(binding.ivWriter)
                }else{
                    binding.ivWriter.setImageResource(R.drawable.image2)
                }
            }

        binding.searchViewWriter.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                binding.tvWriter.setTextToHighlight(p0.toString())
                binding.tvWriter.setTextHighlightColor(R.color.yellow)
                binding.tvWriter.highlight()
            }

        })

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Search2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}