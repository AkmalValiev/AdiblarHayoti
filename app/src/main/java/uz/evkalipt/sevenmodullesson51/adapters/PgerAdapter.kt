package uz.evkalipt.sevenmodullesson51.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.evkalipt.sevenmodullesson51.ForPgerViewFragment
import uz.evkalipt.sevenmodullesson51.databinding.FragmentForPgerViewBinding
import uz.evkalipt.sevenmodullesson51.models.ForPager
import uz.evkalipt.sevenmodullesson51.models.Writer

@Suppress("DEPRECATION")
class PgerAdapter(var list: List<ForPager>, var fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return ForPgerViewFragment.newInstance(list[position])
    }
}