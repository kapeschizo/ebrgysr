package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginpagebtn = findViewById<Button>(R.id.loginPageBtn)
        loginpagebtn.setOnClickListener {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)

        }

        val logoImg = findViewById<ImageView>(R.id.logoImg)
        val rotate = RotateAnimation(
            0F, 360F,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotate.duration = 20000
        rotate.repeatCount = Animation.INFINITE
        logoImg.startAnimation(rotate)

    }
}