package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R // 올바른 R 파일 import
import com.example.student_exchange.model.PopularCourseItem

class PopularCoursesAdapter(private val popularCoursesList: List<PopularCourseItem>) :
    RecyclerView.Adapter<PopularCoursesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_myposts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: PopularCourseItem = popularCoursesList[position]
        // 데이터를 바인딩합니다.
        holder.tvTitle.text = item.title
        holder.tvSubtitle.text = item.subtitle
        holder.tvViewCount.text = item.viewer.toString() // Int를 String으로 변환
        holder.tvScrapCount.text = item.scrap.toString() // Int를 String으로 변환
        holder.viewIcon.setImageResource(item.imageResId)
        holder.scrapIcon.setImageResource(item.profileImageResId)
    }

    override fun getItemCount(): Int {
        return popularCoursesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
        var tvViewCount: TextView = itemView.findViewById(R.id.tvViewCount)
        var tvScrapCount: TextView = itemView.findViewById(R.id.tvScrapCount)
        var viewIcon: ImageView = itemView.findViewById(R.id.viewIcon)
        var scrapIcon: ImageView = itemView.findViewById(R.id.scrapIcon)
    }
}
