package com.bsrebrgy.ebsrv1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class ResidentID : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var certlist: MutableList<Cert>? = null
    var recyclerView: RecyclerView? = null
    var requestQueue: RequestQueue? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resident_id)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerViewcert)

        layoutManager = LinearLayoutManager (this)
        recyclerView?.layoutManager = layoutManager

        certlist = ArrayList()

        loadCert()
    }

    private fun loadCert() {
        val url = "http://www.barangaysanroqueantipolo.site/API/cert4Api.php"
        val username = user.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(Method.POST, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray("certificate")
                    for (i in 0 until jsonArray.length()) {
                        val cert = jsonArray.getJSONObject(i)

                        certlist!!.add(
                            Cert(
                                cert.getString("purp"),
                                cert.getString("refnum"),
                                cert.getString("certUrl"),
                                cert.getString("type")
                            )
                        )

                    }
                    val adapter = CertAdapter(
                        this@ResidentID,
                        certlist!!
                    )
                    recyclerView?.adapter = adapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "No Record Found!", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val map: MutableMap<String, String> =
                        HashMap()
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

}