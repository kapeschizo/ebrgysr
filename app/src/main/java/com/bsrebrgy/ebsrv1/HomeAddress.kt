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

class HomeAddress : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var brgyTxt : EditText? = null
    var munTxt : EditText? = null
    var provTxt : EditText? = null
    var zipcTxt : EditText? = null
    var purokTxt : EditText? = null
    var homeaddBtn : Button? = null
    var houseTxt : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_address)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        homeaddBtn = findViewById(R.id.otherBtn)
        homeaddBtn?.setOnClickListener {
            val bundle: Bundle? = intent.extras
            val fname = bundle?.getString("fname")
            val mname = bundle?.getString("mname")
            val lname = bundle?.getString("lname")
            val sexname = bundle?.getString("sexname")
            val extname = bundle?.getString("extname")
            val maidenname = bundle?.getString("maidenname")
            val age = bundle?.getString("age")
            val bdate = bundle?.getString("bdate")
            val bplace = bundle?.getString("bplace")
            val mobo = bundle?.getString("mobo")
            houseTxt = findViewById(R.id.houseTxt)
            val house = houseTxt?.text.toString()
            purokTxt = findViewById(R.id.purokTxt)
            val purok = purokTxt?.text.toString()

            val otherdetail = Intent(this, OtherDetails::class.java)
            otherdetail.putExtra("fname",fname)
            otherdetail.putExtra("mname",mname)
            otherdetail.putExtra("lname",lname)
            otherdetail.putExtra("sexname",sexname)
            otherdetail.putExtra("extname",extname)
            otherdetail.putExtra("maidenname",maidenname)
            otherdetail.putExtra("age",age)
            otherdetail.putExtra("bdate",bdate)
            otherdetail.putExtra("bplace",bplace)
            otherdetail.putExtra("mobo",mobo)
            otherdetail.putExtra("house",house)
            otherdetail.putExtra("purok",purok)
            startActivity(otherdetail)
        }

    }

    private fun getUserDetails() {
        val url = "http://www.barangaysanroqueantipolo.site/API/profiledisplayApi.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val purok = jsonObject.getString("sitio")
                    val brgy = jsonObject.getString("brgy")
                    val mun = jsonObject.getString("mun")
                    val prov = jsonObject.getString("prov")
                    val zip = jsonObject.getString("zip")

                    purokTxt = findViewById(R.id.purokTxt)
                    purokTxt?.setText(purok)
                    brgyTxt = findViewById(R.id.brgyTxt)
                    brgyTxt?.setText(brgy)
                    munTxt = findViewById(R.id.munTxt)
                    munTxt?.setText(mun)
                    provTxt = findViewById(R.id.provTxt)
                    provTxt?.setText(prov)
                    zipcTxt = findViewById(R.id.zipcTxt)
                    zipcTxt?.setText(zip)


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