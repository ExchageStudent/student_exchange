package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.student_exchange.databinding.FragmentMainWriteBinding

class FragmentMainWrite : Fragment() {

    private lateinit var binding : FragmentMainWriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainWriteBinding.inflate(inflater, container, false)

        binding.backArrow.setOnClickListener {
            navigateBack()
        }

        binding.tvSelectCountry.setOnClickListener {
            showCountrySelectDialog()
        }

        return binding.root
    }

    private fun showCountrySelectDialog() {
        val bottomSheetFragment = CountrySelectionBottomSheetFragment()
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    private fun navigateBack() {
        // 백 스택에 프래그먼트가 있는 경우 이전 프래그먼트로 이동
        if (parentFragmentManager.backStackEntryCount > 0) {
            parentFragmentManager.popBackStack()
        } else {
            // 백 스택이 없는 경우 액티비티를 종료 (필요한 경우)
            activity?.finish()
        }
    }
}