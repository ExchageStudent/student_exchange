package com.example.student_exchange

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class CustomDialogFragment : DialogFragment() {

    interface OnCountrySelectedListener {
        fun onCountrySelected(country: String)
    }

    private var listener: OnCountrySelectedListener? = null
    private var etSearchCountry: EditText? = null
    private var scrollView: ScrollView? = null
    private var linearLayoutWest: LinearLayout? = null
    private var linearLayoutSouthern: LinearLayout? = null
    private var linearLayoutEast: LinearLayout? = null
    private var linearLayoutNorthern: LinearLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as? OnCountrySelectedListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_custom, container, false)

        val countries = mapOf(
            R.id.tvNed to "네덜란드",
            R.id.tvGerman to "독일",
            R.id.tvBel to "벨기에",
            R.id.tvChe to "스위스",
            R.id.tvEng to "영국",
            R.id.tvAut to "오스트리아",
            R.id.tvIre to "아일랜드",
            R.id.tvLux to "룩셈부르크",
            R.id.tvFrance to "프랑스"
            // Add other countries similarly...
        )

        countries.forEach { (viewId, country) ->
            view.findViewById<TextView>(viewId).setOnClickListener {
                updateTextViews(view, country)
                listener?.onCountrySelected(country)
                dismiss()
            }
        }

        // Initialize views
        etSearchCountry = view.findViewById(R.id.etSearchCountry)
        scrollView = view.findViewById(R.id.scrollView)

        // Initialize sections
        linearLayoutWest = view.findViewById(R.id.linearLayoutWest)
        linearLayoutSouthern = view.findViewById(R.id.linearLayoutSouthern)
        linearLayoutEast = view.findViewById(R.id.linearLayoutEast)
        linearLayoutNorthern = view.findViewById(R.id.linearLayoutNorthern)

        // Add TextWatcher for search input
        etSearchCountry?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filterText(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        setupFinishButton(view)

        return view
    }

    private fun updateTextViews(view: View, selectedCountry: String) {
        val defaultColor = resources.getColor(android.R.color.black, null) // Default color
        val selectedColor = resources.getColor(R.color.main, null) // Selected color

        val countries = listOf(
            R.id.tvNed to "네덜란드",
            R.id.tvGerman to "독일",
            R.id.tvBel to "벨기에",
            R.id.tvChe to "스위스",
            R.id.tvEng to "영국",
            R.id.tvAut to "오스트리아",
            R.id.tvIre to "아일랜드",
            R.id.tvLux to "룩셈부르크",
            R.id.tvFrance to "프랑스"
            // Add other countries similarly...
        )

        countries.forEach { (viewId, country) ->
            val textView = view.findViewById<TextView>(viewId)
            textView.setTextColor(if (country == selectedCountry) selectedColor else defaultColor)
        }
    }

    private fun setupFinishButton(view: View) {
        val ivFinish: ImageView? = view.findViewById(R.id.ivFinish)
        ivFinish?.setOnClickListener {
            val selectedCountry = getSelectedCountry()
            listener?.onCountrySelected(selectedCountry)
            dismiss()
        }
    }

    private fun getSelectedCountry(): String {
        // Implement logic to get the selected country
        return "Selected Country" // Replace with actual selected country
    }

    private fun filterText(query: String) {
        if (query.isEmpty()) {
            scrollView?.scrollTo(0, 0)
            return
        }
        var found = false
        if (!found && containsText(linearLayoutWest, query)) {
            scrollToSection(linearLayoutWest)
            found = true
        }
        if (!found && containsText(linearLayoutSouthern, query)) {
            scrollToSection(linearLayoutSouthern)
            found = true
        }
        if (!found && containsText(linearLayoutEast, query)) {
            scrollToSection(linearLayoutEast)
            found = true
        }
        if (!found && containsText(linearLayoutNorthern, query)) {
            scrollToSection(linearLayoutNorthern)
            found = true
        }
    }

    private fun containsText(layout: LinearLayout?, query: String): Boolean {
        layout?.let {
            for (i in 0 until layout.childCount) {
                val view = layout.getChildAt(i)
                if (view is TextView) {
                    if (view.text.toString().contains(query, true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun scrollToSection(section: View?) {
        section?.let {
            val scrollY = section.top
            scrollView?.smoothScrollTo(0, scrollY)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
