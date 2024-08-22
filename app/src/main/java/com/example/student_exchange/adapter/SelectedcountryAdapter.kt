package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R

class SelectedCountryAdapter(
    private val selectedCountries: List<String>
) : RecyclerView.Adapter<SelectedCountryAdapter.SelectedCountryViewHolder>() {

    class SelectedCountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSelectedCountry: TextView = itemView.findViewById(R.id.tvSelectedCountry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedCountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_country, parent, false)
        return SelectedCountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedCountryViewHolder, position: Int) {
        val selectedCountry = selectedCountries[position]
        holder.tvSelectedCountry.text = selectedCountry
    }

    override fun getItemCount(): Int = selectedCountries.size
}
