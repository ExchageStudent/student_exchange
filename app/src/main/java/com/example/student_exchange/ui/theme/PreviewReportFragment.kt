package com.example.exchange_student

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.student_exchange.MainActivity
import com.example.student_exchange.databinding.FragmentPreviewReportBinding

class PreviewReportFragment : Fragment() {
    private lateinit var binding: FragmentPreviewReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviewReportBinding.inflate(inflater, container, false)

        binding.deleteX.setOnClickListener {
            navigateBack()
        }

        return binding.root
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

    override fun onResume() {
        super.onResume()
        (this as? MainActivity)?.hideBottomNavigation()    }
}