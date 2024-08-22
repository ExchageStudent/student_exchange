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
import com.example.student_exchange.databinding.FragmentWriteReportStep2Binding
import com.example.student_exchange.model.Report
import com.example.student_exchange.network.RetrofitInstance
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteReportStep2Fragment : Fragment() {
    private lateinit var binding: FragmentWriteReportStep2Binding
    private lateinit var savingDialog: SavingDialog
    private var nextstepButton: Button? = null
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
        binding = FragmentWriteReportStep2Binding.inflate(inflater, container, false)

        // reportId 받아오기
        reportId = arguments?.getLong("reportId") ?: 0
        reportType = arguments?.getString("reportType") ?: "DAILY"

        fetchReportStage(reportId, 1)

        val mainActivity = activity as MainActivity
        val report = mainActivity.getReport()

        setupInitialItemSelections()

        binding.addCourseReview.setOnClickListener {
            showAddReviewDialog()
        }

        nextstepButton = binding.nextBtn

        binding.addReportPhoto.setOnClickListener {
            goGallery()
        }

        updatePhotoCount()
        updateNextButtonState()

        nextstepButton = binding.nextBtn
//        nextstepButton?.setOnClickListener {
//            report.subjectTitle = binding.courseName.text.toString()
//            report.subjectReview = selectedItemsText.joinToString("\n")
//            Log.d("WriteReportStep2", "Report Data: $report")
//
//            goToNextStep(report)
//        }

        nextstepButton?.setOnClickListener {
            report.subjectReview = selectedItemsText.joinToString("\n")
            Log.d("WriteReportStep2", "Report Data: $report")
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

        binding.previewButton.setOnClickListener {
            navigateToPreviewReportFragment(report)
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

        RetrofitInstance.reportApi.getReportStage(stageDto).enqueue(object : Callback<StageResponseDto> {
            override fun onResponse(call: Call<StageResponseDto>, response: Response<StageResponseDto>) {
                if (response.isSuccessful) {
                    val stageData = response.body()
                    if (stageData != null) {
                        Log.d("WriteReportStep2", "Report stage data: $stageData")
                    } else {
                        Log.e("WriteReportStep2", "Stage data is null")
                    }
                } else {
                    Log.e("WriteReportStep2", "Failed to fetch stage: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<StageResponseDto>, t: Throwable) {
                Log.e("WriteReportStep2", "API call failed: ${t.message}", t)
            }
        })
    }



    private fun sendSelectedOptionsToServer() {
        Log.d("WriteReportStep2", "sendSelectedOptionsToServer called")

        val reportId = reportId // 실제 reportId로 교체
        val stageOrder = 1 // 현재 단계에 맞게 stageOrder를 설정
        val base64Images = listOf<Base64Image>() // 필요한 경우 이미지 데이터를 여기에 추가

        // StageDto로 변경
        val stageRequest = ReportStageDto(
            reportId = reportId,
            stageOrder = stageOrder,
            optionOrders = selectedOptions.toList(), // 선택된 옵션 인덱스를 서버로 전송
            base64Images = base64Images
        )

        Log.d("WriteReportStep2", "Stage request data: $stageRequest")

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

    // PreviewReportFragment로 이동하는 함수
    private fun navigateToPreviewReportFragment(report: Report) {
        val previewFragment = PreviewReportFragment().apply {
            arguments = Bundle().apply {
                putParcelable("report", report)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, previewFragment)
    }

    private fun setupInitialItemSelections() {
        when (reportType) {
            "DAILY" -> {
                binding.headerTitle.text = "동아리 활동"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "다양한 국적의 친구들과 어울릴 수 있었습니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "언어 능력 향상에 목적을 둔 동아리로서, 빠르게 언어 습득이 가능합니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "학기 시작 직전에 교환학생 교류 행사 1번, 단과대별 교류 이벤트를 진행합니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "학교에 운동 시설과 프로그램이 너무 잘 되어있었으며, 운동 동아리가 매우 기억에 남습니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "현지 친구들과 어울려서 진행하며, 친목을 다질 수 있었습니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "처음에는 가벼운 마음으로 동아리 활동에 임했으나 이후부터는 진심을 다해 활동하고 있는 자신을 발견할 수 있었습니다.")
            }
            "INTERIM" -> {
                binding.headerTitle.text = "수강 과목"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "대학 생활 중 가장 보람찬 수업이었습니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "발표와 야외 수업 활동이 많아 만족도가 큽니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "시험 부담이 적어 만족도가 큽니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "영어 실력을 향상하기 위한 수업이었습니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "과제 많았습니다. 많은 시간을 투자해야 했습니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "하지만 그 과정에서 많은 것을 배울 수 있었습니다.")
            }
            "FINAL" -> {
                binding.headerTitle.text = "교환학생에 관심 갖게 된 계기 및 지원동기"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "주위에 교환학생 경험이 있는 지인들을 통해 현지에서의 경험 등을 많이 들어왔었습니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "어학 실력을 향상시키고 싶었습니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "학생 신분으로 해외에 장기로 체류해 보고 싶었기 때문에 관심이 있었습니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "외국에서 생활하면서 휴학 없이 학점을 받을 수 있기에 망설임 없이 준비를 하게 되었습니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "해외에서 한 번쯤 살아보고 싶다는 생각을 가지고 있었습니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "교환학생이라는 제도가 나의 경험에 좋은 영향이 될 거라 생각하여 지원하게 되었습니다.")
            }
        }
    }
    private fun setupItemSelection(imageView: ImageView, textView: TextView, index: Int, itemText: String) {

        textView.text = itemText
        updateItemState(imageView, textView, selectedOptions.contains(index))

        val itemLayout = imageView.parent as View
        itemLayout.setOnClickListener {
            val itemLayout = imageView.parent as View
            itemLayout.setOnClickListener {
                if (selectedOptions.contains(index)) {
                    selectedOptions.remove(index)
                    updateItemState(imageView, textView, false)
                } else {
                    selectedOptions.add(index)
                    updateItemState(imageView, textView, true)
                }
                updateNextButtonState() // 항목 선택 상태 변경 시 버튼 상태 업데이트
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
        binding.courseReviewContainer.addView(newItemLayout)

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
            nextstepButton?.setBackgroundResource(R.drawable.report_button_next_focus)
            nextstepButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            nextstepButton?.isEnabled = true
        } else {
            nextstepButton?.setBackgroundResource(R.drawable.report_button_next_previous)
            nextstepButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            nextstepButton?.isEnabled = false
        }
    }

    private fun goToNextStep(report: Report) {
        Log.d("WriteReportStep2", "Passing Report to Step 3: $report")
        val thirdStep = WriteReportStep3Fragment()
        val bundle = Bundle()
        bundle.putParcelable("report", report) // Report 객체를 번들에 저장
        thirdStep.arguments = bundle

        bundle.putLong("reportId", reportId ?: 0)
        bundle.putString("reportType", reportType ?: "DAILY")

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, thirdStep) // R.id.main_frm은 프래그먼트를 교체할 컨테이너 ID
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
