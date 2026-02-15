package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    val id: Int,

    @SerializedName("conversation_id")
    val conversationId: Int,

    @SerializedName("sender_id")
    val senderId: Int,

    val message: String,

    @SerializedName("message_type")
    var messageType: String = "TEXT",

    @SerializedName("sent_at")
    var sentAt: String? = null
)

data class SendMessageRequest(
    @SerializedName("user_id") val userId: Int,
    val message: String
)
