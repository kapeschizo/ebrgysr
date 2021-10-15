package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DashboardUser : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var logoutImg : ImageView? = null

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

        logoutImg = findViewById(R.id.logoutImg)
        logoutImg?.setOnClickListener {
            val alertdialog : AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Exit Dialog")
            alertdialog.setMessage("Do you want to Logout?")

            alertdialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes") {
                dialog, which -> session.logoutUser()
                dialog.dismiss()}

            alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No") {
                    dialog, which ->
                dialog.dismiss()}
            alertdialog.show()

            val announceImg = findViewById<ImageView>(R.id.announceImg)
            announceImg.setOnClickListener {
                val announcement = Intent(this,Announcement::class.java)
                startActivity(announcement)
            }
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
                    val pimg = jsonObject.getString("pimg")
                    val uemail = jsonObject.getString("email")

                    val urlimg = "http://www.barangaysanroqueantipolo.site/$pimg"

                    val fullNameTxt = findViewById<TextView>(R.id.fullNameTxt)
                    fullNameTxt.setText(fname+" "+mname+" "+lname)

                    val emailTxt = findViewById<TextView>(R.id.emailTxt)
                    emailTxt.setText(uemail)

                    val userImg = findViewById<ImageView>(R.id.userImg)
                    Picasso.with(this).load(urlimg).into(userImg)

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
