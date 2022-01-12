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

class UpdateHomeAddress : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var spinPur: Spinner? = null
    var purAdapter: ArrayAdapter<String>? = null
    var purok = ""
    var purokList: ArrayList<String> = ArrayList()
    var requestQueue: RequestQueue? = null
    var pTxt: EditText? = null
    var hTxt: EditText? = null
    var haddBtn: Button? = null
    var origpTxt = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_home_address)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        spinPur = findViewById(R.id.spinPur)
        val url = "http://www.barangaysanroqueantipolo.site/API/sitioSpinnerApi.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("sitios")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val sitioName = jsonObject.optString("sitio")
                    purokList.add(sitioName)
                    purAdapter = ArrayAdapter<String>(
                        this@UpdateHomeAddress,
                        android.R.layout.simple_spinner_dropdown_item, purokList
                    )
                    spinPur?.setAdapter(purAdapter)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)

        spinPur?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+sitioList[position],Toast.LENGTH_SHORT).show()
                purok = purokList[position]
                if(purok == "Choose Purok")
                {
                    pTxt?.setText(origpTxt)
                }
                else {
                    pTxt?.setText(purok)
                }

            }
        }

        haddBtn = findViewById(R.id.haddBtn)
        haddBtn?.setOnClickListener {
            val alertdialog: AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Are You Sure")
            alertdialog.setMessage("Do you want to Submit")

            alertdialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { dialog, which ->
                uploadtoserver()
                dialog.dismiss()
            }

            alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, which ->
                dialog.dismiss()
            }
            alertdialog.show()
        }
    }

    private fun getUserDetails() {
            val url = "http://www.barangaysanroqueantipolo.site/API/updateHomedisplayApi.php"
            val username = user.toString().trim { it <= ' ' }
            val request: StringRequest =
                object : StringRequest(Method.POST, url, Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val house = jsonObject.getString("house")
                        val pur = jsonObject.getString("sitio")
                        val brgy = jsonObject.getString("brgy")
                        val mun = jsonObject.getString("mun")
                        val prov = jsonObject.getString("prov")
                        val zip = jsonObject.getString("zip")

                        hTxt = findViewById(R.id.hTxt)
                        hTxt?.setText(house)
                        pTxt = findViewById(R.id.pTxt)
                        pTxt?.setText(pur)
                        origpTxt = pTxt?.text.toString()
                        val bTxt = findViewById<EditText>(R.id.bTxt)
                        bTxt.setText(brgy)
                        val muTxt = findViewById<EditText>(R.id.muTxt)
                        muTxt.setText(mun)
                        val proTxt = findViewById<EditText>(R.id.proTxt)
                        proTxt.setText(prov)
                        val zTxt = findViewById<EditText>(R.id.zTxt)
                        zTxt.setText(zip)

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

    private fun uploadtoserver() {
        val url = "http://www.barangaysanroqueantipolo.site/API/updateHomeaddApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val house: String = hTxt!!.text.toString().trim { it <= ' ' }
        val purok: String = pTxt!!.text.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if (success == "0")
                    {
                        Toast.makeText(applicationContext, "Home Address Updated!", Toast.LENGTH_LONG).show()
                        val dashboard = Intent(this, DashboardUser::class.java)
                        startActivity(dashboard)
                    }
                    else
                    {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Update Error! $e", Toast.LENGTH_LONG).show()
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
                    map.put("house", house)
                    map.put("purok", purok)
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
