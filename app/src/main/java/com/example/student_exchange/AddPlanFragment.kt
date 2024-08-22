package com.example.student_exchange

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.student_exchange.databinding.FragmentAddPlanBinding
import com.example.student_exchange.model.Recurrence
import com.example.student_exchange.model.Schedule
import com.example.student_exchange.model.ScheduleRequest
import com.example.student_exchange.network.RetrofitInstance
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AddPlanFragment : Fragment() {

    private lateinit var binding: FragmentAddPlanBinding
    private val selectedTags = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlanBinding.inflate(inflater, container, false)

        binding.backArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.checkIcon.setOnClickListener {
            saveSchedule()
        }

        binding.startDate.setOnClickListener {
            showDatePickerDialog(binding.startDate)
        }

        binding.startTime.setOnClickListener {
            showTimePickerDialog(binding.startTime)
        }

        binding.endDate.setOnClickListener {
            showDatePickerDialog(binding.endDate)
        }

        binding.endTime.setOnClickListener {
            showTimePickerDialog(binding.endTime)
        }

        setupRepeatOptions()
        setupTagButtons()

        return binding.root
    }

    private fun showDatePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "${selectedMonth + 1}월 ${selectedDay}일"
            button.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(button: Button) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val amPm = if (selectedHour < 12) "AM" else "PM"
            val hourIn12Format = if (selectedHour % 12 == 0) 12 else selectedHour % 12
            val time = String.format("%d:%02d", hourIn12Format, selectedMinute, amPm)
            button.text = time
        }, hour, minute, false)

        timePickerDialog.show()
    }

    private fun setupRepeatOptions() {
        val repeatOptions = resources.getStringArray(R.array.routine_options)
        val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, repeatOptions) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.routine_spinner_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)

                // 텍스트 설정
                textView.text = repeatOptions[position]

                // 선택된 항목의 텍스트 색상을 @color/gray로 설정
                if (position == binding.routineSpinner.selectedItemPosition) {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.gray))
                } else {
                    textView.setTextColor(ContextCompat.getColor(context, R.color.routine_default_color))
                }

                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.routine_spinner_dropdown_item, parent, false)
                val textView = view.findViewById<TextView>(R.id.spinner_text)

                textView.text = repeatOptions[position]

                return view
            }
        }
        binding.routineSpinner.adapter = adapter
    }

    private fun setupTagButtons() {
        binding.tagTravel.setOnClickListener { handleTagClick(binding.tagTravel) }
        binding.tagBirthday.setOnClickListener { handleTagClick(binding.tagBirthday) }
        binding.tagParty.setOnClickListener { handleTagClick(binding.tagParty) }
        binding.tagExam.setOnClickListener { handleTagClick(binding.tagExam) }
        binding.tagDiet.setOnClickListener { handleTagClick(binding.tagDiet) }
        binding.tagStudy.setOnClickListener { handleTagClick(binding.tagStudy) }
        binding.tagAdd.setOnClickListener { showAddTagDialog() }
    }

    private fun handleTagClick(button: Button) {
        val tag = button.text.toString()
        if (selectedTags.contains(tag)) {
            selectedTags.remove(tag)
            button.setBackgroundResource(R.drawable.oval_background_20)
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.tag_default_color))
        } else {
            selectedTags.add(tag)
            button.setBackgroundResource(R.drawable.tag_selected)
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun showAddTagDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_tag, null)
        val tagNameInput = dialogView.findViewById<EditText>(R.id.tag_name_input)
        val addButton = dialogView.findViewById<Button>(R.id.button_add_tag)

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogView)

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_add_tag_background)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        tagNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addButton.isEnabled = s.toString().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        addButton.setOnClickListener {
            val tagName = tagNameInput.text.toString()
            addCustomTag(tagName)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addCustomTag(tagName: String) {
        val newTagButton = Button(context)
        newTagButton.text = tagName
        newTagButton.setBackgroundResource(R.drawable.oval_background_20)
        newTagButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        newTagButton.setOnClickListener { handleTagClick(newTagButton) }

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 8, 8, 8)
        }
        newTagButton.layoutParams = layoutParams

        binding.tagContainer.addView(newTagButton)
    }

    private fun convertToIso8601(dateString: String, timeString: String): String? {
        return try {
            // 입력된 날짜와 시간 문자열을 로그로 출력
            Log.d("AddPlanFragment", "convertToIso8601 - 입력된 날짜: $dateString, 입력된 시간: $timeString")

            // 영어 로케일을 사용하여 날짜와 시간을 파싱하기 위한 형식 설정
            val englishDateFormat = SimpleDateFormat("M월 d일 h:mm a", Locale.ENGLISH)
            Log.d("AddPlanFragment", "convertToIso8601 - 날짜 패턴: ${englishDateFormat.toPattern()}")

            // 날짜와 시간을 합쳐서 파싱
            val fullDateTimeString = "$dateString $timeString".trim()
            Log.d("AddPlanFragment", "convertToIso8601 - 파싱할 전체 날짜 문자열: $fullDateTimeString")

            val parsedDate = englishDateFormat.parse(fullDateTimeString)
            Log.d("AddPlanFragment", "convertToIso8601 - 파싱된 날짜 객체: $parsedDate")

            // Calendar를 사용하여 년도를 2024로 설정
            val calendar = Calendar.getInstance().apply {
                time = parsedDate ?: throw ParseException("Failed to parse date", 0)
                set(Calendar.YEAR, 2024)  // 년도를 2024로 고정
            }
            Log.d("AddPlanFragment", "convertToIso8601 - 캘린더 객체: $calendar")

            // ISO 8601 형식으로 변환하기 위한 형식 설정
            val iso8601DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            iso8601DateFormat.timeZone = TimeZone.getTimeZone("UTC") // UTC로 설정
            Log.d("AddPlanFragment", "convertToIso8601 - ISO 8601 패턴: ${iso8601DateFormat.toPattern()}")

            // ISO 8601 형식으로 변환 후 반환
            val iso8601String = iso8601DateFormat.format(calendar.time)
            Log.d("AddPlanFragment", "convertToIso8601 - 최종 ISO 8601 날짜 문자열: $iso8601String")

            return iso8601String
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("AddPlanFragment", "convertToIso8601 - 날짜 변환 실패: ${e.message}")
            return null
        }
    }

    private fun saveSchedule() {
        val scheduleName = binding.planName.text.toString()
        val scheduleDescription = binding.planDescription.text.toString()
        val startDate = binding.startDate.text.toString() // 예: "8월 5일"
        val startTime = binding.startTime.text.toString() // 예: "3:00 PM"
        val endDate = binding.endDate.text.toString()     // 예: "8월 5일"
        val endTime = binding.endTime.text.toString()     // 예: "4:00 PM"

        // ISO 8601 형식으로 변환
        val fullStartTime = convertToIso8601(startDate, startTime)
        val fullEndTime = convertToIso8601(endDate, endTime)

        if (fullStartTime == null || fullEndTime == null) {
            Log.e("AddPlanFragment", "날짜 변환에 실패했습니다.")
            return
        }

        // ScheduleRequest 객체 생성
        val newScheduleRequest = ScheduleRequest(
            userId = 1, // 사용자의 실제 ID로 설정
            scheduleName = scheduleName,
            scheduleDescription = scheduleDescription,
            startTime = fullStartTime,
            endTime = fullEndTime,
            tagNames = selectedTags // 사용자가 선택한 태그 리스트 사용
        )

        Log.d("AddPlanFragment", "새 일정 저장: $newScheduleRequest")

        // Retrofit을 사용한 API 호출
        RetrofitInstance.scheduleApi.saveSchedule(newScheduleRequest).enqueue(object : Callback<Map<String, Int>> {
            override fun onResponse(call: Call<Map<String, Int>>, response: Response<Map<String, Int>>) {
                if (response.isSuccessful) {
                    val scheduleId = response.body()?.get("scheduleId")
                    Log.d("AddPlanFragment", "일정 저장 성공: $scheduleId")

                    if (scheduleId != null) {
                        // RecordFragment에 전달할 Bundle 생성
                        val bundle = Bundle().apply {
                            putInt("SCHEDULE_ID", scheduleId)
                        }

                        // RecordFragment 생성 및 Bundle 전달
                        val recordFragment = RecordFragment().apply {
                            arguments = bundle
                        }

                        // 일정 저장 후 RecordFragment로 전환
                        (activity as MainActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, recordFragment)
                            .commitAllowingStateLoss()
                    }
                } else {
                    Log.e("AddPlanFragment", "일정 저장 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
                Log.e("AddPlanFragment", "API 호출 실패", t)
            }
        })
    }
}
