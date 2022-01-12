package com.bsrebrgy.ebsrv1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class AboutUs : AppCompatActivity() {
    lateinit var session : SessionManager
    var user : String? = null
    var aboutlist: MutableList<About>? = null
    var recyclerView: RecyclerView? = null
    var requestQueue: RequestQueue? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        session = SessionManager(this)
        session.checkLogin()

        val data = session.getUserDetails()
        user = data.get(SessionManager.KEY_USERNAME)

        requestQueue = Volley.newRequestQueue(this)
        recyclerView = findViewById(R.id.recyclerView)

        layoutManager = LinearLayoutManager (this)
        recyclerView?.layoutManager = layoutManager

        val contactUs = findViewById<Button>(R.id.contactUs)
        contactUs.setOnClickListener {
            val contact = Intent(this, ContactUs::class.java)
            startActivity(contact)
        }

        aboutlist = ArrayList()

        loadAnnouncements()
    }

    private fun loadAnnouncements() {

        aboutlist?.add(
            About(
                "EBSRAC Android App",
                "About EBSRAC Android App",
                "EBSRAC is all about using technology to fulfill everyone's needs. With state-of-the-art barangay information management system, the process for acquiring your \n" +
                        "essential documents will be streamlined and hassle-free. \n\nWith in-app news bulletin and community chat, residents are up to date with current affairs, events and festivities hosted by EBSRAC.",
                "App Info",
                "App Name : EBSRAC\nApp Version : 1.0.6\n"
            )
        )
        aboutlist?.add(
            About(
                "Barangay News and Announcements",
                "About Barangay News and Announcements",
                "Allowing residents to can catch up with the latest news and updates within EBSRAC using our in-app News Bulletin that aims to circulate accurate information to \n" +
                        "the residents.",
                "",
                ""
            )
        )
        aboutlist?.add(
            About(
                "Community Chat",
                "About Community Chat",
                "A way for people to come together and stay connected thru our Community Chat. A real time messaging system that allows residents to gather live information, \n" +
                        "send queries and discussions within the community.",
                "Rules & Regulations",
                "1. No sending of Inappropriate Messages\n" +
                        "2. Strictly No Spamming of Messages within the Community Chat\n" +
                        "3. No spreading of False Information\n" +
                        "4. Barangay App nor its admins are not Liable for any personal Transactions done by the users in the Community Chat\n" +
                        "6. For any Urgent Announcements made by fellow users please refrain from replying if not involved in the said announcements \n" +
                        "7. Any Inappropriate Messages will be deleted by the admin\n"
            )
        )
        aboutlist?.add(
            About(
                "Request Forms",
                "About Request Forms",
                "Requisition and processing of important documents made easy thru our information management system that allows residents to request their forms online. \n" +
                        "With just a few taps, the residents and barangay staff are conveniently notified of the needed documents.",
                "Rules & Regulations",
                "1. No Filing of false information within the forms\n" +
                        "2. No False Information (false address, contacnt info ,etc.) \n" +
                        "3. Always Contact the Admin for additional info of certain fields within a specific form\n" +
                        "4. Barangay App nor its admins are not Liable for any personal Transactions done by the users \n" +
                        "5. The payment and needed additional requirements for your requested documents will be emailed together with the \n" +
                        "claiming date.\n" +
                        "6. Claim Document within 3 working days upon Approval and said date scheduled.\n" +
                        "7. Unclaimed Documents after the 3 day time period will be Discarded."
            )
        )
        aboutlist?.add(
            About(
                "My Certificates",
                "About My Certificates",
                "You can now have a look and see for yourself a preview of your requested document once approved, you can now check for possible errors in spelling but the \n" +
                        "document will have a watermark to avoid re-use and for security reasons. ",
                "",
                ""
            )
        )
        aboutlist?.add(
            About(
                "Hotlines",
                "About Hotlines",
                "Finding emergency contact details and number is now made easier. EBSRAC now allows user to call an emergency number (which requires load) directly.",
                "",
                ""
            )
        )

        val adapter = AboutAdapter(this@AboutUs, aboutlist!!)
        recyclerView?.adapter = adapter
    }

}