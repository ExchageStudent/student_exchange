package com.example.student_exchange

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.exchange.Report
import com.example.exchange.SavingDialog
import com.example.student_exchange.databinding.FragmentWriteReportStep6Binding

class WriteReportStep6Fragment : Fragment() {
    private lateinit var binding: FragmentWriteReportStep6Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReportStep6Binding.inflate(inflater, container, false)

        val report = arguments?.getParcelable<Report>("report")
        Log.d("WriteReportStep6", "Received Report: $report")

        report?.let {

            // 수강 과목 섹션 렌더링
            renderSection(
                title = it.subjectTitle,
                review = it.subjectReview,
                photos = it.subjectPhotos,
                container = binding.subjectContainer
            )

            // 기타 활동 섹션 렌더링
            renderSection(
                title = it.etcTitle,
                review = it.etcReview,
                photos = it.etcPhotos,
                container = binding.etcContainer
            )
        }

        binding.saveButton.setOnClickListener {
            SavingDialog().showSavingDialog(requireContext(), layoutInflater) {
                Log.d("Saving report", "Report saved successfully: $report")
            }
        }

        binding.backArrow.setOnClickListener {
            navigateBack()
        }

        binding.previousBtn.setOnClickListener {
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

    private fun renderSection(
        title: String,
        review: String,
        photos: List<Uri>,
        container: LinearLayout
    ) {
        container.removeAllViews() // 기존 뷰들을 제거

        // Title TextView 생성
        val titleView = TextView(requireContext()).apply {
            text = title
            textSize = 16f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.font))
            setPadding(0, 0, 0, 8.dpToPx())
        }
        container.addView(titleView)

        // Divider 추가
        val divider = View(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.dpToPx()
            )
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.divider))
            setPadding(0, 0, 0, 8.dpToPx())
        }
        container.addView(divider)

        // Review TextView 생성
        val reviewView = TextView(requireContext()).apply {
            text = review
            textSize = 14f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.font))
            setPadding(0, 8.dpToPx(), 0, 8.dpToPx())
        }
        container.addView(reviewView)

        // Photos 추가
        photos.forEach { uri ->
            val imageView = ImageView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    200.dpToPx() // 적절한 높이로 설정
                )
                setImageURI(uri)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setPadding(0, 8.dpToPx(), 0, 8.dpToPx())
            }
            container.addView(imageView)
        }
    }

    // dp를 px로 변환하는 함수
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).hideBottomNavigation()
    }
}


