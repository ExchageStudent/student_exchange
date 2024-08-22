package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.databinding.ItemPurposeBinding
import com.example.student_exchange.model.Course

class CourseAdapter(private val courseList: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemPurposeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]

        holder.binding.courseTitleTextView.text = course.title
        holder.binding.courseSubtitleTextView.text = course.details
        holder.binding.tvCommenterName1.text = course.user
        holder.binding.tvViews.text = course.viewCount.toString()
        holder.binding.tvScrap.text = course.scrapCount.toString()

        holder.binding.readMoreTextView.visibility =
            if (course.details.length > 15) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = courseList.size

    class CourseViewHolder(val binding: ItemPurposeBinding) : RecyclerView.ViewHolder(binding.root)
}
