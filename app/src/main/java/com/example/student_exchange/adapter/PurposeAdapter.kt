package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R
import com.example.student_exchange.model.PurposeItem

class PurposeAdapter(private val purposeList: List<PurposeItem>) :
    RecyclerView.Adapter<PurposeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_purpose, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: PurposeItem = purposeList[position]
        // 데이터를 바인딩합니다.
        holder.courseTitleTextView.text = item.title
        holder.courseDetailsTextView.text = item.details
        holder.courseUserTextView.text = item.user
        holder.courseViewerTextView.text = item.viewer.toString()
        holder.courseScrapTextView.text = item.scrap.toString()
        holder.courseImageView.setImageResource(item.imageResId)



        // 추가 데이터 바인딩이 필요하면 여기에 추가
    }

    override fun getItemCount(): Int {
        return purposeList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courseTitleTextView: TextView = itemView.findViewById(R.id.courseTitleTextView)
        var courseDetailsTextView: TextView = itemView.findViewById(R.id.courseSubtitleTextView)
        var courseUserTextView: TextView = itemView.findViewById(R.id.tvCommenterName1)
        var courseViewerTextView: TextView = itemView.findViewById(R.id.tvViews)
        var courseScrapTextView: TextView = itemView.findViewById(R.id.tvScrap)
        var courseImageView: ImageView = itemView.findViewById(R.id.courseImageView)
    }
}
