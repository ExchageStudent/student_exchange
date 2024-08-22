package com.example.student_exchange

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.student_exchange.adapter.Base64Image
import com.example.student_exchange.adapter.ReportStageDto
import com.example.student_exchange.adapter.StageDto
import com.example.student_exchange.adapter.StageResponseDto
import com.example.student_exchange.databinding.FragmentWriteReportStep4Binding
import com.example.student_exchange.model.Report
import com.example.student_exchange.network.RetrofitInstance
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WriteReportStep4Fragment : Fragment() {
    private lateinit var binding: FragmentWriteReportStep4Binding
    private var selectedItemsText = mutableSetOf<String>()
    private var itemIndex = 6 // 기존 항목 개수
    private val REQUEST_IMAGE_PICK = 1
    private val maxPhotos = 4
    private val photoUris = mutableListOf<Uri>()

    private var reportId: Long = 0
    private lateinit var reportType: String
    private var selectedOptions = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReportStep4Binding.inflate(inflater, container, false)

        // reportId 받아오기
        reportId = arguments?.getLong("reportId") ?: 0
        reportType = arguments?.getString("reportType") ?: "DAILY"
        fetchReportStage(reportId, 3)

        val mainActivity = activity as MainActivity
        val report = mainActivity.getReport()

        setupInitialItemSelections()

        // addCourseReview 버튼 클릭 시 다이얼로그를 표시하도록 설정
        binding.addEtcActivity.setOnClickListener {
            showAddReviewDialog()
        }

        binding.addReportPhoto.setOnClickListener {
            goGallery()
        }

        updatePhotoCount()
        updateNextButtonState()

        binding.nextBtn.setOnClickListener {
            report.gainReview = selectedItemsText.joinToString("\n")
            Log.d("WriteReportStep4", "Report Data: $report")
            sendSelectedOptionsToServer()
            goToNextStep(report)
        }

        binding.saveButton.setOnClickListener {
            SavingDialog().showSavingDialog(requireContext(), layoutInflater) {
                Log.d("Saving report", "Report saved successfully: $report")
                sendSelectedOptionsToServer()
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

    // 보고서 단계를 서버에서 가져오는 함수
    private fun fetchReportStage(reportId: Long, stageOrder: Int) {
        Log.d("fetchReportStage", "Fetching report stage with reportId: $reportId and stageOrder: $stageOrder")

        // StageDto 객체 생성
        val stageDto = StageDto(
            reportId = reportId,
            stageOrder = stageOrder
        )

        RetrofitInstance.reportApi.getReportStage(stageDto).enqueue(object :
            Callback<StageResponseDto> {
            override fun onResponse(call: Call<StageResponseDto>, response: Response<StageResponseDto>) {
                if (response.isSuccessful) {
                    val stageData = response.body()
                    if (stageData != null) {
                        Log.d("WriteReportStep4", "Report stage data: $stageData")
                    } else {
                        Log.e("WriteReportStep4", "Stage data is null")
                    }
                } else {
                    Log.e("WriteReportStep4", "Failed to fetch stage: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<StageResponseDto>, t: Throwable) {
                Log.e("WriteReportStep4", "API call failed: ${t.message}", t)
            }
        })
    }

    private fun sendSelectedOptionsToServer() {
        Log.d("WriteReportStep4", "sendSelectedOptionsToServer called")

        val reportId = reportId // 실제 reportId로 교체
        val stageOrder = 3 // 현재 단계에 맞게 stageOrder를 설정
        val base64Images = listOf<Base64Image>() // 필요한 경우 이미지 데이터를 여기에 추가

        // StageDto로 변경
        val stageRequest = ReportStageDto(
            reportId = reportId,
            stageOrder = stageOrder,
            optionOrders = selectedOptions.toList(), // 선택된 옵션 인덱스를 서버로 전송
            base64Images = base64Images
        )

        Log.d("WriteReportStep4", "Stage request data: $stageRequest")

        RetrofitInstance.reportApi.updateReportStage(stageRequest).enqueue(object : Callback<StageResponseDto> {
            override fun onResponse(call: Call<StageResponseDto>, response: Response<StageResponseDto>) {
                if (response.isSuccessful) {
                    Log.d("StageUpdate", "Stage update successful")
                } else {
                    Log.e("StageUpdate", "Stage update failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<StageResponseDto>, t: Throwable) {
                Log.e("StageUpdate", "API call failed: ${t.message}", t)
            }
        })
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
        when (reportType) {
            "DAILY" -> {
                binding.headerTitle.text = "교환 프로그램"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "교환학생 프로그램인 ESN에서 매주 수요일마다 진행되었습니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "다양한 교환학생을 만날 수 있었습니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "학기 시작 전부터 학기가 시작한 후 2~3달 동안 새로운 버디가 지속적으로 나타날 수 있습니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "상호언어교환 프로그램이 존재하며, 파견 대학 내에서 자체적으로 학생들의 수요를 받아 연결해 주었습니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "서로의 문화를 교류하는 좋은 시간이었습니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "국제교류처 담당자분께서 매우 친절하셨습니다.")
            }

            "INTERIM" -> {
                binding.headerTitle.text = "언어 습득"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "교환학생이 많이 없을수록, 교환학생에 대한 배려가 전혀 없는 과목도 많이 있습니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "현지인들과 똑같이 수업을 듣는 데에 어려움이 있을 수 있습니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "커뮤니케이션 과목 같은 경우에는 영어나 해당 국가의 언어로 독해, 말하기, 글쓰기가 가능하신 분들에게 추천 드립니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "언어를 배우다 보면 어느 정도 규칙이 있어서 생활하고 그 규칙을 터득하다 보면 스스로 이해가 가능해집니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "기본적인 회화는 어떤 방식으로든 익히고 오시는 걸 추천드립니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "실제 사용하는 은어 같은 것들을 배우고 싶다면 해당 국가의 드라마를 보시는 걸 추천드립니다.")
            }

            "FINAL" -> {
                binding.headerTitle.text = "수업 및 학사 관련 사항 (수강과목 포함)"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "1학기에는 최대 5과목 수강이 가능합니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "조금 더 자유로운 분위기에서 토론이나 질문을 할 수 있습니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "과제가 많으나 배우는 것도 많이 있습니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "내용은 쉬우나 시험이 조금 까다롭습니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "PPT를 만들어 내기만 하면 성적을 굉장히 잘 주십니다. 아시아계 학생들이 많습니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, " 이론과 토론 수업으로 진행됩니다. 문화 관련 토론이 많아 서로의 문화를 배울 수 있고 흥미롭습니다.")
            }
        }
    }

    private fun setupItemSelection(imageView: ImageView, textView: TextView, index: Int, itemText: String) {

        textView.text = itemText
        updateItemState(imageView, textView, selectedOptions.contains(index))

        val itemLayout = imageView.parent as View
        itemLayout.setOnClickListener {
            if (selectedOptions.contains(index)) {
                selectedOptions.remove(index)
                selectedOptions.remove(index)
                updateItemState(imageView, textView, false)
            } else {
                selectedOptions.add(index)
                updateItemState(imageView, textView, true)
                selectedOptions.add(index)
            }
            updateNextButtonState() // 항목 선택 상태 변경 시 버튼 상태 업데이트
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
        binding.gainActivityContainer.addView(newItemLayout)

        // 새 항목에 대해 클릭 리스너 설정
        setupItemSelection(newCheckImage, newTextView, itemIndex, reviewText)
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

    // 사진 첨부하기
    // 갤러리로 이동하기
    private fun goGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*" // 선택한 파일의 종류를 지정 (이미지만 지정)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val bitmap = getBitmapFromUri(uri)
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false)
                photoUris.add(uri)
                addImageView(resizedBitmap)
                updatePhotoCount()
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun addImageView(bitmap: Bitmap) {
        val imageView = ImageView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(100.dpToPx(), 100.dpToPx())
            setImageBitmap(bitmap)
        }
        binding.photoPreviewContainer.addView(imageView)
    }

    // 사진 개수 업데이트
    private fun updatePhotoCount() {
        val photoCountText = "${photoUris.size}/$maxPhotos"
        binding.photoCount.text = photoCountText
        if (photoUris.size == maxPhotos) {
            binding.photoCount.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            binding.addReportPhoto.isClickable = false
        } else {
            binding.photoCount.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            binding.addReportPhoto.isClickable = true
        }
    }

    private fun updateNextButtonState() {
        if (selectedOptions.isNotEmpty()) {
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
        Log.d("WriteReportStep4", "Passing Report to Step 5: $report")
        val fifthStep = WriteReportStep5Fragment()
        val bundle = Bundle()
        bundle.putParcelable("report", report) // Report 객체를 번들에 저장
        fifthStep.arguments = bundle

        bundle.putLong("reportId", reportId ?: 0)
        bundle.putString("reportType", reportType ?: "DAILY")

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fifthStep)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        // 하단 네비게이션 바 숨기기
        (activity as MainActivity).hideBottomNavigation()
    }
}

