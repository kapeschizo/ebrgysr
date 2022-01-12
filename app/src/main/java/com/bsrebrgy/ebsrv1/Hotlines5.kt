package com.bsrebrgy.ebsrv1

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class Hotlines5 : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var hotlinelist: MutableList<Hline>? = null
    var temphotlinelist : MutableList<Hline>? = null
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
        temphotlinelist = ArrayList()


        val search = findViewById<SearchView>(R.id.search_it)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                temphotlinelist?.clear()
                val searchtext = newText!!.toLowerCase(Locale.getDefault())
                if (searchtext.isNotEmpty()){

                    hotlinelist?.forEach {

                        if(it.hname.toLowerCase(Locale.getDefault()).contains(searchtext)){
                            temphotlinelist?.add(it)
                        }
                    }
                    recyclerView?.adapter!!.notifyDataSetChanged()
                }
                else
                {
                    temphotlinelist?.clear()
                    temphotlinelist?.addAll(hotlinelist!!)
                    recyclerView?.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })

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
                temphotlinelist?.addAll(hotlinelist!!)
                val adapter = HotlineAdapter(
                    this@Hotlines5,
                     temphotlinelist!!

                )
                recyclerView?.adapter = adapter

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)
    }
}