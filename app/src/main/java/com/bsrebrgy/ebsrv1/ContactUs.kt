package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ContactUs : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var subAdapter: ArrayAdapter<String>? = null
    var spinSub: Spinner? = null
    var subStatus = ""
    var sndBtn: Button? = null
    var fullTxt: EditText?= null
    var eaddTxt: EditText?=null
    var msgTxt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        spinSub = findViewById(R.id.spinSub)

        val subStat = arrayOf("Application Feedback","Inquiries","Concerns","Application Bug/Error")
        subAdapter = ArrayAdapter<String>(this@ContactUs,android.R.layout.simple_spinner_dropdown_item,subStat)
        spinSub?.setAdapter(subAdapter)

        spinSub?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                subStatus = subStat[position]
            }
        }

        msgTxt = findViewById(R.id.msgTxt)
        sndBtn = findViewById(R.id.sndBtn)
        sndBtn?.setOnClickListener {
            val alertdialog : AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Are You Sure")
            alertdialog.setMessage("Do you want to Send Message")

            alertdialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes") {
                    dialog, which -> uploadtoserver()
                dialog.dismiss()}

            alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No") {
                    dialog, which ->
                dialog.dismiss()}
            alertdialog.show()
        }
    }

    private fun uploadtoserver() {
        val url = "http://www.barangaysanroqueantipolo.site/API/messageApi.php"
        val user: String = user!!.toString().trim { it <= ' ' }
        val full = fullTxt?.text.toString().trim { it <= ' ' }
        val eadd = eaddTxt?.text.toString().trim { it <= ' ' }
        val msg = msgTxt?.text.toString().trim { it <= ' ' }
        val sub = subStatus
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if(success == "0")
                    {
                        Toast.makeText(applicationContext, "Message Submitted Successfully", Toast.LENGTH_SHORT).show()
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
                    map.put("user", user)
                    map.put("full", full)
                    map.put("eadd", eadd)
                    map.put("sub", sub)
                    map.put("msg", msg)
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


    private fun getUserDetails() {
        val url = "http://www.barangaysanroqueantipolo.site/API/profiledisplayApi.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val fname = jsonObject.getString("fname")
                    val mname = jsonObject.getString("mname")
                    val lname = jsonObject.getString("lname")
                    val email = jsonObject.getString("email")

                    fullTxt = findViewById(R.id.fullTxt)
                    fullTxt?.setText(fname+" "+mname+" "+lname)

                    eaddTxt = findViewById(R.id.eaddTxt)
                    eaddTxt?.setText(email)


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