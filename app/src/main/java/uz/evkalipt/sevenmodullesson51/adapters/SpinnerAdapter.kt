package uz.evkalipt.sevenmodullesson51.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import uz.evkalipt.sevenmodullesson51.databinding.ItemForSpinnerBinding

class SpinnerAdapter(var list: List<String>):BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var vh:Vh
        if (p1==null){
            val inflate = ItemForSpinnerBinding.inflate(LayoutInflater.from(p2?.context), p2, false)
            vh = Vh(inflate)
        }else{
            vh = Vh(ItemForSpinnerBinding.bind(p1))
        }
        vh.itemForSpinnerBinding.tvSpinner.text = list[p0]
        return vh.itemView
    }

    inner class Vh{
        var itemView:View
        var itemForSpinnerBinding:ItemForSpinnerBinding
        constructor(itemForSpinnerBinding: ItemForSpinnerBinding){
            itemView = itemForSpinnerBinding.root
            this.itemForSpinnerBinding = itemForSpinnerBinding
        }
    }
}