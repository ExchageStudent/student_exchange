package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.student_exchange.databinding.FragmentTopicSelectionBinding

class TopicSelectionFragment : DialogFragment() {
    private var _binding: FragmentTopicSelectionBinding? = null
    private val binding get() = _binding!!
    private var selectedTopic: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopicSelectionBinding.inflate(inflater, container, false)

        binding.ivcancle.setOnClickListener {
            dismiss()  // 다이얼로그를 닫습니다.
        }

        binding.ivFinish.setOnClickListener {
            selectedTopic?.let { topic ->
                // 선택된 주제를 CommunityFragment에 전달
                setFragmentResult("topicRequestKey", Bundle().apply {
                    putString("selectedTopic", topic)
                })
                dismiss()
            }
        }

        setupTopicSelection()

        return binding.root
    }

    private fun setupTopicSelection() {
        binding.tvUCLA.setOnClickListener { onTopicSelected("UCLA") }
        binding.tvKUniv.setOnClickListener { onTopicSelected("중앙대") }
        binding.tvTip.setOnClickListener { onTopicSelected("꿀팁") }
        binding.tvReview.setOnClickListener { onTopicSelected("후기") }
        binding.tvQuestion.setOnClickListener { onTopicSelected("질문") }
    }

    private fun onTopicSelected(topic: String) {
        selectedTopic = topic
        updateSelectionUI()
    }

    private fun updateSelectionUI() {
        // 모든 텍스트 뷰의 색상을 기본으로 설정
        binding.tvUCLA.setTextColor(resources.getColor(R.color.black))
        binding.tvKUniv.setTextColor(resources.getColor(R.color.black))
        binding.tvTip.setTextColor(resources.getColor(R.color.black))
        binding.tvReview.setTextColor(resources.getColor(R.color.black))
        binding.tvQuestion.setTextColor(resources.getColor(R.color.black))

        // 선택된 주제의 텍스트 색상을 변경
        when (selectedTopic) {
            "UCLA" -> binding.tvUCLA.setTextColor(resources.getColor(R.color.main))
            "중앙대" -> binding.tvKUniv.setTextColor(resources.getColor(R.color.main))
            "꿀팁" -> binding.tvTip.setTextColor(resources.getColor(R.color.main))
            "후기" -> binding.tvReview.setTextColor(resources.getColor(R.color.main))
            "질문" -> binding.tvQuestion.setTextColor(resources.getColor(R.color.main))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        // 다이얼로그의 크기를 원하는 대로 설정
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
