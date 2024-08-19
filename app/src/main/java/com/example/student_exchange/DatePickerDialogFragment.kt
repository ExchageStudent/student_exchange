import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.student_exchange.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatePickerDialogFragment(private val onDateSelected: (String) -> Unit) : DialogFragment() {

    private lateinit var tvSelectedDateRange: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var btnConfirm: Button

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var isStartDateSelected = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dialog_main_datepicker, container, false)

        tvSelectedDateRange = view.findViewById(R.id.tvTitle)
        calendarView = view.findViewById(R.id.calendarView)
        btnConfirm = view.findViewById(R.id.ivFinish)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            if (isStartDateSelected) {
                startDate = selectedDate
                isStartDateSelected = false
                tvSelectedDateRange.text = "시작 날짜: ${formatDate(startDate!!)}\n종료 날짜 선택해주세요."
            } else {
                endDate = selectedDate
                isStartDateSelected = true
                tvSelectedDateRange.text = "선택된 날짜: ${formatDate(startDate!!)} ~ ${formatDate(endDate!!)}"
            }
        }

        btnConfirm.setOnClickListener {
            if (startDate != null && endDate != null) {
                onDateSelected("${formatDate(startDate!!)} ~ ${formatDate(endDate!!)}")
                dismiss()
            }
        }

        return view
    }

    private fun formatDate(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
