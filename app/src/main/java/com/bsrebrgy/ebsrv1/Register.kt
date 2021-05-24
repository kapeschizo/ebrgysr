package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.*


class Register : AppCompatActivity() {
    var userRegEtxt: EditText? = null
    var emailRegEtxt: EditText? = null
    var signUpBtn: Button? = null
    var url: String = "http://192.168.1.9/Ebrgy/API/sendEmail.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        userRegEtxt = findViewById<EditText>(R.id.userSignEtxt)
        emailRegEtxt = findViewById<EditText>(R.id.emailSignEtxt)
        signUpBtn = findViewById<Button>(R.id.signUpBtn)

        signUpBtn!!.setOnClickListener {
            register(
                userRegEtxt!!.text.toString(),
                emailRegEtxt!!.text.toString()
            )
        }
    }

    private fun register(user: String, email: String) {

        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                userRegEtxt!!.setText(" ")
                emailRegEtxt!!.setText(" ")
                Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()

                if (response == "Register Successful") {
                    val loginIntent = Intent(this, Login::class.java)
                    startActivity(loginIntent)
                }

            }, Response.ErrorListener { error ->
                userRegEtxt!!.setText(" ")
                emailRegEtxt!!.setText(" ")
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): kotlin.collections.MutableMap<String, String> {
                    val map: kotlin.collections.MutableMap<String, String> =
                        HashMap<String, String>()
                    map.put("user", user)
                    map.put("email", email)
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