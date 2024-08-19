package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.adapter.HotAdapter
import com.example.student_exchange.databinding.FragmentCommunityBinding
import com.example.student_exchange.model.HotItem

class HotSectionFragment : Fragment() {

    private lateinit var binding: FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hotItems = listOf(
            HotItem("UCLA 근처 맛집 추천!!", 120),
            // 다른 항목 추가
        )

        val hotAdapter = HotAdapter(hotItems)
        binding.hotItem.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hotItem.adapter = hotAdapter
    }
}
