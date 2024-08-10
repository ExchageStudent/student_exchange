package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R
import com.example.student_exchange.model.MyPageMineItem


class MyPageMineAdapter(private val items: List<MyPageMineItem>) : RecyclerView.Adapter<MyPageMineAdapter.MyPageViewHolder>() {

    class MyPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivPostImage)
        val titleView: TextView = itemView.findViewById(R.id.tvPostTitle )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mypage_mine, parent, false)
        return MyPageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyPageViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.titleView.text = item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
