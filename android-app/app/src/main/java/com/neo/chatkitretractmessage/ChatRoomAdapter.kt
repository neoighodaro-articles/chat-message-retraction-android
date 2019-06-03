package com.neo.chatkitretractmessage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pusher.chatkit.messages.multipart.Message
import com.pusher.chatkit.messages.multipart.Payload

class ChatRoomAdapter (private val chatRoomListener: ChatRoomListener)
    : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    private var messageList = ArrayList<Message>()

    fun addMessage(model:Message){
        val index = messageList.indexOfFirst { it.id == model.id }
        if (index >= 0){
            messageList[index] = model
        } else {
            messageList.add(model)
        }
        notifyDataSetChanged()
    }

    fun remove(position: Int){
        val message = messageList[position]
        chatRoomListener.onDeleteMessage(message.id)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(messageList[position])

    override fun getItemCount() = messageList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val username: TextView = itemView.findViewById(R.id.editTextUsername)
        private val message: TextView = itemView.findViewById(R.id.editTextMessage)
        private val delete : ImageView = itemView.findViewById(R.id.delete_btn)

        fun bind(item: Message) = with(itemView) {
            username.text = item.sender.name
            if (item.sender.id != ChatkitApp.currentUser.id) {
                delete.visibility = View.GONE
            } else {
                delete.visibility = View.VISIBLE
            }

            delete.setOnClickListener{
                // retractMessage(item.id)
                remove(adapterPosition)

            }
            when (val data = item.parts[0].payload){

                is Payload.Inline -> {
                    message.text = data.content
                }

            }

        }

    }

    interface ChatRoomListener {
        fun onDeleteMessage(messageId: Int)
    }

}