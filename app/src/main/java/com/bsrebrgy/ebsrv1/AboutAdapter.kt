package com.bsrebrgy.ebsrv1


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AboutAdapter(private val mCtx: Context, private val aboutlist: List<About>) : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

    inner class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var titleabout: TextView
        var descabout: TextView
        var rules: TextView
        var descrules: TextView


        init {
            title = itemView.findViewById(R.id.titleItem)
            titleabout = itemView.findViewById(R.id.titleAbout)
            descabout = itemView.findViewById(R.id.descAbout)
            rules = itemView.findViewById(R.id.titleRules)
            descrules = itemView.findViewById(R.id.descRules)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AboutAdapter.AboutViewHolder {

        val inflater = LayoutInflater.from(mCtx).inflate(R.layout.about_layout, parent, false)
        return AboutViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        val about = aboutlist[position]

        holder.title.text = about.title
        holder.titleabout.text = about.titleAbout
        holder.descabout.text = about.descAbout
        holder.rules.text = about.titleRules
        holder.descrules.text = about.descRules

        val isVisible = about.visibility
        holder.titleabout.visibility = if (isVisible) View.VISIBLE else View.GONE
        holder.descabout.visibility = if (isVisible) View.VISIBLE else View.GONE
        holder.rules.visibility = if (isVisible) View.VISIBLE else View.GONE
        holder.descrules.visibility = if (isVisible) View.VISIBLE else View.GONE

        holder.title.setOnClickListener {
            about.visibility = !about.visibility
            notifyItemChanged(position)

        }
    }

        override fun getItemCount(): Int {
            return aboutlist.size
        }
    }