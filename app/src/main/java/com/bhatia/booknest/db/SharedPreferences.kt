package com.bhatia.booknest.db

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("pdf_preferences", Context.MODE_PRIVATE)

    fun saveLastReadPage(pageNumber: Int) {
        sharedPreferences.edit().putInt("last_read_page", pageNumber).apply()
    }

    fun getLastReadPage(): Int {
        return sharedPreferences.getInt("last_read_page", 0)
    }
}
