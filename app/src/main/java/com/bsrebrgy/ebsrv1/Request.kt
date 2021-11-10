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
import kotlin.collections.ArrayList

class Request : AppCompatActivity() {
    lateinit var session: SessionManager
    var user: String? = null
    var spinnerDocus: Spinner? = null
    var docusList: ArrayList<String> = ArrayList()
    var docusid: ArrayList<String> = ArrayList()
    var requestQueue: RequestQueue? = null
    var docusAdapter: ArrayAdapter<String>? = null
    var docus = ""
    var submitBtn : Button? = null
    var purposeEtxt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        spinnerDocus = findViewById(R.id.spinnerDocus)

        val url = "http://www.barangaysanroqueantipolo.site/API/documentApi.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("document")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val docusName = jsonObject.optString("docus")
                    val docusId = jsonObject.optString("did")
                    docusList.add(docusName)
                    docusid.add(docusId)
                    docusAdapter = ArrayAdapter<String>(
                        this@Request,
                        android.R.layout.simple_spinner_dropdown_item, docusList
                    )
                    spinnerDocus?.setAdapter(docusAdapter)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)

        spinnerDocus?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Request, "You Have Selected"+" "+docusid[position],Toast.LENGTH_SHORT).show()
                docus = docusid[position]
            }
        }
        purposeEtxt = findViewById(R.id.purposeEtxt)

        submitBtn = findViewById(R.id.submitBtn)
        submitBtn?.setOnClickListener { uploadtoserver() }
    }

    private fun getUserDetails() {
        val url = "http://www.barangaysanroqueantipolo.site/API/profilereqApi.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val fname = jsonObject.getString("fname")
                    val mname = jsonObject.getString("mname")
                    val lname = jsonObject.getString("lname")
                    val sitio = jsonObject.getString("sitio")
                    val add = jsonObject.getString("address")
                    val brgy = jsonObject.getString("brgy")
                    val mun = jsonObject.getString("mun")
                    val prov = jsonObject.getString("prov")
                    val zip = jsonObject.getString("zip")

                    val fnameTview = findViewById<TextView>(R.id.fnameTview)
                    fnameTview.setText(fname)
                    val mnameTview = findViewById<TextView>(R.id.mnameTview)
                    mnameTview.setText(mname)
                    val lnameTview = findViewById<TextView>(R.id.lnameTview)
                    lnameTview.setText(lname)
                    val sitioTview = findViewById<TextView>(R.id.sitioTview)
                    sitioTview.setText(sitio)
                    val addTview = findViewById<TextView>(R.id.addTview)
                    addTview.setText(add)
                    val brgyTview = findViewById<TextView>(R.id.brgyTview)
                    brgyTview.setText(brgy)
                    val munTview = findViewById<TextView>(R.id.munTview)
                    munTview.setText(mun)
                    val provTview = findViewById<TextView>(R.id.provTview)
                    provTview.setText(prov)
                    val zipTview = findViewById<TextView>(R.id.zipTview)
                    zipTview.setText(zip)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Error! $e", Toast.LENGTH_SHORT).show()
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
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)

    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    private fun uploadtoserver() {
        val url = "http://www.barangaysanroqueantipolo.site/API/requestdocApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val purpose = purposeEtxt?.text.toString().trim { it <= ' ' }
        val docus: String = docus!!.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                Toast.makeText(applicationContext, "Request Submitted Successfully", Toast.LENGTH_SHORT).show()

                val dashboardIntent = Intent(this, DashboardUser::class.java)
                startActivity(dashboardIntent)
            },
                Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val map: MutableMap<String, String> =
                        HashMap<String, String>()
                    map.put("user", username)
                    map.put("purpose", purpose)
                    map.put("docus", docus)
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