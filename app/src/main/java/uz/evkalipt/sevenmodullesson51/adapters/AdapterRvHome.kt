package uz.evkalipt.sevenmodullesson51.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import uz.evkalipt.sevenmodullesson51.R
import uz.evkalipt.sevenmodullesson51.databinding.ItemForRvHomeBinding
import uz.evkalipt.sevenmodullesson51.models.Writer

class AdapterRvHome(var list: List<Writer>, var context: Context, var onClick: OnClick):RecyclerView.Adapter<AdapterRvHome.Vh>() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var gson: Gson

    inner class Vh(var itemForRvHomeBinding: ItemForRvHomeBinding):RecyclerView.ViewHolder(itemForRvHomeBinding.root){
        fun onBind(writer: Writer, position: Int){
            itemForRvHomeBinding.fullName.text = writer.fullName
            itemForRvHomeBinding.year.text = "(${writer.yearOfBirth} - ${writer.yearOfBeath})"
            if (writer.imageUrl!="") {
                Picasso.with(context).load(writer.imageUrl).into(itemForRvHomeBinding.imageHome)
            }else{
                itemForRvHomeBinding.imageHome.setImageResource(R.drawable.image2)
            }
            itemForRvHomeBinding.saveWhite.setOnClickListener {
                onClick.onSave(writer, position)
            }
            sharedPreferences = context.getSharedPreferences("writers", Context.MODE_PRIVATE)
            editor = sharedPreferences.edit()
            gson = Gson()
            val uids = sharedPreferences.getString("uids", "")
            var type = object : TypeToken<ArrayList<String>>(){}.type
            val fromJson = gson.fromJson<ArrayList<String>>(uids, type)
            var boolean = false
            if (uids!="") {
                for (i in 0 until fromJson.size) {
                    if (fromJson[i] == writer.uid.toString()) {
                        boolean = true
                        break
                    }
                }
            }
            if (boolean){
                itemForRvHomeBinding.saveGreen.visibility = View.VISIBLE
            }else{
                itemForRvHomeBinding.saveGreen.visibility = View.INVISIBLE
            }
            itemForRvHomeBinding.root.setOnClickListener {
                onClick.onWriter(writer)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemForRvHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClick{
        fun onSave(writer: Writer, position: Int)
        fun onWriter(writer: Writer)
    }

}