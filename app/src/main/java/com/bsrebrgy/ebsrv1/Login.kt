package com.bsrebrgy.ebsrv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class Login : AppCompatActivity() {
    var userLoginEtxt: EditText? = null
    var passLoginETxt: EditText? = null
    var loginBtn: Button? = null
    var url: kotlin.String = "http://192.168.1.9/Ebrgy/API/loginAPI.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signuptxt = findViewById<TextView>(R.id.signupTxt)
        signuptxt.setOnClickListener {
            val signUpIntent = Intent(this, Register::class.java)
            startActivity(signUpIntent)
        }

        userLoginEtxt = findViewById<EditText>(R.id.userLoginEtxt)
        passLoginETxt = findViewById<EditText>(R.id.passLoginEtxt)
        loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn!!.setOnClickListener {
            login(
                userLoginEtxt!!.text.toString(),
                passLoginETxt!!.text.toString()
            )
        }
    }
    private fun login(user: String, pass: String) {

        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                userLoginEtxt!!.setText(" ")
                passLoginETxt!!.setText(" ")
                Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()

                if (response == "Register Successful") {
                    val loginIntent = Intent(this, Login::class.java)
                    startActivity(loginIntent)
                }

            }, Response.ErrorListener { error ->
                userLoginEtxt!!.setText(" ")
                passLoginETxt!!.setText(" ")
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): kotlin.collections.MutableMap<String, String> {
                    val map: kotlin.collections.MutableMap<String, String> =
                        HashMap<String, String>()
                    map.put("user", user)
                    map.put("pass", pass)
                    return map
                }
            }

        request.retryPolicy = DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)

    }
}