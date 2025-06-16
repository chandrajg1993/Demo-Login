package com.scci.demoapplogin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.scci.demoapplogin.databinding.ActivitySplashScreenBinding
import com.scci.demoapplogin.utility.CommonMethod
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var commonMethod: CommonMethod
    private var timer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(binding.root)

        commonMethod = CommonMethod(this@SplashScreenActivity)
    }


    private fun SessionMethodCall() {
          if (commonMethod.getIsActive("isLogin")) {
              startActivity(Intent(this@SplashScreenActivity, DashBoardActivity::class.java))
          } else {
              startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
          }
    }


    override fun onResume() {
        super.onResume()

        timer = object : CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds: Long = millisUntilFinished/1000%60

                if (seconds%3 == 0L) {
                    timer?.cancel()
                    SessionMethodCall()
                }
            }

            override fun onFinish() {
                // Toast.makeText(this@SplashScreen, "Finish", Toast.LENGTH_SHORT).show()
            }
        }
        timer?.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }


}