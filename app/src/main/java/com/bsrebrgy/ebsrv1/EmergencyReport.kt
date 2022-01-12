package com.bsrebrgy.ebsrv1

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.util.*

class EmergencyReport : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    lateinit var session : SessionManager
    var user : String? = null
    var bitmap: Bitmap? = null
    var encodedimage: String? = null
    var emergcamBtn: Button? = null
    var emergCam: ImageView? = null
    var emergsubBtn : Button? = null
    var locTxt : EditText? = null
    var descTxt : EditText? = null
    var dateButton : Button? = null
    var dateTime : EditText? = null
    var day = 0
    var month : Int = 0
    var year : Int = 0
    var hour : Int = 0
    var minute : Int = 0
    var myDay = 0
    var myMonth : Int = 0
    var myYear : Int = 0
    var myHour : Int = 0
    var myMinute : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_report)
        session = SessionManager(this@EmergencyReport)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        emergcamBtn = findViewById(R.id.emergcamBtn)
        emergCam = findViewById(R.id.emergCam)

        emergcamBtn?.setOnClickListener {
            Dexter.withContext(applicationContext)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, 111)
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        }


        locTxt = findViewById(R.id.locTxt)

        dateTime = findViewById(R.id.dateTime)
        dateButton = findViewById<Button>(R.id.dateButton)
        dateButton?.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@EmergencyReport, this@EmergencyReport, year, month,day)
            datePickerDialog.show()
        }

        descTxt = findViewById(R.id.descTxt)
        emergsubBtn = findViewById(R.id.emergsubBtn)
        emergsubBtn?.setOnClickListener { uploadtoserver() }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month+1
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@EmergencyReport, this@EmergencyReport, hour, minute,
            DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
//        val am_pm = if (myHour < 12) "AM" else "PM"
        myMinute = minute
        dateTime?.setText(myYear.toString()+"-"+myMonth.toString()+"-"+myDay.toString()+" "+myHour.toString()+":"+myMinute.toString())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            bitmap = data!!.extras!!["data"] as Bitmap?
            emergCam?.setImageBitmap(bitmap)
            encodebitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun encodebitmap(bitmap: Bitmap?) {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        encodedimage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun uploadtoserver() {
        val url = "http://www.barangaysanroqueantipolo.site/API/emergencyApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val encodedimage = encodedimage.toString().trim { it <= ' ' }
        val loc = locTxt?.text.toString().trim { it <= ' ' }
        val desc = descTxt?.text.toString().trim { it <= ' ' }
        val date = dateTime?.text.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(
                Method.POST, url, Response.Listener { response ->
                    Toast.makeText(applicationContext, "Emergency Report Submitted", Toast.LENGTH_SHORT).show()

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
                    map.put("upload", encodedimage)
                    map.put("loc", loc)
                    map.put("desc", desc)
                    map.put("date", date)
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