package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.student_exchange.databinding.FragmentWritePostBinding
import com.example.student_exchange.model.PostItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WritePostFragment : Fragment() {

    private var _binding: FragmentWritePostBinding? = null
    private val binding get() = _binding!!
    private var selectedTopic: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWritePostBinding.inflate(inflater, container, false)

        binding.backArrow.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.ivSave.setOnClickListener {
            savePost(isFinished = false)
        }

        binding.ivFinish.setOnClickListener {
            savePost(isFinished = true)
        }

        binding.tvSelectTopic.setOnClickListener {
            val dialog = TopicSelectionFragment()
            dialog.show(parentFragmentManager, "SelectTopicDialog")
        }

        // TopicSelectionFragment에서 선택된 주제를 수신
        setFragmentResultListener("topicRequestKey") { _, bundle ->
            selectedTopic = bundle.getString("selectedTopic")
            // 선택된 주제를 tvSelectTopic에 반영
            binding.tvSelectTopic.text = selectedTopic ?: "주제를 선택해주세요"
        }

        return binding.root
    }

    private fun savePost(isFinished: Boolean) {
        val title = binding.etTitle.text.toString()
        val details = binding.etContent.text.toString()
        val author = "작성자" // 서버에서 사용자 id나 닉네임 받아오는 코드로 수정해야 함
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val views = 0
        val scrap = 0

        if (title.isBlank() || details.isBlank()) {
            Toast.makeText(context, "제목과 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedTopic.isNullOrEmpty()) {
            Toast.makeText(context, "주제를 선택하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val postItem = PostItem(
            title = title,
            details = details,
            author = author,
            date = date,
            views = views,
            scrap = scrap,
            isSaved = !isFinished,
            isFinished = isFinished,
            topic = selectedTopic!!  // 선택된 주제를 포함
        )

        // TODO: 이곳에서 postItem을 로컬 저장소, 데이터베이스 또는 서버에 저장하는 로직을 추가하세요.
        // 예: saveToLocalDatabase(postItem)

        Toast.makeText(context, if (isFinished) "글이 완료되었습니다." else "글이 임시 저장되었습니다.", Toast.LENGTH_SHORT).show()

        // 작성 완료 후
        activity?.onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
