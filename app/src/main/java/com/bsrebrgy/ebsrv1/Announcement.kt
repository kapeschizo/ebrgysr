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







class Announcement : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var announcelist: MutableList<Announce>? = null
    var recyclerView: RecyclerView? = null
    var requestQueue: RequestQueue? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerView)

        layoutManager = LinearLayoutManager (this)
        recyclerView?.layoutManager = layoutManager

        announcelist = ArrayList()

        loadAnnouncements()
    }

    private fun loadAnnouncements() {
        val url = "http://www.barangaysanroqueantipolo.site/API/announceApi.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("announce")
                for (i in 0 until jsonArray.length()) {
                    val announce = jsonArray.getJSONObject(i)

                    announcelist!!.add(
                        Announce(
                            announce.getString("img"),
                            announce.getString("title"),
                            announce.getString("loc"),
                            announce.getString("start"),
                            announce.getString("end"),
                            announce.getString("desc"),
                            announce.getString("url")
                        )
                    )
                }
                    val adapter = RecyclerAdapter(
                        this@Announcement,
                        announcelist!!
                    )
                    recyclerView?.adapter = adapter

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)
    }
}