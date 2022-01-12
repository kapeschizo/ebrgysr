package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UpdateProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var session : SessionManager
        var user : String? = null

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        val perBtn = findViewById<Button>(R.id.perBtn)
        perBtn.setOnClickListener {
            val personal = Intent(this, UpdatePersonalProfile::class.java)
            startActivity(personal)
        }

        val homBtn = findViewById<Button>(R.id.homBtn)
        homBtn.setOnClickListener {
            val home = Intent(this, UpdateHomeAddress::class.java)
            startActivity(home)
        }

        val othBtn = findViewById<Button>(R.id.othBtn)
        othBtn.setOnClickListener {
            val other = Intent(this, UpdateOtherDetails::class.java)
            startActivity(other)
        }

        val picBtn = findViewById<Button>(R.id.picBtn)
        picBtn.setOnClickListener {
            val sigandid = Intent(this, UpdateIdsSignature::class.java)
            startActivity(sigandid)
        }

        val cpassBtn = findViewById<Button>(R.id.cpassBtn)
        cpassBtn.setOnClickListener {
            val cpass = Intent(this, ChangePassword::class.java)
            startActivity(cpass)
        }
    }
}