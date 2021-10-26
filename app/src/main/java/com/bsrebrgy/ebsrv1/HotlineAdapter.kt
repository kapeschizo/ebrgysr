package com.bsrebrgy.ebsrv1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class HotlineAdapter(private val mCtx: Context, private val hotlinelist: List<Hline>) : RecyclerView.Adapter<HotlineAdapter.HotlineViewHolder>() {

    inner class HotlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var hname: TextView
        var hnum: TextView
        var callimg: ImageView

        init {
            hname = itemView.findViewById(R.id.hnameTxt)
            hnum = itemView.findViewById(R.id.hnumTxt)
            callimg = itemView.findViewById(R.id.callImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotlineAdapter.HotlineViewHolder {

        val inflater = LayoutInflater.from(mCtx).inflate(R.layout.hotline_layout, parent, false)
        return HotlineViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: HotlineViewHolder, position: Int) {
        val hline = hotlinelist[position]

        holder.hname.text = hline.hname
        holder.hnum.text = hline.hnum

        holder.callimg.setOnClickListener {
            Dexter.withContext(mCtx)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.setData(Uri.parse("tel:" + hline.hnum))
                        mCtx.startActivity(intent)
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        }
    }

    override fun getItemCount(): Int {
        return hotlinelist.size
    }
}