package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class UpdateIdsSignature : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_ids_signature)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        val sigBtn = findViewById<Button>(R.id.camBtn2)
        sigBtn.setOnClickListener {
        val sig = Intent(this, UpdateSignature::class.java)
        startActivity(sig)
        }
        val vidBtn = findViewById<Button>(R.id.camBtn3)
        vidBtn.setOnClickListener {
        val vid = Intent(this, UpdateUploadID::class.java)
        startActivity(vid)
        }
        val selfBtn = findViewById<Button>(R.id.camBtn4)
        selfBtn.setOnClickListener {
        val self = Intent(this, UpdateSelfie::class.java)
        startActivity(self)
        }
    }

    private fun getUserDetails() {
        val url = "http://www.barangaysanroqueantipolo.site/API/updateIdSignatureApi.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val psig = jsonObject.getString("psig")
                    val pselfie = jsonObject.getString("pselfie")
                    val vid = jsonObject.getString("vid")

                    val picsig = "http://www.barangaysanroqueantipolo.site/$psig"
                    val camIview2 = findViewById<ImageView>(R.id.camIview2)
                    Picasso.with(this).load(picsig).into(camIview2)

                    val picvid = "http://www.barangaysanroqueantipolo.site/$vid"
                    val camIview3 = findViewById<ImageView>(R.id.camIview3)
                    Picasso.with(this).load(picvid).into(camIview3)

                    val picselfie = "http://www.barangaysanroqueantipolo.site/$pselfie"
                    val camIview4 = findViewById<ImageView>(R.id.camIview4)
                    Picasso.with(this).load(picselfie).into(camIview4)

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