package com.bsrebrgy.ebsrv1

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class Chat : AppCompatActivity() {
    val h = Handler()
    lateinit var session : SessionManager
    var user : String? = null
    var chatlist: MutableList<Chatlister>? = null
    var recyclerView: RecyclerView? = null
    var requestQueue: RequestQueue? = null
    var txtMsg: EditText? = null
    var sendBtn : Button? = null
    //Here you can change timer interval oke.:)
    val howManySecondsToRefresh:Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerView)

        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        recyclerView?.layoutManager = layoutManager
        recyclerView?.refreshDrawableState()

        chatlist = ArrayList()
        //On screen load show first time chats
        loadChats()
        h.postDelayed(object : Runnable {
            private var time: Long = 0
            override fun run() {
                //after every N seconds refresh view
                loadChats()
                time += 1000
                h.postDelayed(this, howManySecondsToRefresh)
//                Log.d("K3xri","Updating RecyclerView")
            }
        }, howManySecondsToRefresh)
        txtMsg = findViewById(R.id.txtMsg)
        sendBtn = findViewById(R.id.sendBtn)
        sendBtn?.setOnClickListener { uploadtoserver()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        h.removeCallbacksAndMessages(null)
    }

    private fun loadChats() {
        val url = "http://www.barangaysanroqueantipolo.site/API/chatApi.php"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, { response ->
            try {
                val jsonArray = response.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val chat = jsonArray.getJSONObject(i)
                    chatlist!!.add(
                        Chatlister(
                            chat.getString("fnam"),
                            chat.getString("lnam"),
                            chat.getString("msg"),
                            chat.getString("dt"),
                        ))
                }
                val adapter = ChatAdapter(
                    this@Chat,
                    chatlist!!
                )
                recyclerView?.scrollToPosition((chatlist as ArrayList<*>).size - 1)
                recyclerView?.adapter = adapter

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        requestQueue?.add(jsonObjectRequest)
    }


    private fun uploadtoserver() {
        val url = "http://www.barangaysanroqueantipolo.site/API/sendChatApi.php"
        val username: String = user!!.toString().trim { it <= ' ' }
        val message = txtMsg?.text.toString().trim { it <= ' ' }
        val request: StringRequest =
            object : StringRequest(
                Method.POST, url, Response.Listener { response ->
                    Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_SHORT).show()

                },
                Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): MutableMap<String, String> {
                    val map: MutableMap<String, String> =
                        HashMap<String, String>()
                    map.put("user", username)
                    map.put("message", message)
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