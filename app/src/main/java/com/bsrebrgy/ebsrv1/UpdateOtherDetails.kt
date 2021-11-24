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

class UpdateOtherDetails : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var spinCivil: Spinner? = null
    var spinEmp: Spinner? = null
    var empAdapter: ArrayAdapter<String>? = null
    var civilAdapter: ArrayAdapter<String>? = null
    var civilStatus = ""
    var empStatus = ""
    var ocTxt : EditText? = null
    var monTxt : EditText?= null
    var rTxt : EditText? = null
    var nTxt : EditText? = null
    var cTxt : EditText? = null
    var empTxt : EditText?= null
    var oBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_other_details)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        spinCivil = findViewById(R.id.spinCivil)

        val civilStat = arrayOf("Change Civil Status","Single","Married","Separated","Widowed")
        civilAdapter = ArrayAdapter<String>(this@UpdateOtherDetails,android.R.layout.simple_spinner_dropdown_item,civilStat)
        spinCivil?.setAdapter(civilAdapter)

        spinCivil?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+civilStat[position],Toast.LENGTH_SHORT).show()
                civilStatus = civilStat[position]
                cTxt?.setText(civilStatus)

            }
        }

        spinEmp = findViewById(R.id.spinEmp)
        val empStat = arrayOf("Change Employment Status","Student","Employed","Self-Employed","Unemployed")
        empAdapter = ArrayAdapter<String>(this@UpdateOtherDetails,android.R.layout.simple_spinner_dropdown_item,empStat)
        spinEmp?.setAdapter(empAdapter)

        ocTxt = findViewById(R.id.ocTxt)
        monTxt = findViewById(R.id.monTxt)

        spinEmp?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+empStat[position],Toast.LENGTH_SHORT).show()
                empStatus = empStat[position]
                empTxt?.setText(empStatus)

                if (empStatus == "Student" || empStatus == "Unemployed") {
                    ocTxt?.isEnabled = false
                    ocTxt?.setText("None")
                    monTxt?.isEnabled = false
                    monTxt?.setText("0")
                }
                else
                {
                    ocTxt?.isEnabled = true
                    ocTxt?.setText("")
                    monTxt?.isEnabled = true
                    monTxt?.setText("")
                }
            }
        }

        oBtn = findViewById(R.id.oBtn)
        oBtn?.setOnClickListener {
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
        val url = "http://www.barangaysanroqueantipolo.site/API/updateOtherdetailsApi.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val civil = jsonObject.getString("civil")
                    val emp = jsonObject.getString("emp")
                    val occu = jsonObject.getString("occu")
                    val income = jsonObject.getString("income")
                    val reli = jsonObject.getString("reli")
                    val nation = jsonObject.getString("nation")

                    cTxt = findViewById(R.id.cTxt)
                    cTxt?.setText(civil)
                    empTxt = findViewById(R.id.empTxt)
                    empTxt?.setText(emp)
                    ocTxt?.setText(occu)
                    monTxt?.setText(income)
                    rTxt = findViewById(R.id.rTxt)
                    rTxt?.setText(reli)
                    nTxt = findViewById(R.id.nTxt)
                    nTxt?.setText(nation)


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
        val url = "http://www.barangaysanroqueantipolo.site/API/updateOdetailsApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val civil: String = cTxt!!.text.toString().trim { it <= ' ' }
        val emp: String = empTxt!!.text.toString().trim { it <= ' ' }
        val occu: String = ocTxt!!.text.toString().trim { it <= ' ' }
        val mon: String = monTxt!!.text.toString().trim { it <= ' ' }
        val rel: String = rTxt!!.text.toString().trim { it <= ' ' }
        val nation: String = nTxt!!.text.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if (success == "0")
                    {
                        Toast.makeText(applicationContext, "Other Details Updated!", Toast.LENGTH_LONG).show()
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
                    map.put("civil", civil)
                    map.put("emp", emp)
                    map.put("occu", occu)
                    map.put("mon", mon)
                    map.put("rel", rel)
                    map.put("nation", nation)
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