package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.*

class InfoDisplay : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var fname: TextView? = null
    var mname: TextView? = null
    var lname: TextView? = null
    var extname: TextView? = null
    var sexname: TextView? = null
    var maidenname: TextView? = null
    var bdate: TextView? = null
    var age: TextView? = null
    var bplace: TextView? = null
    var mobo: TextView? = null
    var house: TextView? = null
    var purok: TextView? = null
    var civil: TextView? = null
    var emp: TextView? = null
    var occu: TextView? = null
    var mon: TextView? = null
    var rel: TextView? = null
    var nation: TextView? = null
    var infoBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_display)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        val bundle: Bundle? = intent.extras
        val fname1 = bundle?.getString("fname")
        val mname1 = bundle?.getString("mname")
        val lname1 = bundle?.getString("lname")
        val sexname1 = bundle?.getString("sexname")
        val extname1 = bundle?.getString("extname")
        val maidenname1 = bundle?.getString("maidenname")
        val age1 = bundle?.getString("age")
        val bdate1 = bundle?.getString("bdate")
        val bplace1 = bundle?.getString("bplace")
        val mobo1 = bundle?.getString("mobo")
        val house1 = bundle?.getString("house")
        val purok1 = bundle?.getString("purok")
        val civil1 = bundle?.getString("civil")
        val emp1 = bundle?.getString("emp")
        val occu1 = bundle?.getString("occu")
        val mon1 = bundle?.getString("mon")
        val rel1 = bundle?.getString("rel")
        val nation1 = bundle?.getString("nation")

        fname = findViewById(R.id.fname)
        fname?.text = fname1
        mname = findViewById(R.id.mname)
        mname?.text = mname1
        lname = findViewById(R.id.lname)
        lname?.text = lname1
        sexname = findViewById(R.id.sexname)
        sexname?.text = sexname1
        extname = findViewById(R.id.extname)
        extname?.text = extname1
        maidenname = findViewById(R.id.maidenname)
        maidenname?.text = maidenname1
        age = findViewById(R.id.age)
        age?.text = age1
        bdate = findViewById(R.id.bdate)
        bdate?.text = bdate1
        bplace = findViewById(R.id.bplace)
        bplace?.text = bplace1
        mobo = findViewById(R.id.mobo)
        mobo?.text = mobo1
        house = findViewById(R.id.house)
        house?.text = house1
        purok = findViewById(R.id.purok)
        purok?.text = purok1
        civil = findViewById(R.id.civil)
        civil?.text = civil1
        emp = findViewById(R.id.emp)
        emp?.text = emp1
        occu = findViewById(R.id.occu)
        occu?.text = occu1
        mon = findViewById(R.id.mon)
        mon?.text = mon1
        rel = findViewById(R.id.rel)
        rel?.text = rel1
        nation = findViewById(R.id.nation)
        nation?.text = nation1

        infoBtn = findViewById(R.id.infoBtn)
        infoBtn?.setOnClickListener {
            val alertdialog : AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Are You Sure")
            alertdialog.setMessage("Is your Details Correct?\nDo you want to Submit")

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
        val url = "http://www.barangaysanroqueantipolo.site/API/infoinsertApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val fname: String = fname!!.text.toString().trim { it <= ' ' }
        val mname: String = mname!!.text.toString().trim { it <= ' ' }
        val lname: String = lname!!.text.toString().trim { it <= ' ' }
        val ext: String = extname!!.text.toString().trim { it <= ' ' }
        val sex: String = sexname!!.text.toString().trim { it <= ' ' }
        val maiden: String = maidenname!!.text.toString().trim { it <= ' ' }
        val bdate: String = bdate!!.text.toString().trim { it <= ' ' }
        val age: String = age!!.text.toString().trim { it <= ' ' }
        val bplace: String = bplace!!.text.toString().trim { it <= ' ' }
        val mobo: String = mobo!!.text.toString().trim { it <= ' ' }
        val house: String = house!!.text.toString().trim { it <= ' ' }
        val purok: String = purok!!.text.toString().trim { it <= ' ' }
        val civil: String = civil!!.text.toString().trim { it <= ' ' }
        val emp: String = emp!!.text.toString().trim { it <= ' ' }
        val occu: String = occu!!.text.toString().trim { it <= ' ' }
        val mon: String = mon!!.text.toString().trim { it <= ' ' }
        val rel: String = rel!!.text.toString().trim { it <= ' ' }
        val nation: String = nation!!.text.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(
                Method.POST, url, Response.Listener { response ->
                Toast.makeText(applicationContext, "Personal Information Updated!", Toast.LENGTH_LONG).show()
                    val signatureIntent = Intent(this, Signature::class.java)
                    startActivity(signatureIntent)
//                session.logoutUser()
            },
                Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val map: MutableMap<String, String> =
                        HashMap<String, String>()
                    map.put("user", username)
                    map.put("fname", fname)
                    map.put("mname", mname)
                    map.put("lname", lname)
                    map.put("ename", ext)
                    map.put("sex", sex)
                    map.put("maiden", maiden)
                    map.put("bdate", bdate)
                    map.put("age", age)
                    map.put("bplace", bplace)
                    map.put("mobo", mobo)
                    map.put("house", house)
                    map.put("purok", purok)
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
}