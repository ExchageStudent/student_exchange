package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.student_exchange.databinding.FragmentBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CountrySelectionBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃을 인플레이트하여 뷰를 반환합니다.
        binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)

        // 바텀 시트 내에서 필요한 클릭 이벤트나 데이터를 설정합니다.

        return binding.root
    }
}
