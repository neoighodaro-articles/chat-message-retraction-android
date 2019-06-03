package com.neo.chatkitretractmessage


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("delete-message")
    fun retractMessage(@Body body: RetractedMessage): Call<ResponseBody>

}