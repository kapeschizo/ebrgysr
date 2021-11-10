package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MyCertificate : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_certificate)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        val certRes = findViewById<ImageView>(R.id.certRes)
        certRes.setOnClickListener {
            val certRes = Intent(this, Residency::class.java)
            startActivity(certRes)
        }

        val certInd = findViewById<ImageView>(R.id.certInd)
        certInd.setOnClickListener {
            val certInd = Intent(this, Indigency::class.java)
            startActivity(certInd)
        }

        val certWork = findViewById<ImageView>(R.id.certWork)
        certWork.setOnClickListener {
            val certWork = Intent(this, WorkingClearance::class.java)
            startActivity(certWork)
        }

        val certID = findViewById<ImageView>(R.id.certID)
        certID.setOnClickListener {
            val certWork = Intent(this, ResidentID::class.java)
            startActivity(certWork)
        }
    }
}