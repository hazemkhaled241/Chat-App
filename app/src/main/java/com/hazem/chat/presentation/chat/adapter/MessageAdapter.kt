package com.hazem.chat.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hazem.chat.R
import com.hazem.chat.domain.model.Message
import java.text.SimpleDateFormat

class MessageAdapter:RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
    private var messages: List<Message> = arrayListOf()
    private var currentUserId: String = ""
    companion object {
        private const val VIEW_TYPE_SENDER = 0
        private const val VIEW_TYPE_RECEIVER = 1
    }
inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    private val messageTextView: TextView = itemView.findViewById(R.id.tv_message)
    private val imageView: ImageView = itemView.findViewById(R.id.iv_photo)
    private val dateTextView: TextView = itemView.findViewById(R.id.tv_time)
    private val formatter = SimpleDateFormat("HH:mm")

    fun bind(message: Message){
        if (message.imageUrl!=null) {
            messageTextView.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            imageView.load(message.imageUrl)
            dateTextView.text=formatter.format(message.date)
        } else {
            messageTextView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            messageTextView.text = message.messageText
            dateTextView.text=formatter.format(message.date)
        }
    }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutResId = if (viewType == VIEW_TYPE_SENDER) {
            R.layout.message_sent_item
        } else {
            R.layout.message_receive_item
        }
        val view = layoutInflater.inflate(layoutResId, parent, false)
        return MyViewHolder(view)
    }

    fun addMessages(newMessages: List<Message>, currentUserId: String) {
        this.currentUserId = currentUserId
        val diffUtilCallBack = MessagesDiffUtil(this.messages, newMessages)
        val result = DiffUtil.calculateDiff(diffUtilCallBack)
        this.messages = newMessages
        result.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == currentUserId) {
            VIEW_TYPE_SENDER
        } else {
            VIEW_TYPE_RECEIVER
        }
    }
}