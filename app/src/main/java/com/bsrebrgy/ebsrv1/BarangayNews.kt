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

class BarangayNews : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var newslist: MutableList<News>? = null
    var recyclerView: RecyclerView? = null
    var requestQueue: RequestQueue? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barangay_news)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerView)

        layoutManager = LinearLayoutManager (this)
        recyclerView?.layoutManager = layoutManager

        newslist = ArrayList()

        loadAnnouncements()
    }

    private fun loadAnnouncements() {
        val url = "http://www.barangaysanroqueantipolo.site/API/newsApi.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("news")
                for (i in 0 until jsonArray.length()) {
                    val news = jsonArray.getJSONObject(i)

                    newslist!!.add(
                        News(
                            news.getString("img"),
                            news.getString("date"),
                            news.getString("loc"),
                            news.getString("desc"),
                            news.getString("url")
                        )
                    )
                }
                val adapter = NewsAdapter(
                    this@BarangayNews,
                    newslist!!
                )
                recyclerView?.adapter = adapter

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)
    }

}