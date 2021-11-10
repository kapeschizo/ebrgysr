package com.bsrebrgy.ebsrv1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private var mCtx: Context, private var chatlist: List<Chatlister>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var chatName : TextView
        var chatMessage : TextView
        var chatDate : TextView

        init {
            chatName = itemView.findViewById(R.id.chatName1)
            chatMessage = itemView.findViewById(R.id.chatMessage1)
            chatDate = itemView.findViewById(R.id.chatDate1)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {

        val inflater = LayoutInflater.from(mCtx).inflate(R.layout.chat_layout,parent,false)
        return ChatViewHolder(inflater)

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatlister = chatlist[position]

        holder.chatName.text = (""+chatlister.fnam+" "+chatlister.lnam+"")
        holder.chatMessage.text = chatlister.msg
        holder.chatDate.text = chatlister.dt

    }

    override fun getItemCount(): Int {
        return chatlist.size
    }


}