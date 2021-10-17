package com.bsrebrgy.ebsrv1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class SessionManager {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var con: Context
    var PRIVATEMODE: Int = 0

    constructor(con: Context) {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATEMODE)
        editor = pref.edit()
    }

    companion object {
        val PREF_NAME = "Login_Preference"
        val IS_LOGIN = "isloggedin"
        val KEY_USERNAME = "user"
    }

    fun createdLoginSessions(user: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, user)
        editor.commit()
    }

    fun checkLogin() {
        if (!this.isloggedin()) {
            var i: Intent = Intent(con, Login::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }
    }

    fun getUserDetails(): HashMap<String, String> {
        var user: Map<String, String> = HashMap()
        (user as HashMap).put(KEY_USERNAME, pref.getString(KEY_USERNAME, null)!!)
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        var i = Intent(con, MainActivity::class.java)
        i.putExtra("finish", true) // if you are checking for this in your other Activities
        i.setFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
            Intent.FLAG_ACTIVITY_CLEAR_TASK or
            Intent.FLAG_ACTIVITY_NEW_TASK
        )
        con.startActivity(i)
    }

    fun isloggedin(): Boolean{
        return pref.getBoolean(IS_LOGIN, false)
    }
}