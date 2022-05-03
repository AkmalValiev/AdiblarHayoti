package uz.evkalipt.sevenmodullesson51

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.evkalipt.sevenmodullesson51.adapters.SpinnerAdapter
import uz.evkalipt.sevenmodullesson51.databinding.FragmentAddWriterBinding
import uz.evkalipt.sevenmodullesson51.models.Writer
import uz.evkalipt.sevenmodullesson51.utils.OnDataPass

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddWriterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentAddWriterBinding
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var list: ArrayList<String>

    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference: StorageReference
    lateinit var fireStore: FirebaseFirestore
    lateinit var onDataPass: OnDataPass
    var imageUri:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddWriterBinding.inflate(layoutInflater)
        imageUri = ""
        loadList()
        spinnerAdapter = SpinnerAdapter(list)
        binding.spinner.adapter = spinnerAdapter
        fireStore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("images")

        binding.cardImageWriter.setOnClickListener {
            askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE){
                getImageContent.launch("image/*")
            }.onDeclined {e->
                if (e.hasDenied()){
                    AlertDialog.Builder(binding.root.context)
                        .setMessage("Ruxsat bering")
                        .setPositiveButton("Ruxsat"){d, w->
                            e.askAgain()
                        }.setNegativeButton("yo'q"){d, w->
                            d.dismiss()
                        }.show()
                }
                if (e.hasForeverDenied()){
                    e.goToSettings()
                }
            }
        }

        binding.cardSave.setOnClickListener {
            if (!binding.edit1.text.toString().trim().equals("") &&
                    !binding.edit2.text.toString().trim().equals("") &&
                    !binding.edit3.text.toString().trim().equals("") &&
                    !binding.edit4.text.toString().trim().equals("")){
                var type = binding.spinner.selectedItem
                var uid = System.currentTimeMillis().toString()
                var writer = Writer(imageUri, binding.edit1.text.toString(),
                    binding.edit2.text.toString(), binding.edit3.text.toString(), type.toString(), binding.edit4.text.toString(), 0, uid)
                fireStore.collection("writers").add(writer)
                    .addOnSuccessListener {
                        Toast.makeText(binding.root.context, "Saved!", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }.addOnFailureListener {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    }


            }else{
                Toast.makeText(binding.root.context, "Qatorlarni to'ldiring!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }

    private var getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null){
            binding.imageWriter.setImageURI(uri)
        var m = System.currentTimeMillis().toString()
        val uplaodUri = reference.child(m).putFile(uri)

        uplaodUri.addOnSuccessListener {
            if (it.task.isSuccessful) {
                val downloadUrl = it?.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener { uri2 ->
                    imageUri = uri2.toString()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
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

    private fun loadList() {
        list = ArrayList()
        list.add("Mumtoz adabiyoti")
        list.add("O'zbek adabiyoti")
        list.add("Jahon adabiyoti")
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddWriterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}