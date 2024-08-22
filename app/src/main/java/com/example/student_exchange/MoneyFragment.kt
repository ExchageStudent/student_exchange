package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.student_exchange.databinding.FragmentMoneyBinding

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

        // 'tvPoints' TextView를 초기화합니다.
        val tvPoints: TextView = view.findViewById(R.id.tvPoints)

        // 'tvPoints'의 값을 가져와 정수로 변환합니다.
        val tvPointsValue = tvPoints.text.toString().replace("P", "").replace(",", "").toIntOrNull() ?: 0

//        // 'WriteReportFragment'로 이동할 때 tvPoints 값을 전달합니다.
//        val bundle = Bundle().apply {
//            putInt("points", tvPointsValue)
//        }

        val points = 10000 // tvPoints 값을 여기에 할당하세요.
        val bundle = Bundle().apply {
            putInt("points", points)
        }

        val fragment = WriteReportFragment()
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment)
            .addToBackStack(null)
            .commit()

        return view
    }


}
