package com.bsrebrgy.ebsrv1

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DashboardUser : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var logoutBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_user)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.noimg)
        val rounded = RoundedBitmapDrawableFactory.create(resources, bitmap)
        rounded.cornerRadius = 15f
        val logo = findViewById<ImageView>(R.id.userImg)
        logo.setImageDrawable(rounded)

        logoutBtn = findViewById(R.id.logoutBtn)
        logoutBtn?.setOnClickListener {
            session.logoutUser()
        }
    }

    private fun getUserDetails() {
    val url = "http://www.barangaysanroqueantipolo.site/API/dashUserApi.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val fname = jsonObject.getString("fname")
                    val mname = jsonObject.getString("mname")
                    val lname = jsonObject.getString("lname")

                    val fullNameTxt = findViewById<TextView>(R.id.fullNameTxt)
                    fullNameTxt.setText(fname+" "+mname+" "+lname)

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

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
}