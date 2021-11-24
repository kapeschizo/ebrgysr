package com.bsrebrgy.ebsrv1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val mCtx: Context, private val newslist: List<News>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var newsImage : ImageView
        var newsLoc : TextView
        var newsTime : TextView
        var newsDesc : TextView

        init {
            newsImage = itemView.findViewById(R.id.newsImage)
            newsLoc = itemView.findViewById(R.id.newsLoc)
            newsTime = itemView.findViewById(R.id.newsTime)
            newsDesc = itemView.findViewById(R.id.newsDesc)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {

        val inflater = LayoutInflater.from(mCtx).inflate(R.layout.news_layout,parent,false)
        return NewsViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newslist[position]
        val urli = news.url+news.img
        Glide.with(mCtx)
            .load(urli)
            .into(holder.newsImage)
        holder.newsLoc.text = news.loc
        holder.newsTime.text = (news.date)
        holder.newsDesc.text = news.desc

        val isVisible = news.visibility
        holder.newsDesc.visibility = if(isVisible) View.VISIBLE else View.GONE

        holder.newsImage.setOnClickListener {
            news.visibility = !news.visibility
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return newslist.size
    }

}