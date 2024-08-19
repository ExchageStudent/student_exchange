package com.example.student_exchange

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.student_exchange.databinding.FragmentWriteReportStep5Binding
import com.google.android.material.bottomsheet.BottomSheetDialog

class WriteReportStep5Fragment : Fragment() {
    private lateinit var binding: FragmentWriteReportStep5Binding
    private var nextstepButton: Button? = null
    private var selectedMerits = mutableSetOf<Int>()
    private var selectedDismerits = mutableSetOf<Int>()
    private var itemIndex = 6 // 기존 항목 개수

    private val REQUEST_IMAGE_PICK = 1
    private val maxPhotos = 4
    private val photoUris = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReportStep5Binding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity
        val report = mainActivity.getReport()


        setupInitialItemSelections()

        /*
        binding.addEtcActivity.setOnClickListener {
            showAddReviewDialog()
        } */

        updateNextButtonState()

        binding.nextBtn.setOnClickListener {
            report.merits = selectedMerits.map { index -> getMeritText(index) }
            report.dismerits = selectedDismerits.map { index -> getDismeritText(index) }

            goToNextStep(report)
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

    private fun setupInitialItemSelections() {
        // Set up initial item selections with click listeners for merits
        setupItemSelection(binding.check1, binding.checkTv1, 1, isMerit = true)
        setupItemSelection(binding.check2, binding.checkTv2, 2, isMerit = true)
        setupItemSelection(binding.check3, binding.checkTv3, 3, isMerit = true)
        setupItemSelection(binding.check4, binding.checkTv4, 4, isMerit = true)
        setupItemSelection(binding.check5, binding.checkTv5, 5, isMerit = true)
        setupItemSelection(binding.check6, binding.checkTv6, 6, isMerit = true)

        // Set up initial item selections with click listeners for dismerits
        setupItemSelection(binding.dismerits1, binding.dismeritsTv1, 1, isMerit = false)
        setupItemSelection(binding.dismerits2, binding.dismeritsTv2, 2, isMerit = false)
        setupItemSelection(binding.dismerits3, binding.dismeritsTv3, 3, isMerit = false)
        setupItemSelection(binding.dismerits4, binding.dismeritsTv4, 4, isMerit = false)
        setupItemSelection(binding.dismerits5, binding.dismeritsTv5, 5, isMerit = false)
        setupItemSelection(binding.dismerits6, binding.dismeritsTv6, 6, isMerit = false)
    }

    private fun setupItemSelection(imageView: ImageView, textView: TextView, index: Int, isMerit: Boolean) {
        val itemLayout = imageView.parent as View
        itemLayout.setOnClickListener {
            if (isMerit) {
                if (selectedMerits.contains(index)) {
                    selectedMerits.remove(index)
                    updateItemState(imageView, textView, false)
                } else {
                    selectedMerits.add(index)
                    updateItemState(imageView, textView, true)
                }
            } else {
                if (selectedDismerits.contains(index)) {
                    selectedDismerits.remove(index)
                    updateItemState(imageView, textView, false)
                } else {
                    selectedDismerits.add(index)
                    updateItemState(imageView, textView, true)
                }
            }
            updateNextButtonState()
        }
    }

    private fun updateItemState(imageView: ImageView, textView: TextView, isSelected: Boolean) {
        if (isSelected) {
            imageView.setBackgroundResource(R.drawable.circle_main) // 선택된 배경 리소스 설정
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.main)) // 선택된 텍스트 색상 설정
        } else {
            imageView.setBackgroundResource(R.drawable.circle_white) // 기본 배경 리소스 설정
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray)) // 기본 텍스트 색상 설정
        }
    }

    private fun getMeritText(index: Int): String {
        return when (index) {
            1 -> "대학 생활 중 가장 보람찬 수업"
            2 -> "발표와 야외 수업 활동이 많아 만족도가 큼"
            3 -> "시험 부담이 적어 만족도가 큼"
            4 -> "영어 실력을 향상하기 위한 수업"
            5 -> "과제 많았음"
            6 -> "그 과정에서 많은 것을 배울 수 있었음"
            else -> ""
        }
    }

    private fun getDismeritText(index: Int): String {
        return when (index) {
            1 -> "대학 생활 중 가장 보람찬 수업"
            2 -> "발표와 야외 수업 활동이 많아 만족도가 큼"
            3 -> "시험 부담이 적어 만족도가 큼"
            4 -> "영어 실력을 향상하기 위한 수업"
            5 -> "과제 많았음"
            6 -> "그 과정에서 많은 것을 배울 수 있었음"
            else -> ""
        }
    }


    private fun addCustomCourseReview(reviewText: String) {
        itemIndex += 1 // 새로운 항목의 인덱스 증가

        // 새로운 항목을 담을 LinearLayout 생성
        val newItemLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // 너비는 내용물에 맞춤
                LinearLayout.LayoutParams.WRAP_CONTENT  // 높이도 내용물에 맞춤
            ).apply {
                setMargins(36.dpToPx(), 18.dpToPx(), 0, 0) // 레이아웃의 마진 설정 (왼쪽, 위, 오른쪽, 아래)
            }
            orientation = LinearLayout.HORIZONTAL // 가로 방향으로 배치
            gravity = Gravity.CENTER_VERTICAL // 내용물의 정렬 설정
        }

        // 체크 이미지를 표시할 ImageView 생성
        val newCheckImage = ImageView(context).apply {
            id = View.generateViewId() // 뷰의 고유 ID 생성
            layoutParams = LinearLayout.LayoutParams(26.dpToPx(), 26.dpToPx()).apply {
                setMargins(0, 0, 12.dpToPx(), 0) // 이미지의 마진 설정 (왼쪽, 위, 오른쪽, 아래)
            }
            setBackgroundResource(R.drawable.circle_white) // 배경 이미지 설정 (원 모양 흰색)
            setImageResource(R.drawable.ic_check) // 체크 아이콘 이미지 설정
            scaleType = ImageView.ScaleType.FIT_CENTER // 이미지 스케일 타입 설정 (중앙에 맞추기)
            setPadding(5.dpToPx()) // 이미지 내부 패딩 설정
        }

        // 텍스트를 표시할 TextView 생성
        val newTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // 너비는 내용물에 맞춤
                LinearLayout.LayoutParams.WRAP_CONTENT  // 높이도 내용물에 맞춤
            )
            gravity = Gravity.CENTER_VERTICAL // 텍스트의 정렬 설정
            text = reviewText // 텍스트 설정
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gray)) // 텍스트 색상 설정 (회색)
            textSize = 14f // 텍스트 크기 설정
        }

        // 레이아웃에 이미지와 텍스트를 추가
        newItemLayout.addView(newCheckImage)
        newItemLayout.addView(newTextView)

        // 기존 레이아웃에 새로 만든 항목 레이아웃 추가
        binding.meritsContainer.addView(newItemLayout)

        // 새 항목에 대해 클릭 리스너 설정
        setupItemSelection(newCheckImage, newTextView, itemIndex, isMerit = true)
    }

    // dp를 px로 변환하는 확장 함수
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    // 수강 과목 후기 직접 추가하기 버튼 클릭 시
    private fun showAddReviewDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_course_review, null)

        val reviewInput = dialogView.findViewById<EditText>(R.id.review_input)
        val addButton = dialogView.findViewById<Button>(R.id.button_add_review)
        val charCount = dialogView.findViewById<TextView>(R.id.num_letter)
        val resetText = dialogView.findViewById<TextView>(R.id.reset_btn)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.delete_x)

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogView)

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_add_tag_background)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        reviewInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0
                val countText = "$textLength/200"

                val spannable = SpannableStringBuilder(countText)
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.main)),
                    0, // 시작 인덱스 (0부터 시작)
                    textLength.toString().length, // 끝 인덱스 (작성된 글자수 길이만큼)
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                charCount.text = spannable
                addButton.isEnabled = textLength > 0
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        resetText.setOnClickListener {
            reviewInput.text.clear()
        }

        addButton.setOnClickListener {
            val reviewText = reviewInput.text.toString()
            addCustomCourseReview(reviewText)
            dialog.dismiss()
        }

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateNextButtonState() {
        if (selectedMerits.isNotEmpty() || selectedDismerits.isNotEmpty()) {
            binding.nextBtn.setBackgroundResource(R.drawable.report_button_next_focus)
            binding.nextBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.nextBtn.isEnabled = true
        } else {
            binding.nextBtn.setBackgroundResource(R.drawable.report_button_next_previous)
            binding.nextBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.nextBtn.isEnabled = false
        }
    }

    private fun goToNextStep(report: Report) {
        val sixthStep = WriteReportStep6Fragment()
        val bundle = Bundle()
        bundle.putParcelable("report", report) // Report 객체를 번들에 저장
        sixthStep.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, sixthStep) // R.id.main_frm은 프래그먼트를 교체할 컨테이너 ID
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
