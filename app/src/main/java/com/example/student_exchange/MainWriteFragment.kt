package com.example.student_exchange

import DatePickerDialogFragment
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.student_exchange.databinding.FragmentMainWriteBinding
import com.example.student_exchange.model.AppDatabase
import com.example.student_exchange.model.TravelDto
import com.example.student_exchange.model.TravelTagDto
import com.example.student_exchange.model.TravelViewModel
import com.example.student_exchange.model.UserSubject
import com.example.student_exchange.repository.TravelRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainWriteFragment : Fragment(), CustomDialogFragment.OnCountrySelectedListener, MainTopicFragment.OnTopicSelectedListener {

    private lateinit var binding: FragmentMainWriteBinding
    private lateinit var travelViewModel: TravelViewModel
    private var travelId: Long? =1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainWriteBinding.inflate(inflater, container, false)
        (activity as? RealMainActivity)?.hideBottomNavigation()

        // ViewModel 및 Repository 초기화
        val context = requireContext()
        val travelApiService = RetrofitClient.createService(TravelApiService::class.java)
        val database = AppDatabase.getDatabase(context)
        val travelDao = database.travelDao()
        val travelRepository = TravelRepository(travelApiService, travelDao)

        // ViewModel 팩토리 생성 및 초기화
        val factory = TravelViewModel.Factory(travelRepository)
        travelViewModel = ViewModelProvider(this, factory).get(TravelViewModel::class.java)

        // 수정할 게시글 ID 가져오기
        travelId = arguments?.getLong("travelId")

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (activity as? RealMainActivity)?.showBottomNavigation()
        }

        binding.ivSave.setOnClickListener {
            saveDraft()
        }

        binding.ivFinish.setOnClickListener {
            savePost()
        }

        binding.tvSelectCountry.setOnClickListener {
            val fragment = CustomDialogFragment()
            fragment.setTargetFragment(this, 0)
            fragment.show(parentFragmentManager, fragment.tag)
        }

        binding.tvSelectTopic.setOnClickListener {
            val fragment = MainTopicFragment()
            fragment.setTargetFragment(this, 0)
            fragment.show(parentFragmentManager, fragment.tag)
        }

        binding.tvSelectDate.setOnClickListener {
            val datePickerDialogFragment = DatePickerDialogFragment { selectedDateRange ->
                binding.tvSelectDate.text = selectedDateRange
                binding.tvSelectDate.setTextColor(resources.getColor(R.color.main, null))
            }
            datePickerDialogFragment.show(childFragmentManager, "datePicker")
        }

        binding.addPhotoButton.setOnClickListener {
            selectPhotoFromGallery()
        }
    }

    private fun savePost() {
        val title = binding.tvSelectTopic.text.toString()
        val content = binding.etContent.text.toString()

        val tags = listOf(
            TravelTagDto(
                id=1,
                travelPostId = travelId ?: 0,
                subject = getUserSubject(),
                travelDateStart = "2024-08-22T10:00:00",
                travelDateEnd = "2024-08-22T10:00:00",
                countryName = binding.tvSelectCountry.text.toString(),
                placeName = "tag"
            )
        )

        val currentDate = Date()  // 현재 날짜와 시간 가져오기

        val travelDto = TravelDto(
            id = travelId ?: 0,
            title = title,
            content = content,
            pageView = 0,
            likes = 0,
            createdAt = "2024-08-22T10:00:00",  // Date 객체 사용
            updatedAt = "2024-08-22T10:00:00",  // Date 객체 사용
            tags = tags
        )

        lifecycleScope.launch {
            try {
                if (travelId != null) {
                    // 기존 게시글 수정
                    travelViewModel.updateTravelPost(travelId!!, travelDto,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Post updated successfully", Toast.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.popBackStack()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(requireContext(), "Failed to update post: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    // 새 게시글 생성
                    travelViewModel.createTravelPost(travelDto,
                        onSuccess = {
                            Toast.makeText(requireContext(), "Post created successfully", Toast.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.popBackStack()
                        },
                        onError = { errorMessage ->
                            Toast.makeText(requireContext(), "Failed to create post: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun insertPhotoIntoEditor(imageUri: Uri) {
        val start = binding.etContent.selectionStart
        val spannableString = SpannableString("\n[사진]\n")
        val imageSpan = ImageSpan(requireContext(), imageUri)
        spannableString.setSpan(imageSpan, 0, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.etContent.text.insert(start, spannableString)
        binding.etContent.setSelection(start + spannableString.length)
    }

    override fun onTopicSelected(selectedTopic: String) {
        binding.tvSelectTopic.text = selectedTopic
    }

    override fun onCountrySelected(country: String) {
        binding.tvSelectCountry.text = country
    }

    private fun saveDraft() {
        // 임시 저장 로직 추가
    }

    private fun getUserSubject(): UserSubject {
        return UserSubject(userId = "123", userName = "John Doe")
    }

    companion object {
        private const val REQUEST_CODE = 100
    }
}
