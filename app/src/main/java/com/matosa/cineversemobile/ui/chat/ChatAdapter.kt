package com.matosa.cineversemobile.ui.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.matosa.cineversemobile.R
import com.matosa.cineversemobile.model.ChatMessage

class ChatAdapter(
    private val myUserId: Int,
    private val messages: MutableList<ChatMessage>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val container: ConstraintLayout = view.findViewById(R.id.clMessageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val msg = messages[position]

        holder.tvMessage.text = msg.message

        val rawTime = msg.sentAt ?: ""

        holder.tvTime.text = when {
            rawTime.contains("T") -> rawTime.substringAfter("T").substring(0, 5)
            rawTime.contains(" ") -> rawTime.split(" ")[1].substring(0, 5)
            else -> rawTime
        }

        val constraintSet = ConstraintSet()
        constraintSet.clone(holder.container)

        if (msg.senderId == myUserId) {
            constraintSet.clear(R.id.tvMessage, ConstraintSet.START)
            constraintSet.connect(R.id.tvMessage, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            holder.tvMessage.setBackgroundColor(Color.parseColor("#00D4FF"))
        } else {
            constraintSet.clear(R.id.tvMessage, ConstraintSet.END)
            constraintSet.connect(R.id.tvMessage, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            holder.tvMessage.setBackgroundColor(Color.parseColor("#333344"))
        }

        constraintSet.connect(R.id.tvTime, ConstraintSet.END, R.id.tvMessage, ConstraintSet.END)
        constraintSet.connect(R.id.tvTime, ConstraintSet.START, R.id.tvMessage, ConstraintSet.START)

        constraintSet.applyTo(holder.container)
    }

    override fun getItemCount() = messages.size

    fun addMessage(newMessage: ChatMessage) {
        messages.add(newMessage)
        notifyItemInserted(messages.size - 1)
    }

    fun updateMessages(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}