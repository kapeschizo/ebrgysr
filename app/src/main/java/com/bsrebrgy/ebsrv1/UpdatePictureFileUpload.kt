package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
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
import java.io.ByteArrayOutputStream
import java.util.*

class UpdatePictureFileUpload : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var bitmap: Bitmap? = null
    var encodedimage: String? = null
    var picView: ImageView? = null
    var picupBtn : Button? = null
    var picsubBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_picture_file_upload)
        session = SessionManager(this@UpdatePictureFileUpload)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        picView = findViewById(R.id.picView)

        picupBtn = findViewById(R.id.picupBtn)
        picupBtn?.setOnClickListener {
            launchGallery()
        }

        picsubBtn = findViewById(R.id.picsubBtn)
        picsubBtn?.setOnClickListener {
            val alertdialog : AlertDialog = AlertDialog.Builder(this).create()
            alertdialog.setTitle("Are You Sure")
            alertdialog.setMessage("Do you want to Submit")

            alertdialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes") {
                    dialog, which ->
                if(picView?.drawable == null)
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
            picView?.setImageBitmap(bitmap)
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
        val url = "http://www.barangaysanroqueantipolo.site/API/updatepicApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val encodedimage = encodedimage.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(
                Method.POST, url, Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")
                        val message = jsonObject.getString("message")

                        if (success == "0")
                        {
                            Toast.makeText(applicationContext, "Profile Picture Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            val dashboardIntent = Intent(this, DashboardUser::class.java)
                            startActivity(dashboardIntent)
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
                    map.put("upload", encodedimage)
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