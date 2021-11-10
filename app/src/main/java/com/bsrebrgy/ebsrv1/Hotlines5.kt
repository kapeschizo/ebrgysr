package com.bsrebrgy.ebsrv1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class Hotlines5 : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var hotlinelist: MutableList<Hline>? = null
    var recyclerView: RecyclerView? = null
    var requestQueue: RequestQueue? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotlines5)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerView)

        layoutManager = LinearLayoutManager (this)
        recyclerView?.layoutManager = layoutManager

        hotlinelist = ArrayList()

        loadHotlines()
    }

    private fun loadHotlines() {
        val url = "http://www.barangaysanroqueantipolo.site/API/hotline5Api.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("hotline")
                for (i in 0 until jsonArray.length()) {
                    val hline = jsonArray.getJSONObject(i)

                    hotlinelist!!.add(
                        Hline(
                            hline.getString("hname"),
                            hline.getString("hnum")
                        )
                    )
                }
                val adapter = HotlineAdapter(
                    this@Hotlines5,
                    hotlinelist!!
                )
                recyclerView?.adapter = adapter

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)
    }
}