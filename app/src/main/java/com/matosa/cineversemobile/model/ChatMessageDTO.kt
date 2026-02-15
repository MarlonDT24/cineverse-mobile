package com.matosa.cineversemobile.model

import com.google.gson.annotations.SerializedName

data class ChatMessageDTO(
    var id: Int? = null,
    var conversationId: Int? = null,
    var senderId: Int? = null,
    var senderUsername: String? = null,
    var message: String? = null,
    var messageType: String? = "TEXT",
    var sentAt: String? = null
)