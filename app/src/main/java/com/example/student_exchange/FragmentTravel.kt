package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.student_exchange.databinding.FragmentTravelBinding
import com.example.student_exchange.databinding.FragmentWriteReportBinding

class FragmentTravel: Fragment() {

    private lateinit var binding: FragmentWriteReportBinding
    private var selectedButton: Button? = null
    private var nextstepButton : Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReportBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    // 이전 프래그먼트로 이동하는 함수
    private fun navigateBack() {
        // 백 스택에 프래그먼트가 있는 경우 이전 프래그먼트로 이동
        if (parentFragmentManager.backStackEntryCount > 0) {
            parentFragmentManager.popBackStack()
        } else {
            // 백 스택이 없는 경우 액티비티를 종료 (필요한 경우)
            activity?.finish()
        }
    }

    private fun setupReportButtons() {
        binding.dailyReport.setOnClickListener{ handleReportButtonClick(binding.dailyReport) }
        binding.midReport.setOnClickListener{ handleReportButtonClick(binding.midReport) }
        binding.finalReport.setOnClickListener{ handleReportButtonClick(binding.finalReport) }
    }

    private fun handleReportButtonClick(button: Button) {
        // 이미 선택된 버튼이 있는 경우 선택 해제
        selectedButton?.let {
            it.setBackgroundResource(R.drawable.report_category_default) // 기본 배경 리소스 설정
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.font)) // 기본 텍스트 색상 설정
            it.tag = false
        }

        // 선택된 버튼이 현재 버튼과 동일한 경우 선택 취소
        if (selectedButton == button) {
            selectedButton = null
            handleNextStepButton(false)
        } else {
            // 새로운 버튼 선택
            button.setBackgroundResource(R.drawable.report_category_pressed) // 선택된 배경 리소스 설정
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // 선택된 텍스트 색상 설정
            button.tag = true
            selectedButton = button
            handleNextStepButton(true)
        }
    }

    private fun handleNextStepButton(isSelected: Boolean) {
        nextstepButton?.let {
            if (isSelected) {
                it.setBackgroundResource(R.drawable.report_button_next_focus) // 선택된 배경 리소스 설정
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // 선택된 텍스트 색상 설정
                it.isEnabled = true // 버튼 활성화
            } else {
                it.setBackgroundResource(R.drawable.report_button_next_previous) // 기본 배경 리소스 설정
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // 기본 텍스트 색상 설정
                it.isEnabled = false // 버튼 비활성화
            }
        }
    }

    private fun goToNextStep() {
        val secondStep = WriteReportStep2Fragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, secondStep) // R.id.main_frm은 프래그먼트를 교체할 컨테이너 ID
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null) // 뒤로 가기 버튼을 통해 현재 프래그먼트로 돌아올 수 있게 함
            .commit()
    }

    override fun onResume() {
        super.onResume()
        // 하단 네비게이션 바 숨기기
        (activity as MainActivity).hideBottomNavigation()
    }
}