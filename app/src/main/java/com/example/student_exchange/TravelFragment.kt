package com.example.student_exchange

import androidx.fragment.app.Fragment
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.Toast

class TravelFragment : Fragment() {
    private lateinit var calendarView : CalendarView
    private lateinit var spinnerYear : Spinner
    private lateinit var spinnerMonth : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_travel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CalendarView 초기화
        calendarView = view.findViewById(R.id.calendarView)
        spinnerYear = view.findViewById(R.id.spinner_year)
        spinnerMonth = view.findViewById(R.id.spinner_month)

        // CalendarView 날짜 선택 리스너 설정
        calendarView.setOnDateChangeListener { view, year, month, dayofMonth ->
            // 선택된 날짜 처리
            val selectedDate = "$dayofMonth/${month + 1}/$year"
            Toast.makeText(context, "Selected Date: $selectedDate",Toast.LENGTH_SHORT).show()
        }

        // 스피너 초기화
        initSpinners(view)
    }

    private fun initSpinners(view: View) {
        // 임시 데이터
        val years = (2020..2030).map{it.toString()}
        val months = (1..12).map {it.toString()}

        // 어댑터 설정
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = yearAdapter

        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter

        // 스피너 선택 리스너 (선택된 연도와 월을 기반으로 캘린더뷰 업데이트)
        spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updatedCalenderView()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updatedCalenderView()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updatedCalenderView() {
        // 선택된 연도와 월을 기반으로 캘린더 날짜 업데이트
        val selectedYear = spinnerYear.selectedItem.toString().toInt()
        val selectedMonth = spinnerMonth.selectedItem.toString().toInt()

        // CalendarView 업데이트 로직 구현
        val calendar = Calendar.getInstance()
        calendar.set(selectedYear, selectedMonth-1, 1)
        calendarView.date = calendar.timeInMillis
    }

    fun updateSchedules(scheduleList: Any) {

    }
}