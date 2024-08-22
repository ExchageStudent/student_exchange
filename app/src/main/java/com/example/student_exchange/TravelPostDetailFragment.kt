package com.example.student_exchange


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

class TravelPostDetailFragment : Fragment() {

    private lateinit var commentSection: LinearLayout
    private lateinit var etCommentInput: EditText
    private lateinit var ivSubmitComment: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View 초기화
        commentSection = view.findViewById(R.id.commentSection)
        etCommentInput = view.findViewById(R.id.etCommentInput)
        ivSubmitComment = view.findViewById(R.id.ivSubmitComment)

        // 댓글 제출 버튼 클릭 리스너
        ivSubmitComment.setOnClickListener {
            val comment = etCommentInput.text.toString().trim()
            if (comment.isNotEmpty()) {
                addCommentToSection(comment)
                etCommentInput.setText("") // 입력창 비우기
            }
        }
    }

    private fun addCommentToSection(comment: String) {
        val textView = TextView(context).apply {
            text = comment
            textSize = 14f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.titleblack))
            setPadding(8, 8, 8, 8)
            typeface = ResourcesCompat.getFont(requireContext(), R.font.pretendardmedium)
        }
        commentSection.addView(textView)
    }
}
