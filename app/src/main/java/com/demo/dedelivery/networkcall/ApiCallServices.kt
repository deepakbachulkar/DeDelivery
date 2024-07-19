package com.demo.dedelivery.networkcall

import com.demo.dedelivery.model.RequestLogs
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiCallServices {

        @Headers("client: dudelivery")
        @POST("logs")
        fun getLogs(
                @Header("Authorization") header: String,
                @Body body: RequestLogs
        ) : Call<Any>

}