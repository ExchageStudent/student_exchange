package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.adapter.CourseAdapter
import com.example.student_exchange.adapter.TagAdapter
import com.example.student_exchange.databinding.FragmentPurposeCourseBinding
import com.example.student_exchange.model.Course
import com.example.student_exchange.model.Tag

class PurposeCourseFragment : Fragment() {

    private var _binding: FragmentPurposeCourseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurposeCourseBinding.inflate(inflater, container, false)
        val view = binding.root

        // Setup Course RecyclerView
        binding.CoursesPostRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.CoursesPostRecyclerView.adapter = CourseAdapter(getCourses())

        // Setup Tag RecyclerView
        binding.filteredCoursesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding.filteredCoursesRecyclerView.adapter = TagAdapter(getTags())

        // Setup ivBack button
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()  // or use findNavController().navigate(R.id.action_back_to_main_write) if using Navigation component
        }

        // Setup ivSave button
        binding.ivSave.setOnClickListener {
            // Implement search functionality here
            Toast.makeText(requireContext(), "Search functionality to be implemented", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun getCourses(): List<Course> {
        return listOf(
            Course("헬싱키", "한 템포 쉬고 싶을 때 찾는 곳 헬싱키로 가봤어 나는 헬싱키가 좋더라고 정말로", "user123", 120, 10),
            Course("슬로베니아", "3일 동안 추천 코스", "user456", 340, 20)
        )
    }

    private fun getTags(): List<Tag> {
        return listOf(
            Tag("전체"),
            Tag("가성비"),
            Tag("맛집"),
            Tag("역사"),
            Tag("로컬"),
            Tag("장기여행")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
