package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.student_exchange.R
import com.example.student_exchange.model.MyPostsItem

class ScrapAdapter(private val postList: List<MyPostsItem>) : RecyclerView.Adapter<ScrapAdapter.MyViewHolder>() {

    // ViewHolder 클래스
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image1: ImageView = itemView.findViewById(R.id.image1)
        val image2: ImageView = itemView.findViewById(R.id.image2)
        val image3: ImageView = itemView.findViewById(R.id.image3)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
        val tvViewCount: TextView = itemView.findViewById(R.id.tvViewCount)
        val tvScrapCount: TextView = itemView.findViewById(R.id.tvScrapCount)

        fun bind(myPost: MyPostsItem) {
            // Title and subtitle
            tvTitle.text = myPost.title
            tvSubtitle.text = myPost.detail

            // Image handling
            val imageUrls = myPost.imageUrls
            if (imageUrls.isNotEmpty()) {
                image1.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(imageUrls[0])
                    .into(image1)
            } else {
                image1.visibility = View.GONE
            }

            if (imageUrls.size > 1) {
                image2.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(imageUrls[1])
                    .into(image2)
            } else {
                image2.visibility = View.GONE
            }

            if (imageUrls.size > 2) {
                image3.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(imageUrls[2])
                    .into(image3)
            } else {
                image3.visibility = View.GONE
            }

            // View and scrap counts
            tvViewCount.text = myPost.viewCount.toString()
            tvScrapCount.text = myPost.scrapCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_myposts, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myPost = postList[position]
        holder.bind(myPost)
    }

    override fun getItemCount(): Int = postList.size
}
