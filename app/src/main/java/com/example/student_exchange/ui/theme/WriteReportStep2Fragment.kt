package com.example.exchange

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
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
import com.example.exchange.databinding.FragmentWriteReportStep2Binding
import com.google.android.material.bottomsheet.BottomSheetDialog

class WriteReportStep2Fragment : Fragment() {
    private lateinit var binding: FragmentWriteReportStep2Binding
    private var nextstepButton: Button? = null
    private var selectedItems = mutableSetOf<Int>()
    private var itemIndex = 6 // 기존 항목 개수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReportStep2Binding.inflate(inflater, container, false)

        setupInitialItemSelections()

        // addCourseReview 버튼 클릭 시 다이얼로그를 표시하도록 설정
        binding.addCourseReview.setOnClickListener {
            showAddReviewDialog()
        }

        nextstepButton = binding.nextBtn

        return binding.root
    }

    private fun setupInitialItemSelections() {
        // Set up initial item selections with click listeners
        setupItemSelection(binding.check1, binding.checkTv1, 1)
        setupItemSelection(binding.check2, binding.checkTv2, 2)
        setupItemSelection(binding.check3, binding.checkTv3, 3)
        setupItemSelection(binding.check4, binding.checkTv4, 4)
        setupItemSelection(binding.check5, binding.checkTv5, 5)
        setupItemSelection(binding.check6, binding.checkTv6, 6)
    }

    private fun setupItemSelection(imageView: ImageView, textView: TextView, index: Int) {
        val itemLayout = imageView.parent as View
        itemLayout.setOnClickListener {
            if (selectedItems.contains(index)) {
                selectedItems.remove(index)
                updateItemState(imageView, textView, false)
            } else {
                selectedItems.add(index)
                updateItemState(imageView, textView, true)
            }
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

    private fun addCustomCourseReview(reviewText: String) {
        itemIndex += 1 // 새로운 항목의 인덱스 증가

        val newItemLayout = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(36, 18, 0, 0) // 마진 설정
            }
            orientation = LinearLayout.HORIZONTAL
            gravity = View.TEXT_ALIGNMENT_CENTER
        }

        val newCheckImage = ImageView(context).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(26, 26).apply {
                setMargins(0, 0, 12, 0) // 마진 설정
            }
            setBackgroundResource(R.drawable.circle_white)
            setImageResource(R.drawable.ic_check)
            scaleType = ImageView.ScaleType.FIT_CENTER
            setPadding(5)
        }

        val newTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = View.TEXT_ALIGNMENT_CENTER
            text = reviewText
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            textSize = 14f
        }

        newItemLayout.addView(newCheckImage)
        newItemLayout.addView(newTextView)

        binding.container.addView(newItemLayout)

        setupItemSelection(newCheckImage, newTextView, itemIndex)
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

    override fun onResume() {
        super.onResume()
        // 하단 네비게이션 바 숨기기
        (activity as MainActivity).hideBottomNavigation()
    }
}
