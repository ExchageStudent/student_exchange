package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.databinding.ItemHotBinding
import com.example.student_exchange.model.HotItem

class HotAdapter(private val items: List<HotItem>) :
    RecyclerView.Adapter<HotAdapter.HotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotViewHolder {
        val binding = ItemHotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class HotViewHolder(private val binding: ItemHotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HotItem) {
            binding.tvTitleHot.text = item.title
            binding.tvViews.text = item.views.toString()
            binding.viewerEye.setImageResource(item.imageResId)
        }
    }
}
