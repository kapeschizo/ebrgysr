package com.bsrebrgy.ebsrv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import javax.security.auth.login.LoginException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginpagebtn = findViewById<Button>(R.id.loginPageBtn)
        loginpagebtn.setOnClickListener {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }

    }
}