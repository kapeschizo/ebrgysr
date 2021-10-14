package com.bsrebrgy.ebsrv1

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
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

class ProfileImage : AppCompatActivity() {
    var fnameTv: TextView? = null
    var mnameTv: TextView? = null
    var lnameTv: TextView? = null
    var extnameTv: TextView? = null
    var sexTv: TextView? = null
    var maidenTv: TextView? = null
    var dateTv: TextView? = null
    var ageTv: TextView? = null
    var bplaceTv: TextView? = null
    var moboTv: TextView? = null
    var houseTv: TextView? = null
    var sitioTv: TextView? = null
    var brgyTv: TextView? = null
    var munTv: TextView? = null
    var provTv: TextView? = null
    var zipTv: TextView? = null
    var civilTv: TextView? = null
    var empTv: TextView? = null
    var occTv: TextView? = null
    var mincomeTv: TextView? = null
    var religionTv: TextView? = null
    var nationTv: TextView? = null
    var cameraBtn: Button? = null
    var cameraIview: ImageView? = null
    var bitmap: Bitmap? = null
    var encodedimage: String? = null
    lateinit var session : SessionManager
    var user : String? = null
    var nextProfBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_image)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        session = SessionManager(this@ProfileImage)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        val bundle: Bundle? = intent.extras
        val fname = bundle?.getString("fname")
        val mname = bundle?.getString("mname")
        val lname = bundle?.getString("lname")
        val extname = bundle?.getString("extname")
        val sex = bundle?.getString("sex")
        val maiden = bundle?.getString("maiden")
        val date = bundle?.getString("date")
        val age = bundle?.getString("age")
        val bplace = bundle?.getString("bplace")
        val mobo = bundle?.getString("mobo")
        val house = bundle?.getString("house")
        val sitio = bundle?.getString("sitio")
        val brgy = bundle?.getString("brgy")
        val mun = bundle?.getString("mun")
        val prov = bundle?.getString("prov")
        val zip = bundle?.getString("zip")
        val civil = bundle?.getString("civil")
        val emp = bundle?.getString("emp")
        val occ = bundle?.getString("occ")
        val mincome = bundle?.getString("mincome")
        val religion = bundle?.getString("religion")
        val nation = bundle?.getString("nation")

        fnameTv = findViewById(R.id.fnameTv)
        fnameTv?.text = fname
        mnameTv = findViewById(R.id.mnameTv)
        mnameTv?.text = mname
        lnameTv = findViewById(R.id.lnameTv)
        lnameTv?.text = lname
        extnameTv = findViewById(R.id.extnameTv)
        extnameTv?.text = extname
        sexTv = findViewById(R.id.sexTv)
        sexTv?.text = sex
        maidenTv = findViewById(R.id.maidenTv)
        maidenTv?.text = maiden
        dateTv = findViewById(R.id.dateTv)
        dateTv?.text = date
        ageTv = findViewById(R.id.ageTv)
        ageTv?.text = age
        bplaceTv = findViewById(R.id.bplaceTv)
        bplaceTv?.text = bplace
        moboTv = findViewById(R.id.moboTv)
        moboTv?.text = mobo
        houseTv = findViewById(R.id.houseTv)
        houseTv?.text = house
        sitioTv = findViewById(R.id.sitioTv)
        sitioTv?.text = sitio
        brgyTv = findViewById(R.id.brgyTv)
        brgyTv?.text = brgy
        munTv = findViewById(R.id.munTv)
        munTv?.text = mun
        provTv = findViewById(R.id.provTv)
        provTv?.text = prov
        zipTv = findViewById(R.id.zipTv)
        zipTv?.text = zip
        civilTv = findViewById(R.id.civilTv)
        civilTv?.text = civil
        empTv = findViewById(R.id.empTv)
        empTv?.text = emp
        occTv = findViewById(R.id.occTv)
        occTv?.text = occ
        mincomeTv = findViewById(R.id.mincomeTv)
        mincomeTv?.text = mincome
        religionTv = findViewById(R.id.religionTv)
        religionTv?.text = religion
        nationTv = findViewById(R.id.nationTv)
        nationTv?.text = nation

        cameraIview = findViewById(R.id.cameraIview)
        cameraBtn = findViewById(R.id.cameraBtn)

        cameraBtn?.setOnClickListener {
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
            nextProfBtn = findViewById(R.id.nextProfBtn)
        nextProfBtn?.setOnClickListener { uploadtoserver() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            bitmap = data!!.extras!!["data"] as Bitmap?
            cameraIview?.setImageBitmap(bitmap)
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
            val url = "http://www.barangaysanroqueantipolo.site/API/profileApi.php"
            val username: String = user!!.toString().trim { it <= ' ' }
            val fname: String = fnameTv!!.text.toString().trim { it <= ' ' }
            val mname: String = mnameTv!!.text.toString().trim { it <= ' ' }
            val lname: String = lnameTv!!.text.toString().trim { it <= ' ' }
            val ename: String = extnameTv!!.text.toString().trim { it <= ' ' }
            val sex: String = sexTv!!.text.toString().trim { it <= ' ' }
            val maiden: String = maidenTv!!.text.toString().trim { it <= ' ' }
            val date: String = dateTv!!.text.toString().trim { it <= ' ' }
            val age: String = ageTv!!.text.toString().trim { it <= ' ' }
            val bplace: String = bplaceTv!!.text.toString().trim { it <= ' ' }
            val mobo: String = moboTv!!.text.toString().trim { it <= ' ' }
            val house: String = houseTv!!.text.toString().trim { it <= ' ' }
            val sitio: String = sitioTv!!.text.toString().trim { it <= ' ' }
            val brgy: String = brgyTv!!.text.toString().trim { it <= ' ' }
            val mun: String = munTv!!.text.toString().trim { it <= ' ' }
            val prov: String = provTv!!.text.toString().trim { it <= ' ' }
            val zip: String = zipTv!!.text.toString().trim { it <= ' ' }
            val civil: String = civilTv!!.text.toString().trim { it <= ' ' }
            val emp: String = empTv!!.text.toString().trim { it <= ' ' }
            val occ: String = occTv!!.text.toString().trim { it <= ' ' }
            val mincome: String = mincomeTv!!.text.toString().trim { it <= ' ' }
            val rel: String = religionTv!!.text.toString().trim { it <= ' ' }
            val nation: String = nationTv!!.text.toString().trim { it <= ' ' }
            val encodedimage = encodedimage.toString().trim { it <= ' ' }
            val request: StringRequest =
                object : StringRequest(Method.POST, url, Response.Listener { response ->
                    Toast.makeText(applicationContext, "FileUploaded Successfully", Toast.LENGTH_SHORT).show()

                    val profileIntent = Intent(this, DashboardUser::class.java)
                    startActivity(profileIntent)

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
                        map.put("date", date)
                        map.put("age", age)
                        map.put("bplace", bplace)
                        map.put("mobo", mobo)
                        map.put("house", house)
                        map.put("sitio", sitio)
                        map.put("brgy", brgy)
                        map.put("mun", mun)
                        map.put("prov", prov)
                        map.put("zip", zip)
                        map.put("civil", civil)
                        map.put("emp", emp)
                        map.put("occ", occ)
                        map.put("mincome", mincome)
                        map.put("religion", rel)
                        map.put("nation", nation)
                        map.put("upload", encodedimage)
                        return map
                    }
                }
            val queue = Volley.newRequestQueue(applicationContext)
            queue.add(request)
        }
    }