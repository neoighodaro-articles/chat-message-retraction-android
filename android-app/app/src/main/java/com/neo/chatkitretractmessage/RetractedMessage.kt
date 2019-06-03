package com.neo.chatkitretractmessage


import com.google.gson.annotations.SerializedName

data class RetractedMessage(

    @field:SerializedName("messageId")
    val messageId: Int? = null
)