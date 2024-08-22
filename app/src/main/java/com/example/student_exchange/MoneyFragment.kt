package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class MoneyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_money, container, false)

        // 'backArrow'라는 ImageView를 찾아 초기화합니다.
        val backArrow: ImageView = view.findViewById(R.id.backArrow)

        // 'backArrow' 클릭 리스너 설정
        backArrow.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        return view
    }
}
