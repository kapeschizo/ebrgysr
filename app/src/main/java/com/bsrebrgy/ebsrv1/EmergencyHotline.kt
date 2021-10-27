package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class EmergencyHotline : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_hotline)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        val brgyImg = findViewById<ImageView>(R.id.brgyImg)
        brgyImg.setOnClickListener {
            val hotline = Intent(this, Hotlines::class.java)
            startActivity(hotline)
        }
        val hospImg = findViewById<ImageView>(R.id.hospImg)
        hospImg.setOnClickListener {
            val hotline2 = Intent(this, Hotlines2::class.java)
            startActivity(hotline2)
        }
        val fireImg = findViewById<ImageView>(R.id.fireImg)
        fireImg.setOnClickListener {
            val hotline3 = Intent(this, Hotlines3::class.java)
            startActivity(hotline3)
        }
        val polImg = findViewById<ImageView>(R.id.polImg)
        polImg.setOnClickListener {
            val hotline4 = Intent(this, Hotlines4::class.java)
            startActivity(hotline4)
        }
        val cusImg = findViewById<ImageView>(R.id.cusImg)
        cusImg.setOnClickListener {
            val hotline5 = Intent(this, Hotlines5::class.java)
            startActivity(hotline5)
        }
    }
}