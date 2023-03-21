package com.hrithik.stegoshield.data

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .readTimeout(180, TimeUnit.SECONDS)
    .connectTimeout(180, TimeUnit.SECONDS)
    .build()

val retrofitClient = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("")
    .client(okHttpClient)
    .build()!!

interface RetrofitInterface {

    @POST("")
    fun postData(@Body data: MyData) : Call<MyData?>?

}

object RetrofitApi {
    val retrofitService: RetrofitInterface by lazy {
        retrofitClient.create(RetrofitInterface::class.java)
    }
}