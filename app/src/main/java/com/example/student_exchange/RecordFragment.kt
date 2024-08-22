package com.example.student_exchange

import androidx.fragment.app.Fragment
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.databinding.FragmentRecordBinding
import com.example.student_exchange.model.Schedule
import com.example.student_exchange.network.RetrofitInstance.scheduleApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordFragment : Fragment() {

    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val scheduleList = mutableListOf<Schedule>()
    private var scheduleId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        // RecyclerView 및 어댑터 초기화
        scheduleAdapter = ScheduleAdapter(scheduleList)
        binding.recyclerView.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(context)
        }*/

        arguments?.let {
            scheduleId = it.getInt("SCHEDULE_ID", 0)
        }
        Log.d("RecordFragment", "Received scheduleId: $scheduleId")

        scheduleAdapter = ScheduleAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = scheduleAdapter

        // CalendarView 날짜 선택 리스너 설정
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // 선택된 날짜 처리 (두 자리 숫자 형식으로 맞춤)
            val formattedSelectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

            // 선택된 날짜를 기반으로 일정 API 호출
            scheduleId?.let { id ->
                scheduleApi.getSchedule(id).enqueue(object : Callback<Schedule> {
                    override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                        if (response.isSuccessful) {
                            response.body()?.let { schedule ->
                                // API 응답 데이터를 이용해 RecyclerView 업데이트
                                val scheduleDate = schedule.startTime.substring(0, 10) // "YYYY-MM-DD" 부분만 추출
                                if (scheduleDate == formattedSelectedDate) {
                                    Log.d("RecordFragment", "Schedule matches the selected date: $formattedSelectedDate")
                                    updateSchedules(listOf(schedule))
                                } else {
                                    Log.d("RecordFragment", "No schedules found for the selected date: $formattedSelectedDate")
                                    updateSchedules(emptyList())
                                }
                            }
                        } else {
                            Log.e("RecordFragment", "Failed to load schedule: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<Schedule>, t: Throwable) {
                        Log.e("RecordFragment", "Error loading schedule", t)
                    }
                })
            } ?: run {
                Log.e("RecordFragment", "scheduleId is null")
            }
        }

        // 스피너 초기화
        initSpinners()

        // planAddBtn 클릭 리스너 설정
        binding.planAddBtn.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.main_frm, AddPlanFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.writeReportTv.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.main_frm, WriteReportFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun initSpinners() {
        // 임시 데이터
        val years = (2020..2030).map { it.toString() }
        val months = (1..12).map { it.toString() }

        // 어댑터 설정
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = yearAdapter
        binding.spinnerYear.setSelection(years.indexOf(calendar.get(Calendar.YEAR).toString()))

        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = monthAdapter
        binding.spinnerMonth.setSelection(calendar.get(Calendar.MONTH))

        // 스피너 선택 리스너 (선택된 연도와 월을 기반으로 캘린더뷰 업데이트)
        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updatedCalendarView()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updatedCalendarView()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updatedCalendarView() {
        // 선택된 연도와 월을 기반으로 캘린더 날짜 업데이트
        val selectedYear = binding.spinnerYear.selectedItem.toString().toInt()
        val selectedMonth = binding.spinnerMonth.selectedItem.toString().toInt() - 1

        // CalendarView 업데이트 로직 구현
        val calendar = Calendar.getInstance()
        calendar.set(selectedYear, selectedMonth, 1)
        binding.calendarView.date = calendar.timeInMillis
    }

    fun updateSchedules(schedules: List<Schedule>) {
        scheduleAdapter.updateSchedules(schedules)
        scheduleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}