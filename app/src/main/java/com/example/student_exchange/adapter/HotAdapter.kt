package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R
import com.example.student_exchange.model.HotItem

class HotAdapter(private val items: List<HotItem>) : RecyclerView.Adapter<HotAdapter.HotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hot, parent, false)
        return HotViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
    }

    override fun getItemCount(): Int = items.size

    class HotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitleHot)
    }
}
