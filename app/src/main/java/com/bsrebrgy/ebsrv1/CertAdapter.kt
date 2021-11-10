package com.bsrebrgy.ebsrv1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CertAdapter(private val mCtx: Context, private val certlist: List<Cert>) : RecyclerView.Adapter<CertAdapter.CertViewHolder>() {

    inner class CertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var purp: TextView
        var refnum: TextView
        var certDl: ImageView
        var certUrl: TextView
        var type: TextView

        init {
            purp = itemView.findViewById(R.id.purpTxt)
            refnum = itemView.findViewById(R.id.refTxt)
            certDl = itemView.findViewById(R.id.certDl)
            certUrl = itemView.findViewById(R.id.certUrl)
            type = itemView.findViewById(R.id.cert)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertAdapter.CertViewHolder {

        val inflater = LayoutInflater.from(mCtx).inflate(R.layout.cert_layout, parent, false)
        return CertViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CertViewHolder, position: Int) {
        val cert = certlist[position]

        holder.purp.text = cert.purp
        holder.refnum.text = cert.refnum
        holder.certUrl.text = cert.certUrl
        holder.type.text = cert.type

        val urlcert = cert.certUrl
        holder.certDl.setOnClickListener {
            val url = "http://www.barangaysanroqueantipolo.site$urlcert"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            mCtx.startActivity(i)
        }
    }
    override fun getItemCount(): Int {
        return certlist.size
    }
}