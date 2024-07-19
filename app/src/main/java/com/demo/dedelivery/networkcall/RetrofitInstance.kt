package com.demo.dedelivery.networkcall

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val baseUrl = "https://link of base url"
    fun getInstance(): Retrofit {
        val client = OkHttpClient()
//        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder: OkHttpClient.Builder =
            client.newBuilder()
//                .addInterceptor(interceptor as HttpLoggingInterceptor)

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()

//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        return Retrofit.Builder().baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
    }
}