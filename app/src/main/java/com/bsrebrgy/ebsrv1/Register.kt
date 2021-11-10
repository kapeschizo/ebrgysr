package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Register : AppCompatActivity() {
    var fnameEtext: EditText? = null
    var mnameEtext: EditText? = null
    var lnameEtext: EditText? = null
    var emailRegEtxt: EditText? = null
    var signUpBtn: Button? = null
    var loading: ProgressBar? = null
    var spinnerSitio: Spinner? = null
    var sitioList: ArrayList<String> = ArrayList()
    var sitioAdapter: ArrayAdapter<String>? = null
    var sitio = ""
    var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fnameEtext = findViewById(R.id.fnameEtext)
        mnameEtext = findViewById(R.id.mnameEtext)
        lnameEtext = findViewById(R.id.lnameEtext)
        emailRegEtxt = findViewById(R.id.emailSignEtxt)
        signUpBtn = findViewById(R.id.signUpBtn)
        loading = findViewById(R.id.loading)

        signUpBtn?.setOnClickListener {
            register()
        }

        requestQueue = Volley.newRequestQueue(this)
        spinnerSitio = findViewById(R.id.sitioSpin)

        val url = "http://www.barangaysanroqueantipolo.site/API/sitioSpinnerApi.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("sitios")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val sitioName = jsonObject.optString("sitio")
                    sitioList.add(sitioName)
                    sitioAdapter = ArrayAdapter<String>(
                        this@Register,
                        android.R.layout.simple_spinner_dropdown_item, sitioList
                    )
                    spinnerSitio?.setAdapter(sitioAdapter)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)

        spinnerSitio?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sitio = sitioList[position]
            }
        }
    }

    private fun register() {
        loading?.visibility = View.VISIBLE
        signUpBtn?.visibility = View.GONE
        val url = "http://www.barangaysanroqueantipolo.site/API/sendEmailApi.php"
        val fname = fnameEtext?.text.toString().trim { it <= ' ' }
        val mname = mnameEtext?.text.toString().trim { it <= ' ' }
        val lname = lnameEtext?.text.toString().trim { it <= ' ' }
        val sitio = sitio.trim { it <= ' ' }
        val email = emailRegEtxt?.text.toString().trim { it <= ' ' }

        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if(success == "4")
                    {
                        val mainIntent = Intent(this, MainActivity::class.java)
                        startActivity(mainIntent)

                        Toast.makeText(applicationContext,"Registered Successful! Please Check your Email for the Password",Toast.LENGTH_LONG).show()
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
                override fun getParams(): MutableMap<String, String> {
                    val map: MutableMap<String, String> =
                        HashMap<String, String>()
                    map.put("fname", fname)
                    map.put("mname", mname)
                    map.put("lname", lname)
                    map.put("sitio", sitio)
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