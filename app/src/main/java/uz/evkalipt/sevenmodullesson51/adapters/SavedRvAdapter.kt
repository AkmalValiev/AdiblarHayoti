package uz.evkalipt.sevenmodullesson51.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.evkalipt.sevenmodullesson51.R
import uz.evkalipt.sevenmodullesson51.databinding.ItemForRvHomeBinding
import uz.evkalipt.sevenmodullesson51.models.Writer

class SavedRvAdapter(var list: List<Writer>, var context: Context, var onClickMy: OnClickMy):RecyclerView.Adapter<SavedRvAdapter.Vh>() {

    inner class Vh(var itemForRvHomeBinding: ItemForRvHomeBinding):RecyclerView.ViewHolder(itemForRvHomeBinding.root){
        fun onBind(writer: Writer, position: Int){
            itemForRvHomeBinding.saveGreen.visibility = View.VISIBLE
            itemForRvHomeBinding.year.text = "(${writer.yearOfBirth} - ${writer.yearOfBeath})"
            itemForRvHomeBinding.fullName.text = writer.fullName
            if (writer.imageUrl!="") {
                Picasso.with(context).load(writer.imageUrl).into(itemForRvHomeBinding.imageHome)
            }else{
                itemForRvHomeBinding.imageHome.setImageResource(R.drawable.image2)
            }
            itemForRvHomeBinding.saveGreen.setOnClickListener {
                onClickMy.myClick(writer, position)
            }
            itemForRvHomeBinding.root.setOnClickListener {
                onClickMy.onClickWriter(writer)
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
    interface OnClickMy{
        fun myClick(writer: Writer, position: Int)
        fun onClickWriter(writer: Writer)
    }
}