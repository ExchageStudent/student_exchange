package com.example.student_exchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.R
import com.example.student_exchange.model.Schedule

class ScheduleAdapter(private val scheduleList: MutableList<Schedule>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.schedule_name)
        val timeTextView: TextView = view.findViewById(R.id.schedule_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.nameTextView.text = schedule.name
        holder.timeTextView.text = schedule.startTime
    }

    override fun getItemCount(): Int = scheduleList.size

    // 추가된 메서드: 데이터 변경 시 리스트를 업데이트하고 알림
    fun updateSchedules(newSchedules: List<Schedule>) {
        scheduleList.clear()
        scheduleList.addAll(newSchedules)
        notifyDataSetChanged()
    }
}