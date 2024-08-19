import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R

class AddLocationFragment : Fragment() {

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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        countryAdapter = CountryAdapter(emptyList()) { country ->
            selectCountry(country)
        }
        recyclerView.adapter = countryAdapter

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

        displayRecentSearches()

        selectTextView.setOnClickListener {
            goToMainWriteFragment()
        }

        return view
    }

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

    private fun displayRecentSearches() {
        val recentCountries = recentSearches.map { Country(it, "최근 검색") }
        countryAdapter.updateCountries(recentCountries)
    }

    private fun selectCountry(country: Country) {
        if (selectedCountries.contains(country)) return // 이미 선택된 경우 무시

        selectedCountries.add(country)

        // 선택된 국가를 아이콘 형태로 추가
        val countryView = LayoutInflater.from(context).inflate(R.layout.item_selected_country, selectedCountriesLayout, false)
        val countryTextView = countryView.findViewById<TextView>(R.id.tvSelectedCountry)
        countryTextView.text = country.name

        val removeButton = countryView.findViewById<TextView>(R.id.tvSelectedCountry)
        removeButton.setOnClickListener {
            selectedCountries.remove(country)
            selectedCountriesLayout.removeView(countryView)
        }

        selectedCountriesLayout.addView(countryView)
    }

    private fun goToMainWriteFragment() {
        val selectedCountryNames = selectedCountries.joinToString(", ") { it.name }
        val mainWriteFragment = parentFragmentManager.findFragmentByTag("MainWriteFragment") as? MainWriteFragment
        mainWriteFragment?.updateSelectedCountry(selectedCountryNames)
        parentFragmentManager.popBackStack()
    }
}

// Country 데이터 클래스
data class Country(val name: String, val description: String)

// CountryAdapter 클래스
class CountryAdapter(private var countries: List<Country>, private val onClick: (Country) -> Unit) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

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

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(country: Country) {
            itemView.findViewById<TextView>(R.id.tvCountryName).text = country.name
            itemView.findViewById<TextView>(R.id.tvCountryDescription).text = country.description
            itemView.setOnClickListener { onClick(country) }
        }
    }
}
