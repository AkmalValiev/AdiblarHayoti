package uz.evkalipt.sevenmodullesson51

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.item_for_tab.view.*
import uz.evkalipt.sevenmodullesson51.adapters.PgerAdapter
import uz.evkalipt.sevenmodullesson51.databinding.FragmentFragmentBinding
import uz.evkalipt.sevenmodullesson51.databinding.ItemForTabBinding
import uz.evkalipt.sevenmodullesson51.models.ForPager
import uz.evkalipt.sevenmodullesson51.models.Writer

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentFragmentBinding
    lateinit var pagerAdapter:PgerAdapter
    lateinit var writerList: ArrayList<ForPager>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFragmentBinding.inflate(layoutInflater)
        loadWriterList()
        pagerAdapter = PgerAdapter(writerList, childFragmentManager)
        binding.pagerView.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.pagerView)

        var tabCount = binding.tabLayout.tabCount
        for (i in 0 until tabCount){
            var tabView = ItemForTabBinding.inflate(layoutInflater)
            binding.tabLayout.getTabAt(i)?.customView = tabView.root
            tabView.tv1.text = writerList[i].title
            tabView.tv2.text = writerList[i].title

            if (i==0){
                tabView.cardGreen.visibility = View.VISIBLE
            }else{
                tabView.cardGreen.visibility = View.INVISIBLE
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var tabView = tab?.customView
                tabView?.card_green?.visibility = View.VISIBLE

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var tabView = tab?.customView
                tabView?.card_green?.visibility = View.INVISIBLE
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.searchHome.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        return binding.root
    }

    private fun loadWriterList() {
        writerList = ArrayList()
        writerList.add(ForPager("Mumtoz adabiyoti", ArrayList<Writer>()))
        writerList.add(ForPager("O'zbek adabiyoti", ArrayList<Writer>()))
        writerList.add(ForPager("Jahon adabiyoti", ArrayList<Writer>()))
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}