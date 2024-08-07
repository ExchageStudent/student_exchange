package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.adapter.PostsAdapter
import com.example.student_exchange.databinding.FragmentItemListBinding
import com.example.student_exchange.model.PostItem

class PostSectionFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postItems = listOf(
            PostItem("UCLA 근처 맛집 추천해요", "한 템포 쉬고 싶을 때 찾는 곳 헬싱키로 가벼워 진짜...", "jlunj123", "2일전", 120, 120),
            // 더 많은 항목 추가
        )

        val postsAdapter = PostsAdapter(postItems)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = postsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
