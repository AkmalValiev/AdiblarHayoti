package uz.evkalipt.sevenmodullesson51

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(kotlinx.coroutines.Runnable {
          var intent = Intent(this, MainActivity::class.java)
          startActivity(intent)
            finish()
        }, 2000)

    }
}