package uz.evkalipt.sevenmodullesson51

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationBarView
import me.ibrahimsn.lib.OnItemSelectedListener
import uz.evkalipt.sevenmodullesson51.databinding.ActivityMainBinding
import uz.evkalipt.sevenmodullesson51.databinding.ItemForTabBinding
import uz.evkalipt.sevenmodullesson51.utils.OnDataPass

class MainActivity : AppCompatActivity(), OnDataPass {
    lateinit var binding: ActivityMainBinding
    var data1 = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.fragment)

        binding.bottomNav.onItemSelected = {
            when(it){
                0->{
                    navController.navigateUp()
                    navController.navigate(R.id.fragmentFragment)
                }
                1->{
                    navController.navigateUp()
                    navController.navigate(R.id.saveFragment)
                }
                2->{
                    navController.navigateUp()
                    navController.navigate(R.id.settingFragment)
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragment).navigateUp()
    }

    override fun onDataPass(data: Int) {
        data1 = data
        if (data1 == 1){
            binding.bottomNav.visibility = View.INVISIBLE
        }else if (data1==0){
            binding.bottomNav.visibility = View.VISIBLE
        }
    }

}