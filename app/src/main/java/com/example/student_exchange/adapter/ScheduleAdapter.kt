package com.example.student_exchange

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.model.Schedule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ScheduleAdapter(private var schedules: List<Schedule>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scheduleName: TextView = itemView.findViewById(R.id.schedule_name)
        val scheduleTime: TextView = itemView.findViewById(R.id.schedule_time)

        fun bind(schedule: Schedule) {
            // 올바른 필드명을 사용
            scheduleName.text = schedule.scheduleName
            Log.d("ScheduleAdapter", "Schedule name: ${schedule.scheduleName}")

            // 시간 부분만 추출 및 포맷 변환 ("06:00 AM/PM")
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            val date = inputFormat.parse(schedule.startTime)
            val formattedTime = outputFormat.format(date ?: Date())

            scheduleTime.text = formattedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.bind(schedule)
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    fun updateSchedules(newSchedules: List<Schedule>) {
        schedules = newSchedules
        notifyDataSetChanged()
    }
}