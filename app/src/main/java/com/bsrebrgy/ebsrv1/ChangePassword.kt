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
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ChangePassword : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var curPass: EditText? = null
    var newPass: EditText? = null
    var conPass: EditText? = null
    var subPass : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        curPass = findViewById(R.id.curPass)
        newPass = findViewById(R.id.newPass)
        conPass = findViewById(R.id.conPass)

        subPass = findViewById(R.id.subPass)

        subPass?.setOnClickListener {
            changepass()
        }
    }

    private fun changepass() {
        val url = "http://www.barangaysanroqueantipolo.site/API/changepasswordApi.php"
        val username = user.toString().trim { it <= ' ' }
        val curpass = curPass?.text.toString().trim { it <= ' ' }
        val newpass = curPass?.text.toString().trim { it <= ' ' }
        val conpass = curPass?.text.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if(success == "0")
                    {
                        val dashIntent = Intent(this, DashboardUser::class.java)
                        startActivity(dashIntent)
                        Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Register Error! $e", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val map: MutableMap<String, String> =
                        HashMap<String, String>()
                    map.put("user", username)
                    map.put("curpass", curpass)
                    map.put("newpass", newpass)
                    map.put("conpass", conpass)
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