package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    var cbTerm: CheckBox? = null
    var termTxt: TextView? = null

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
                docus = docusid[position]
            }
        }
        purposeEtxt = findViewById(R.id.purposeEtxt)

        termTxt = findViewById(R.id.termTxt)
        termTxt?.setOnClickListener {
            val alertdialog : AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Terms and Conditions & Privacy Policy")
            alertdialog.setMessage("Terms & Conditions\n" +
                    "\n" +
                    "By downloading or using the app, these terms will automatically apply to you - you should make sure therefore that you read them carefully before using the app. You're not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You're not allowed to attempt to extract the source code of the app, and you also shouldn't try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you're paying for. \n" +
                    "\n" +
                    "The EBSRAC App stores and processes personal data that you have provided to us, in order to provide our Service. It's your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/ malicious programs, compromise your phone's security features and it could mean that the EBSRAC App won't work properly or at all. The app does use third party services that declare their own Terms and Conditions. Link to Terms and Conditions of third party service providers used by the app. \n" +
                    "\n" +
                    "\n" +
                    "Privacy Policy\n" +
                    "\n" +
                    "Log Data\n" +
                    "\n" +
                    "We want to inform you that whenever you use our Service, in a case of an error in the app we collect data and information (through third party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (\"IP\") address, device name, operating system version, the configuration of the app when utilizing our Service, the time and date of your use of the Service, and other statistics.\n" +
                    "\n" +
                    "Cookies\n" +
                    "\n" +
                    "Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory. This Service does not use these “cookies” to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.\n")

            alertdialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes") {
                    dialog, which -> cbTerm?.isChecked = true
                dialog.dismiss()}

            alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No") {
                    dialog, which -> cbTerm?.isChecked = false
                dialog.dismiss()}
            alertdialog.show()
        }

        cbTerm = findViewById(R.id.cbTerm)
        submitBtn = findViewById(R.id.submitBtn)
        submitBtn?.setOnClickListener {
            if (cbTerm!!.isChecked)
            {
                uploadtoserver()
            }
            else
            {
                    Toast.makeText(
                        applicationContext,
                        "Please Read Terms and Conditions & Privacy Policy",
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
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
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if(success == "0")
                    {
                        Toast.makeText(applicationContext, "Request Submitted Successfully", Toast.LENGTH_SHORT).show()
                        val dashboardIntent = Intent(this, DashboardUser::class.java)
                        startActivity(dashboardIntent)
                    }
                    else
                    {
                        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Register Error! $e", Toast.LENGTH_SHORT).show()
                }

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