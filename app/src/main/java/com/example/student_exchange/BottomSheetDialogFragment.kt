package com.example.student_exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddLocationFragment : BottomSheetDialogFragment() {
    private var etSearchCountry: EditText? = null
    private var scrollView: ScrollView? = null
    private var linearLayoutWest: LinearLayout? = null
    private var linearLayoutSouthern: LinearLayout? = null
    private var linearLayoutEast: LinearLayout? = null
    private var linearLayoutNorthern: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_location, container, false)
        etSearchCountry = view.findViewById(R.id.etSearchCountry)
        scrollView = view.findViewById(R.id.scrollView)

        // Initialize sections
        linearLayoutWest = view.findViewById(R.id.linearLayoutWest)
        linearLayoutSouthern = view.findViewById(R.id.linearLayoutSouthern)
        linearLayoutEast = view.findViewById(R.id.linearLayoutEast)
        linearLayoutNorthern = view.findViewById(R.id.linearLayoutNorthern)
        etSearchCountry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filterText(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        return view
    }

    private fun filterText(query: String) {
        if (query.isEmpty()) {
            scrollView!!.scrollTo(0, 0)
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
        for (i in 0 until layout!!.childCount) {
            val view = layout.getChildAt(i)
            if (view is TextView) {
                if (view.text.toString().contains(query)) {
                    return true
                }
            }
        }
        return false
    }

    private fun scrollToSection(section: View?) {
        val scrollY = section!!.top
        scrollView!!.smoothScrollTo(0, scrollY)
    }
}

