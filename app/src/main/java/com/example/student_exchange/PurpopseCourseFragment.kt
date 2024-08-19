package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.adapter.CourseAdapter

class PurposeCourseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃을 인플레이트합니다.
        val view = inflater.inflate(R.id.purposeRecyclerView, container, false)

        // RecyclerView 설정
        val recyclerView = view.findViewById<RecyclerView>(R.id.filteredCoursesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CourseAdapter(getFilteredCourses())

        return view
    }

    // 필터링된 코스를 반환하는 메서드 (여기서는 더미 데이터를 사용합니다)
    private fun getFilteredCourses(): List<Course> {
        return listOf(
            Course("헬싱키, 한 템포 쉬고 싶을 때 찾는 곳", "여행지 설명 예시"),
            Course("슬로베니아, 3일 동안 추천 코스", "여행지 설명 예시")
        )
    }
}
