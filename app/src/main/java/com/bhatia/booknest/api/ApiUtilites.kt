package com.bhatia.booknest.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilites {
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummybase.com/")  // Set the base URL for the API requests
            .addConverterFactory(GsonConverterFactory.create())  // Add a converter to handle JSON responses
            .build()  // Build and return the Retrofit instance
    }
}