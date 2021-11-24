package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
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
import java.io.ByteArrayOutputStream
import java.util.*

class UpdateUploadIDFileUpload : AppCompatActivity() {
    lateinit var session: SessionManager
    var user: String? = null
    var vidView: ImageView? = null
    var viduploadidBtn: Button? = null
    var vidsubBtn: Button? = null
    var bitmap: Bitmap? = null
    var encodedimage: String? = null
    var spinValid: Spinner? = null
    var validAdapter: ArrayAdapter<String>? = null
    var validStatus = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_upload_idfile_upload)
        session = SessionManager(this@UpdateUploadIDFileUpload)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)



        spinValid = findViewById(R.id.spinValid)
        val vidStat = arrayOf("Driver`s License","Voter`s ID","Company ID","Student ID")
        validAdapter = ArrayAdapter<String>(this@UpdateUploadIDFileUpload,android.R.layout.simple_spinner_dropdown_item,vidStat)
        spinValid?.adapter = validAdapter

        spinValid?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                validStatus = vidStat[position]
            }
        }

        vidView = findViewById(R.id.vidView)
        viduploadidBtn = findViewById(R.id.viduploadidBtn)
        viduploadidBtn?.setOnClickListener {
            launchGallery()
        }

        vidsubBtn = findViewById(R.id.vidsubBtn)
        vidsubBtn?.setOnClickListener {
            val alertdialog : AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Are You Sure")
            alertdialog.setMessage("Do you want to Submit")

            alertdialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes") {
                    dialog, which ->
                if(vidView?.drawable == null)
                {
                    Toast.makeText(applicationContext, "No File", Toast.LENGTH_LONG).show()
                }
                else
                {
                    uploadtoserver()
                }
                    dialog.dismiss()}

            alertdialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No") {
                    dialog, which ->
                dialog.dismiss()}
            alertdialog.show()
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val uri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            vidView?.setImageBitmap(bitmap)
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
        val url = "http://www.barangaysanroqueantipolo.site/API/updateUploadIDApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val encodedimage = encodedimage.toString().trim { it <= ' ' }
        val vid = validStatus
        val request: StringRequest =
            object : StringRequest(
                Method.POST, url, Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")
                        val message = jsonObject.getString("message")

                        if (success == "0")
                        {
                            Toast.makeText(applicationContext, "Valid ID Updated", Toast.LENGTH_SHORT).show()
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
                    map.put("valid", encodedimage)
                    map.put("vid", vid)
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