package com.cogitator.firebasemlkit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.cogitator.firebasemlkit.textRecognizer.TextRecognizerActivity

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 25/05/2018 (MM/DD/YYYY)
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_spalsh)
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, TextRecognizerActivity::class.java))
            finish()
        }, 500)
    }
}