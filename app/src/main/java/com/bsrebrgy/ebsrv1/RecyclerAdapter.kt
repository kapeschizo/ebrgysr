package com.bsrebrgy.ebsrv1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerAdapter(private val mCtx: Context, private val announcelist: List<Announce>) : RecyclerView.Adapter<RecyclerAdapter.AnnounceViewHolder>() {

    inner class AnnounceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var itemImage : ImageView
        var itemTitle : TextView
        var itemLoc : TextView
        var itemTime : TextView
        var itemDesc : TextView

        init {
            itemImage = itemView.findViewById(R.id.itemImage)
            itemTitle = itemView.findViewById(R.id.itemTitle)
            itemLoc = itemView.findViewById(R.id.itemLoc)
            itemTime = itemView.findViewById(R.id.itemTime)
            itemDesc = itemView.findViewById(R.id.itemDesc)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.AnnounceViewHolder {

        val inflater = LayoutInflater.from(mCtx).inflate(R.layout.recycler_layout,parent,false)
        return AnnounceViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: AnnounceViewHolder, position: Int) {
        val announce = announcelist[position]
        val urli = announce.url+announce.img
        Glide.with(mCtx)
            .load(urli)
            .into(holder.itemImage)
        holder.itemTitle.text = announce.title
        holder.itemLoc.text = announce.loc
        holder.itemTime.text = (""+announce.start+" - "+announce.end+"")
        holder.itemDesc.text = announce.desc

        val isVisible = announce.visibility
        holder.itemDesc.visibility = if(isVisible) View.VISIBLE else View.GONE

        holder.itemImage.setOnClickListener {
            announce.visibility = !announce.visibility
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return announcelist.size
    }

}