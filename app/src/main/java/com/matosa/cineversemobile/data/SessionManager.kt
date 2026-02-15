package com.matosa.cineversemobile.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("cineverse_prefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }

    // Guardamos el token
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    // Lee el token
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserId(id: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    // Leer el ID
    fun fetchUserId(): Int {
        return prefs.getInt(USER_ID, -1)
    }

    // Borra la sesión
    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}