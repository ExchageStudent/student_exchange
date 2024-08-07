package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R
import com.example.student_exchange.model.HotItem
import com.example.student_exchange.model.PostItem

class PostsAdapter(private val items: List<PostItem>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.details.text = item.details
        holder.author.text = item.author
        holder.date.text = item.date
        holder.views.text = "조회수: ${item.views}"
        holder.scrap.text = "스크랩수: ${item.scrap}"

    }

    override fun getItemCount(): Int = items.size

    class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val details: TextView = itemView.findViewById(R.id.tvDetails)
        val author: TextView = itemView.findViewById(R.id.tvAuthor)
        val date: TextView = itemView.findViewById(R.id.tvDate)
        val views: TextView = itemView.findViewById(R.id.tvViews)
        val scrap: TextView = itemView.findViewById(R.id.tvScrap)
    }
}
