package com.example.student_exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.adapter.CountryAdapter
import com.example.student_exchange.model.Country

class AddLocationFragment : DialogFragment() {

    private lateinit var countryAdapter: CountryAdapter
    private var selectedCountries = mutableListOf<Country>()
    private lateinit var selectedCountriesLayout: LinearLayout
    private var recentSearches = listOf("프랑스", "독일", "일본") // 최근 검색어 예시

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_location, container, false)

        val searchEditText = view.findViewById<EditText>(R.id.etSearchLocation)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewLocations)
        val selectTextView = view.findViewById<TextView>(R.id.btnSelectCountry)
        selectedCountriesLayout = view.findViewById(R.id.selectedCountriesLayout)

        // RecyclerView와 어댑터 설정
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        countryAdapter = CountryAdapter(emptyList()) { country ->
            selectCountry(country)
        }
        recyclerView.adapter = countryAdapter

        // 검색 EditText에 텍스트 변화 감지
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                if (searchText.isEmpty()) {
                    displayRecentSearches()
                } else {
                    filterCountries(searchText)
                }
            }
        })

        // 초기에는 최근 검색어 표시
        displayRecentSearches()

        // 선택 완료 버튼 클릭 시 처리
        selectTextView.setOnClickListener {
            goToMainWriteFragment()
        }

        return view
    }

    // 검색 필터링 로직
    private fun filterCountries(query: String) {
        val allCountries = listOf(
            Country("프랑스", "파리, 음식점"),
            Country("독일", "베를린, 음식점"),
            Country("일본", "도쿄, 음식점"),
            Country("스페인", "마드리드, 음식점")
        )
        val filteredCountries = allCountries.filter { it.name.contains(query) }
        countryAdapter.updateCountries(filteredCountries)
    }

    // 최근 검색어 표시
    private fun displayRecentSearches() {
        val recentCountries = recentSearches.map { Country(it, "최근 검색") }
        countryAdapter.updateCountries(recentCountries)
    }

    // 선택한 국가를 추가하는 함수
    private fun selectCountry(country: Country) {
        if (selectedCountries.contains(country)) return // 이미 선택된 경우 무시

        selectedCountries.add(country)

        // 선택된 국가를 레이아웃에 추가
        val countryView = LayoutInflater.from(context).inflate(R.layout.item_selected_country, selectedCountriesLayout, false)
        val countryTextView = countryView.findViewById<TextView>(R.id.tvSelectedCountry)
        countryTextView.text = country.name

        // 국가 제거 버튼
        val removeButton = countryView.findViewById<TextView>(R.id.tvSelectedCountry)
        removeButton.setOnClickListener {
            selectedCountries.remove(country)
            selectedCountriesLayout.removeView(countryView)
        }

        selectedCountriesLayout.addView(countryView)
    }

    // MainWriteFragment로 선택한 국가 전송하는 함수
    private fun goToMainWriteFragment() {
        val mainWriteFragment = targetFragment as? MainWriteFragment
        mainWriteFragment?.updateSelectedCountry(selectedCountries.joinToString(", ") { it.name })
        dismiss()
    }
}
