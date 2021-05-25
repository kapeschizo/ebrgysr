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

class ForgetPass : AppCompatActivity() {
    var emailForgetETxt: EditText? = null
    var forgetBtn: Button? = null
    var url: kotlin.String = "http://192.168.1.6/Ebrgy/API/forgotAPI.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        val forgetBtn = findViewById<TextView>(R.id.forgetBtn)
        emailForgetETxt = findViewById<EditText>(R.id.emailForgetEtxt)
        forgetBtn!!.setOnClickListener {
            forget(
                emailForgetETxt!!.text.toString()
            )
        }
    }

    private fun forget(email: String) {

        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                emailForgetETxt!!.setText(" ")
                Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
                val loginIntent = Intent(this,Login::class.java)
                startActivity(loginIntent)

            }, Response.ErrorListener { error ->
                emailForgetETxt!!.setText(" ")
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): kotlin.collections.MutableMap<String, String> {
                    val map: kotlin.collections.MutableMap<String, String> =
                        HashMap<String, String>()
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