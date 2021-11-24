package com.bsrebrgy.ebsrv1

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

class UpdatePersonalProfile : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var bdTxt: EditText? = null
    var aTxt: EditText? = null
    var pBtn: Button? = null
    var fTxt: EditText? = null
    var mTxt: EditText? = null
    var lTxt: EditText? = null
    var eTxt: EditText? = null
    var sTxt: EditText? = null
    var maiTxt: EditText? = null
    var bpTxt: EditText? = null
    var moTxt: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_personal_profile)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        aTxt = findViewById(R.id.aTxt)
        bdTxt = findViewById(R.id.bdTxt)
        val bdBtn = findViewById<Button>(R.id.bdBtn)
        bdBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val c = Calendar.getInstance()
                val mYear = c[Calendar.YEAR]
                val mMonth = c[Calendar.MONTH]
                val mDay = c[Calendar.DAY_OF_MONTH]
                val dateDialog =
                    DatePickerDialog(view.getContext(), datePickerListener, mYear, mMonth, mDay)
                dateDialog.datePicker.maxDate = Date().time
                dateDialog.show()
            }
        })

        pBtn = findViewById(R.id.pBtn)
        pBtn?.setOnClickListener {
            val alertdialog : AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Are You Sure")
            alertdialog.setMessage("Do you want to Submit")

            alertdialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes") {
                    dialog, which -> uploadtoserver()
                dialog.dismiss()}

            alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No") {
                    dialog, which ->
                dialog.dismiss()}
            alertdialog.show()
        }

    }

    private val datePickerListener =
        OnDateSetListener { datePicker, year, month, day ->
            val c = Calendar.getInstance()
            c[Calendar.YEAR] = year
            c[Calendar.MONTH] = month
            c[Calendar.DAY_OF_MONTH] = day
            val mon = month+1
            bdTxt?.setText(year.toString() + "-" + mon.toString() + "-" + day.toString())
            aTxt?.setText(Integer.toString(calculateAge(c.timeInMillis)))
        }

    fun calculateAge(date: Long): Int {
        val dob = Calendar.getInstance()
        dob.timeInMillis = date
        val today = Calendar.getInstance()
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_MONTH] < dob[Calendar.DAY_OF_MONTH]) {
            age--
        }
        return age
    }

    private fun getUserDetails() {
        val url = "http://www.barangaysanroqueantipolo.site/API/updateProfiledisplayApi.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val ftxt = jsonObject.getString("ftxt")
                    val mtxt = jsonObject.getString("mtxt")
                    val ltxt = jsonObject.getString("ltxt")
                    val etxt = jsonObject.getString("etxt")
                    val stxt = jsonObject.getString("stxt")
                    val maitxt = jsonObject.getString("maitxt")
                    val atxt = jsonObject.getString("atxt")
                    val bdtxt = jsonObject.getString("bdtxt")
                    val bptxt = jsonObject.getString("bptxt")
                    val motxt = jsonObject.getString("motxt")

                    fTxt = findViewById(R.id.fTxt)
                    fTxt?.setText(ftxt)

                    mTxt = findViewById(R.id.mTxt)
                    mTxt?.setText(mtxt)

                    lTxt = findViewById(R.id.lTxt)
                    lTxt?.setText(ltxt)

                    eTxt = findViewById(R.id.eTxt)
                    eTxt?.setText(etxt)

                    sTxt = findViewById(R.id.sTxt)
                    sTxt?.setText(stxt)

                    maiTxt = findViewById(R.id.maiTxt)
                    maiTxt?.setText(maitxt)
                    
                    aTxt?.setText(atxt)

                    bdTxt?.setText(bdtxt)

                    bpTxt = findViewById(R.id.bpTxt)
                    bpTxt?.setText(bptxt)

                    moTxt = findViewById(R.id.moTxt)
                    moTxt?.setText(motxt)

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
        val url = "http://www.barangaysanroqueantipolo.site/API/updatePersonalApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val fname: String = fTxt!!.text.toString().trim { it <= ' ' }
        val mname: String = mTxt!!.text.toString().trim { it <= ' ' }
        val lname: String = lTxt!!.text.toString().trim { it <= ' ' }
        val ename: String = eTxt!!.text.toString().trim { it <= ' ' }
        val sex: String = sTxt!!.text.toString().trim { it <= ' ' }
        val maiden: String = maiTxt!!.text.toString().trim { it <= ' ' }
        val bdate: String = bdTxt!!.text.toString().trim { it <= ' ' }
        val age: String = aTxt!!.text.toString().trim { it <= ' ' }
        val bplace: String = bpTxt!!.text.toString().trim { it <= ' ' }
        val mobo: String = moTxt!!.text.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")

                    if (success == "0")
                    {
                        Toast.makeText(applicationContext, "Personal Profile Updated!", Toast.LENGTH_LONG).show()
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
                    map.put("fname", fname)
                    map.put("mname", mname)
                    map.put("lname", lname)
                    map.put("ename", ename)
                    map.put("sex", sex)
                    map.put("maiden", maiden)
                    map.put("bdate", bdate)
                    map.put("age", age)
                    map.put("bplace", bplace)
                    map.put("mobo", mobo)
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
