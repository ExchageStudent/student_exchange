package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.databinding.ItemTagBinding
import com.example.student_exchange.model.Tag

class TagAdapter(private val tagList: List<Tag>) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tagList[position]
        holder.binding.tagTextView.text = tag.name
    }

    override fun getItemCount(): Int = tagList.size

    class TagViewHolder(val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root)
}
