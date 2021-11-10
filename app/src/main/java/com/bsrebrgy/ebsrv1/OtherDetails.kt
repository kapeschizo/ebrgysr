package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class OtherDetails : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var otherBtn : Button? = null
    var spinnerCivil: Spinner? = null
    var spinnerEmp: Spinner? = null
    var empAdapter: ArrayAdapter<String>? = null
    var civilAdapter: ArrayAdapter<String>? = null
    var civilStatus = ""
    var empStatus = ""
    var occuTxt : EditText? = null
    var monthTxt : EditText?= null
    var relTxt : EditText? = null
    var nationTxt : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_details)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        spinnerCivil = findViewById(R.id.spinnerCivil)

        val civilStat = arrayOf("Single","Married","Separated","Widowed")
        civilAdapter = ArrayAdapter<String>(this@OtherDetails,android.R.layout.simple_spinner_dropdown_item,civilStat)
        spinnerCivil?.setAdapter(civilAdapter)

        spinnerCivil?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+civilStat[position],Toast.LENGTH_SHORT).show()
                civilStatus = civilStat[position]
            }
        }

        spinnerEmp = findViewById(R.id.spinnerEmp)
        val empStat = arrayOf("Student","Employed","Self-Employed","Unemployed")
        empAdapter = ArrayAdapter<String>(this@OtherDetails,android.R.layout.simple_spinner_dropdown_item,empStat)
        spinnerEmp?.setAdapter(empAdapter)

        occuTxt = findViewById(R.id.occuTxt)
        monthTxt = findViewById(R.id.monthTxt)

        spinnerEmp?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@Profile, "You Have Selected"+" "+empStat[position],Toast.LENGTH_SHORT).show()
                empStatus = empStat[position]

                if(empStatus == "Student" || empStatus == "Unemployed")
                {
                    occuTxt?.isEnabled = false
                    occuTxt?.setText("None")
                    monthTxt?.isEnabled = false
                    monthTxt?.setText("0")
                }
                else
                {
                    occuTxt?.isEnabled = true
                    occuTxt?.setText("")
                    monthTxt?.isEnabled = true
                    monthTxt?.setText("")
                }

            }
        }

        otherBtn = findViewById(R.id.otherBtn)
        otherBtn?.setOnClickListener {
            val bundle: Bundle? = intent.extras
            val fname = bundle?.getString("fname")
            val mname = bundle?.getString("mname")
            val lname = bundle?.getString("lname")
            val sexname = bundle?.getString("sexname")
            val extname = bundle?.getString("extname")
            val maidenname = bundle?.getString("maidenname")
            val age = bundle?.getString("age")
            val bdate = bundle?.getString("bdate")
            val bplace = bundle?.getString("bplace")
            val mobo = bundle?.getString("mobo")
            val house = bundle?.getString("house")
            val purok = bundle?.getString("purok")
            val civil = civilStatus
            val emp = empStatus
            occuTxt = findViewById(R.id.occuTxt)
            val occu = occuTxt?.text.toString()
            monthTxt = findViewById(R.id.monthTxt)
            val mon = monthTxt?.text.toString()
            relTxt = findViewById(R.id.relTxt)
            val rel = relTxt?.text.toString()
            nationTxt = findViewById(R.id.nationTxt)
            val nation = nationTxt?.text.toString()

            val profileDisplay = Intent(this, InfoDisplay::class.java)
            profileDisplay.putExtra("fname",fname)
            profileDisplay.putExtra("mname",mname)
            profileDisplay.putExtra("lname",lname)
            profileDisplay.putExtra("sexname",sexname)
            profileDisplay.putExtra("extname",extname)
            profileDisplay.putExtra("maidenname",maidenname)
            profileDisplay.putExtra("age",age)
            profileDisplay.putExtra("bdate",bdate)
            profileDisplay.putExtra("bplace",bplace)
            profileDisplay.putExtra("mobo",mobo)
            profileDisplay.putExtra("house",house)
            profileDisplay.putExtra("purok",purok)
            profileDisplay.putExtra("civil",civil)
            profileDisplay.putExtra("emp",emp)
            profileDisplay.putExtra("occu",occu)
            profileDisplay.putExtra("mon",mon)
            profileDisplay.putExtra("rel",rel)
            profileDisplay.putExtra("nation",nation)
            startActivity(profileDisplay)
        }
    }
}