package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Login : AppCompatActivity() {

    var userLoginEtxt: EditText? = null
    var passLoginETxt: EditText? = null
    var signupTxt: TextView? = null
    var loginBtn: Button? = null
    var forgetPassTxt: TextView? = null
    var loadinglogin: ProgressBar? = null

    lateinit var session : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)

        if(session.isloggedin()){
            val i = Intent(applicationContext, DashboardUser::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }

        loadinglogin = findViewById(R.id.loadinglogin)
        signupTxt = findViewById(R.id.signupTxt)
        signupTxt?.setOnClickListener {
            val signUpIntent = Intent(this, Register::class.java)
            startActivity(signUpIntent)
        }

        userLoginEtxt = findViewById(R.id.userLoginEtxt)
        passLoginETxt = findViewById(R.id.passLoginEtxt)
        loginBtn = findViewById(R.id.loginBtn)
        loginBtn?.setOnClickListener {
            login()
        }

        forgetPassTxt = findViewById(R.id.forgotPassTxt)
        forgetPassTxt!!.setOnClickListener {
            val forgetPassIntent = Intent(this,ForgetPass::class.java)
            startActivity(forgetPassIntent)
        }

    }
    private fun login() {
        loadinglogin?.visibility = View.VISIBLE
        loginBtn?.visibility = View.GONE
        val url = "http://www.barangaysanroqueantipolo.site/API/loginApi.php"
        val user = userLoginEtxt?.text.toString().trim { it <= ' ' }
        val pass = passLoginETxt?.text.toString().trim { it <= ' ' }

        if (user.isEmpty() || pass.isEmpty()) {
            loadinglogin?.visibility = View.GONE
            loginBtn?.visibility = View.VISIBLE
            Toast.makeText(applicationContext, "Required Field Empty!", Toast.LENGTH_SHORT).show()
        }
        else{
            val request: StringRequest =
                object : StringRequest(Method.POST, url, Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")
                        val message = jsonObject.getString("message")

                        if (success == "2") {
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            session.createdLoginSessions(user)
                            val personIntent = Intent(this, PersonalProfile::class.java)
                            startActivity(personIntent)
                        }
                        else if (success == "3")
                        {
                            session.createdLoginSessions(user)
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                            val dashboardIntent = Intent(this, DashboardUser::class.java)
                            startActivity(dashboardIntent)
                        }
                        else if (success == "9")
                        {
                            session.createdLoginSessions(user)
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                            val signatureIntent = Intent(this, Signature::class.java)
                            startActivity(signatureIntent)
                        }
                        else if (success == "10")
                        {
                            session.createdLoginSessions(user)
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                            val uploadidIntent = Intent(this, UploadID::class.java)
                            startActivity(uploadidIntent)
                        }
                        else if (success == "11")
                        {
                            session.createdLoginSessions(user)
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                            val selfieIntent = Intent(this, Selfie::class.java)
                            startActivity(selfieIntent)
                        }
                        else
                        {
                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            loadinglogin?.visibility = View.GONE
                            loginBtn?.visibility = View.VISIBLE
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Register Error! $e", Toast.LENGTH_LONG).show()
                        loadinglogin?.visibility = View.GONE
                        loginBtn?.visibility = View.VISIBLE
                    }
                }, Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                    loadinglogin?.visibility = View.GONE
                    loginBtn?.visibility = View.VISIBLE
                }
                ) {
                    @Throws(AuthFailureError::class)
                    override fun getParams(): MutableMap<String, String> {
                        val map: MutableMap<String, String> =
                            HashMap<String, String>()
                        map.put("user", user)
                        map.put("pass", pass)
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
}