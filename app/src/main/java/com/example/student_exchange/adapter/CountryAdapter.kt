package com.example.student_exchange.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R
import com.example.student_exchange.model.Country

class CountryAdapter(
    private var countries: List<Country>,
    private val onClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    // ViewHolder 클래스 정의
    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val countryNameTextView: TextView = itemView.findViewById(R.id.tvCountryName)
        private val countryDescriptionTextView: TextView = itemView.findViewById(R.id.tvCountryDescription)

        // 데이터 바인딩 메서드
        fun bind(country: Country) {
            countryNameTextView.text = country.name
            countryDescriptionTextView.text = country.description

            // 항목 클릭 시 콜백 호출
            itemView.setOnClickListener {
                onClick(country)
            }
        }
    }

    // 새로운 국가 리스트로 업데이트 메서드
    fun updateCountries(newCountries: List<Country>) {
        countries = newCountries
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size
}
