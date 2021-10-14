package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Register : AppCompatActivity() {
    var userRegEtxt: EditText? = null
    var emailRegEtxt: EditText? = null
    var signUpBtn: Button? = null
    var loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userRegEtxt = findViewById(R.id.userSignEtxt)
        emailRegEtxt = findViewById(R.id.emailSignEtxt)
        signUpBtn = findViewById(R.id.signUpBtn)
        loading = findViewById(R.id.loading)

        signUpBtn?.setOnClickListener {
            register()
        }
    }
    private fun register() {
        loading?.visibility = View.VISIBLE
        signUpBtn?.visibility = View.GONE
        val url = "http://www.barangaysanroqueantipolo.site/API/sendEmailApi.php"
        val user = userRegEtxt?.text.toString().trim { it <= ' ' }
        val email = emailRegEtxt?.text.toString().trim { it <= ' ' }

        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if(success == "1")
                    {
                        val loginIntent = Intent(this, Login::class.java)
                        startActivity(loginIntent)

                        Toast.makeText(applicationContext,"Registered Successful",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                        loading?.visibility = View.GONE
                        signUpBtn?.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Register Error! $e", Toast.LENGTH_SHORT).show()
                    loading?.visibility = View.GONE
                    signUpBtn?.visibility = View.VISIBLE

                } }, Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                loading?.visibility = View.GONE
                signUpBtn?.visibility = View.VISIBLE
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