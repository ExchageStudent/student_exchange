package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R
import com.example.student_exchange.model.MyPageMineItem

// ScrapAdapter 클래스
class ScrapAdapter(private val items: List<MyPageMineItem>) : RecyclerView.Adapter<ScrapAdapter.MyViewHolder>() {

    // MyViewHolder 클래스는 ScrapAdapter 내부에 정의되어야 합니다.
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageView: ImageView = itemView.findViewById(R.id.ivPostImage)
        val itemTextView: TextView = itemView.findViewById(R.id.tvPostTitle)

        fun bind(item: MyPageMineItem) {
            itemImageView.setImageResource(item.imageResId)
            itemTextView.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mypage_mine, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}
