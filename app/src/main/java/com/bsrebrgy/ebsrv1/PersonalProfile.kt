package com.bsrebrgy.ebsrv1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class PersonalProfile : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    private var y: Int = 0
    private var m: Int = 0
    private var d: Int = 0
    var firstTxt : EditText? = null
    var middleTxt : EditText? = null
    var lastTxt : EditText? = null
    var extTxt : EditText? = null
    var sex = ""
    var maidenTxt : EditText? = null
    var bdateTxt : EditText? = null
    var ageTxt : EditText? = null
    var bplaceTxt : EditText? = null
    var moboTxt : EditText? = null
    var personBtn : Button? = null
    var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_profile)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        bdateTxt = findViewById(R.id.bdateTxt)
        val bdateBtn = findViewById<Button>(R.id.bdateBtn)
        bdateBtn.setOnClickListener {
            val c = Calendar.getInstance()
            d = c.get(Calendar.DATE)
            m = c.get(Calendar.MONTH)
            y = c.get(Calendar.YEAR)

            val datePicker = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    val mon = month+1
                    bdateTxt?.setText(year.toString() + "-" + mon.toString() + "-" + dayOfMonth.toString())
                }, y, m, d)
            datePicker.show()
        }

        val radioSexGroup = findViewById<RadioGroup>(R.id.radioSexGrp)
        radioSexGroup.setOnCheckedChangeListener { group, ID ->
            when (ID)
            {
                R.id.radioMale -> {
                    sex = "Male"
                }
                R.id.radioFemale -> {
                    sex = "Female"
                }
            }
        }

        personBtn = findViewById(R.id.personBtn)
        personBtn?.setOnClickListener {

            firstTxt = findViewById(R.id.firstTxt)
            val fname = firstTxt?.text.toString()
            middleTxt = findViewById(R.id.middleTxt)
            val mname = middleTxt?.text.toString()
            lastTxt = findViewById(R.id.lastTxt)
            val lname = lastTxt?.text.toString()
            val sexname = sex
            extTxt = findViewById(R.id.extTxt)
            val extname = extTxt?.text.toString()
            maidenTxt = findViewById(R.id.maidenTxt)
            val maidenname = maidenTxt?.text.toString()
            ageTxt = findViewById(R.id.ageTxt)
            val age = ageTxt?.text.toString()
            val bdate = bdateTxt?.text.toString()
            bplaceTxt = findViewById(R.id.bplaceTxt)
            val bplace = bplaceTxt?.text.toString()
            moboTxt = findViewById(R.id.moboTxt)
            val mobo = moboTxt?.text.toString()

            val homeaddress = Intent(this, HomeAddress::class.java)
            homeaddress.putExtra("fname",fname)
            homeaddress.putExtra("mname",mname)
            homeaddress.putExtra("lname",lname)
            homeaddress.putExtra("sexname",sexname)
            homeaddress.putExtra("extname",extname)
            homeaddress.putExtra("maidenname",maidenname)
            homeaddress.putExtra("age",age)
            homeaddress.putExtra("bdate",bdate)
            homeaddress.putExtra("bplace",bplace)
            homeaddress.putExtra("mobo",mobo)
            startActivity(homeaddress)
        }
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

                    firstTxt = findViewById(R.id.firstTxt)
                    firstTxt?.setText(fname)
                    middleTxt = findViewById(R.id.middleTxt)
                    middleTxt?.setText(mname)
                    lastTxt = findViewById(R.id.lastTxt)
                    lastTxt?.setText(lname)
                    firstTxt = findViewById(R.id.firstTxt)
                    firstTxt?.setText(fname)


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