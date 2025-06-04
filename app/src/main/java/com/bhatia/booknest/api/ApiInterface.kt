package com.bhatia.booknest.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {
    @GET
    suspend fun downloadPdfFile(@Url fileUrl: String): Response<ResponseBody>
}