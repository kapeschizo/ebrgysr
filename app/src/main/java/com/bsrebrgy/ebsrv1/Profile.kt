package com.bsrebrgy.ebsrv1

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class Profile : AppCompatActivity() {
    private var y: Int = 0
    private var m: Int = 0
    private var d: Int = 0
    var spinnerSitio: Spinner? = null
    var spinnerCivilStat: Spinner? = null
    var spinnerEmpStat: Spinner? = null
    var sitioList: ArrayList<String> = ArrayList()
    var sitioAdapter: ArrayAdapter<String>? = null
    var civilAdapter: ArrayAdapter<String>? = null
    var empAdapter: ArrayAdapter<String>? = null
    var requestQueue: RequestQueue? = null
    var fnameEtxt : EditText? = null
    var mnameEtxt : EditText? = null
    var lnameEtxt : EditText? = null
    var extnameEtxt : EditText? = null
    var sex = ""
    var maidenEtxt : EditText? = null
    var dateEtxt : EditText? = null
    var ageEtxt : EditText? = null
    var bplaceEtxt : EditText? = null
    var moboEtxt : EditText? = null
    var houseEtxt : EditText? = null
    var sitio = ""
    var brgyEtxt : EditText? = null
    var munEtxt : EditText? = null
    var provEtxt : EditText? = null
    var zipEtxt : EditText? = null
    var civilStatus = ""
    var empStatus = ""
    var occEtxt : EditText? = null
    var mincomeEtxt : EditText?= null
    var religionEtxt : EditText? = null
    var nationEtxt : EditText? = null
    var nextBtn : Button? = null
    lateinit var session : SessionManager
    var user : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        dateEtxt = findViewById(R.id.dateEtxt)
        val dateBtn = findViewById<Button>(R.id.dateBtn)
        dateBtn.setOnClickListener {
            val c = Calendar.getInstance()
            d = c.get(Calendar.DATE)
            m = c.get(Calendar.MONTH)
            y = c.get(Calendar.YEAR)

            val datePicker = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    val mon = month+1
                    dateEtxt?.setText(year.toString() + "-" + mon.toString() + "-" + dayOfMonth.toString())
                }, y, m, d)
            datePicker.show()
        }

        val radioSexGroup = findViewById<RadioGroup>(R.id.radioSexGroup)
        radioSexGroup.setOnCheckedChangeListener { group, ID ->
            when(ID)
            {
                R.id.maleRadio -> {
                    sex = "Male"
//                    Toast.makeText(this@Profile, "You Have Selected"+" "+sex,Toast.LENGTH_SHORT).show()
                }
                R.id.radioFemale -> {
                    sex = "Female"
//                    Toast.makeText(this@Profile, "You Have Selected"+" "+sex,Toast.LENGTH_SHORT).show()
                }
            }
        }

        requestQueue = Volley.newRequestQueue(this)
        spinnerSitio = findViewById(R.id.spinnerSitio)

        val url = "http://www.barangaysanroqueantipolo.site/API/sitioSpinnerApi.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("sitios")
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val sitioName = jsonObject.optString("sitio")
                    sitioList.add(sitioName)
                    sitioAdapter = ArrayAdapter<String>(
                        this@Profile,
                        android.R.layout.simple_spinner_dropdown_item, sitioList
                    )
                    spinnerSitio?.setAdapter(sitioAdapter)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)

        spinnerSitio?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+sitioList[position],Toast.LENGTH_SHORT).show()
                sitio = sitioList[position]
            }
        }

        spinnerCivilStat = findViewById(R.id.spinnerCivilStat)

        val civilStat = arrayOf("Single","Married","Separated","Widowed")
        civilAdapter = ArrayAdapter<String>(this@Profile,android.R.layout.simple_spinner_dropdown_item,civilStat)
        spinnerCivilStat?.setAdapter(civilAdapter)

        spinnerCivilStat?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+civilStat[position],Toast.LENGTH_SHORT).show()
                civilStatus = civilStat[position]
            }
        }

        spinnerEmpStat = findViewById(R.id.spinnerEmpStat)
        val empStat = arrayOf("Student","Employed","Self-Employed","Unemployed")
        empAdapter = ArrayAdapter<String>(this@Profile,android.R.layout.simple_spinner_dropdown_item,empStat)
        spinnerEmpStat?.setAdapter(empAdapter)

        occEtxt = findViewById(R.id.occEtxt)
        mincomeEtxt = findViewById(R.id.mincomeEtxt)
        spinnerEmpStat?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+empStat[position],Toast.LENGTH_SHORT).show()
                empStatus = empStat[position]

                if(empStatus == "Student" || empStatus == "Unemployed")
                {
                    occEtxt?.isEnabled = false
                    occEtxt?.setText("None")
                    mincomeEtxt?.isEnabled = false
                    mincomeEtxt?.setText("0")
                }
                else
                {
                    occEtxt?.isEnabled = true
                    occEtxt?.setText("")
                    mincomeEtxt?.isEnabled = true
                    mincomeEtxt?.setText("")
                }

            }
        }

        nextBtn = findViewById(R.id.nextBtn)
        nextBtn?.setOnClickListener {

            fnameEtxt = findViewById(R.id.fnameEtxt)
            val fname = fnameEtxt?.text.toString()
            mnameEtxt = findViewById(R.id.mnameEtxt)
            val mname = mnameEtxt?.text.toString()
            lnameEtxt = findViewById(R.id.lnameEtxt)
            val lname = lnameEtxt?.text.toString()
            extnameEtxt = findViewById(R.id.extnameEtxt)
            val extname = extnameEtxt?.text.toString()
            val sexname = sex
            maidenEtxt = findViewById(R.id.maidenEtxt)
            val maiden = maidenEtxt?.text.toString()
            val date = dateEtxt?.text.toString()
            ageEtxt = findViewById(R.id.ageEtxt)
            val age = ageEtxt?.text.toString()
            bplaceEtxt = findViewById(R.id.bplaceEtxt)
            val bplace = bplaceEtxt?.text.toString()
            moboEtxt = findViewById(R.id.moboEtxt)
            val mobo = moboEtxt?.text.toString()
            houseEtxt = findViewById(R.id.houseEtxt)
            val house = houseEtxt?.text.toString()
            val sitio = sitio
            brgyEtxt = findViewById(R.id.brgyEtxt)
            val brgy = brgyEtxt?.text.toString()
            munEtxt = findViewById(R.id.munEtxt)
            val mun = munEtxt?.text.toString()
            provEtxt = findViewById(R.id.provEtxt)
            val prov = provEtxt?.text.toString()
            zipEtxt = findViewById(R.id.zipEtxt)
            val zip = zipEtxt?.text.toString()
            val civil = civilStatus
            val emp = empStatus
            occEtxt = findViewById(R.id.occEtxt)
            val occ = occEtxt?.text.toString()
            mincomeEtxt = findViewById(R.id.mincomeEtxt)
            val mincome = mincomeEtxt?.text.toString()
            religionEtxt = findViewById(R.id.religionEtxt)
            val religion = religionEtxt?.text.toString()
            nationEtxt = findViewById(R.id.nationEtxt)
            val nation = nationEtxt?.text.toString()

            val profileImageIntent = Intent(applicationContext,ProfileImage::class.java)
            profileImageIntent.putExtra("fname",fname)
            profileImageIntent.putExtra("mname",mname)
            profileImageIntent.putExtra("lname",lname)
            profileImageIntent.putExtra("extname",extname)
            profileImageIntent.putExtra("sex",sexname)
            profileImageIntent.putExtra("maiden",maiden)
            profileImageIntent.putExtra("date",date)
            profileImageIntent.putExtra("age",age)
            profileImageIntent.putExtra("bplace",bplace)
            profileImageIntent.putExtra("mobo",mobo)
            profileImageIntent.putExtra("house",house)
            profileImageIntent.putExtra("sitio",sitio)
            profileImageIntent.putExtra("brgy",brgy)
            profileImageIntent.putExtra("mun",mun)
            profileImageIntent.putExtra("prov",prov)
            profileImageIntent.putExtra("zip",zip)
            profileImageIntent.putExtra("civil",civil)
            profileImageIntent.putExtra("emp",emp)
            profileImageIntent.putExtra("occ",occ)
            profileImageIntent.putExtra("mincome",mincome)
            profileImageIntent.putExtra("religion",religion)
            profileImageIntent.putExtra("nation",nation)
            startActivity(profileImageIntent)

        }

    }
}
