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
import com.example.student_exchange.model.Schedule
import com.google.android.material.bottomsheet.BottomSheetDialog

class AddPlanFragment : Fragment() {

    private lateinit var binding: FragmentAddPlanBinding
    private val selectedTags = mutableSetOf<String>()

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
            val time = String.format("%d:%02d %s", hourIn12Format, selectedMinute, amPm)
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
            150,
            80
        ).apply {
            setMargins(8, 8, 8, 8)
        }
        newTagButton.layoutParams = layoutParams

        binding.tagContainer.addView(newTagButton)
    }

    private fun saveSchedule() {
        val scheduleName = binding.planName.text.toString()
        val scheduleDescription = binding.planDescription.text.toString()
        val allDay = binding.allDaySwitch.isChecked
        val startDate = binding.startDate.text.toString()
        val startTime = binding.startTime.text.toString()
        val endDate = binding.endDate.text.toString()
        val endTime = binding.endTime.text.toString()
        val repeatOption = binding.routineSpinner.selectedItem.toString()

        val newSchedule = Schedule(
            name = scheduleName,
            description = scheduleDescription,
            allDay = allDay,
            startDate = startDate,
            startTime = startTime,
            endDate = endDate,
            endTime = endTime,
            repeatOption = repeatOption
        )

        // 콘솔 로그 추가
        Log.d("AddPlanFragment", "새 일정 저장: $newSchedule")

        (activity as? MainActivity)?.addSchedule(newSchedule)

        // 일정 저장 후 TravelFragment로 전환
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, (activity as MainActivity).travelFragment)
            .commitAllowingStateLoss()
    }
}
