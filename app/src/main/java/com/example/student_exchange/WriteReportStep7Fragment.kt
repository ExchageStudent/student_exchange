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
import com.example.student_exchange.adapter.Base64Image
import com.example.student_exchange.adapter.ReportStageDto
import com.example.student_exchange.adapter.StageDto
import com.example.student_exchange.adapter.StageResponseDto
import com.example.student_exchange.databinding.FragmentWriteReportStep6Binding
import com.example.student_exchange.databinding.FragmentWriteReportStep7Binding
import com.example.student_exchange.model.Report
import com.example.student_exchange.network.RetrofitInstance
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteReportStep7Fragment : Fragment() {
    private lateinit var binding: FragmentWriteReportStep7Binding
    private var nextstepButton: Button? = null
    private var selectedMerits = mutableSetOf<Int>()
    private var selectedDismerits = mutableSetOf<Int>()
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
        binding = FragmentWriteReportStep7Binding.inflate(inflater, container, false)

        // reportId 받아오기
        reportId = arguments?.getLong("reportId") ?: 0
        reportType = arguments?.getString("reportType") ?: "DAILY"

        fetchReportStage(reportId, 6)

        val mainActivity = activity as MainActivity
        val report = mainActivity.getReport()


        setupInitialItemSelections()

        /*
        binding.addEtcActivity.setOnClickListener {
            showAddReviewDialog()
        } */

        updateNextButtonState()

        binding.nextBtn.setOnClickListener {
            //report.merits = selectedMerits.map { index -> getMeritText(index) }
            //report.dismerits = selectedDismerits.map { index -> getDismeritText(index) }

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
                        Log.d("WriteReportStep7", "Report stage data: $stageData")
                    } else {
                        Log.e("WriteReportStep7", "Stage data is null")
                    }
                } else {
                    Log.e("WriteReportStep7", "Failed to fetch stage: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<StageResponseDto>, t: Throwable) {
                Log.e("WriteReportStep7", "API call failed: ${t.message}", t)
            }
        })
    }

    private fun sendSelectedOptionsToServer() {
        Log.d("WriteReportStep6", "sendSelectedOptionsToServer called")

        val reportId = reportId // 실제 reportId로 교체
        val stageOrder = 6 // 현재 단계에 맞게 stageOrder를 설정
        val base64Images = listOf<Base64Image>() // 필요한 경우 이미지 데이터를 여기에 추가

        // StageDto로 변경
        val stageRequest = ReportStageDto(
            reportId = reportId,
            stageOrder = stageOrder,
            optionOrders = selectedOptions.toList(), // 선택된 옵션 인덱스를 서버로 전송
            base64Images = base64Images
        )

        Log.d("WriteReportStep7", "Stage request data: $stageRequest")

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
                binding.headerTitle.text = "학습사항"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "대부분의 과목에서 괜찮은 성적을 받았습니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "출석과 과제, 시험에 꽤 많은 정성을 쏟지 않는다면 과목에 따라 의외로 패스하지 못하는 과목들도 많습니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "생각보다 사소하고 자잘한 것까지 암기해야 하는 경우도 있었습니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "먼저 열심히 하려는 의지를 보여주는 것이 중요합니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "과제나 팀프로젝트가 많은 과목도 있어서 친구들에게 물어가며, 해결해야 하는 경우도 있습니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "교수님과 자유롭게 질문/소통이 가능하니 이를 활용하세요.")
            }
            "INTERIM" -> {
                binding.headerTitle.text = "교환학생 여가 활동"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "교통편을 알아볼 때는 omio 앱을 추천드립니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "항공권은 구글 항공권을 주로 이용합니다.")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "자유롭게 날짜와 여행지를 조정할 수 있는 만큼 한국에서 쉽게 가기 힘든 여행지들을 추천드립니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "유럽 곳곳에 예쁜 소도시들이 많으니 열심히 검색하여 색다른 여행지를 가보시기를 추천합니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "정말 다양한 축제가 열리는데 가능하다면 다양하게 참여해 보는 것을 추천합니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "학생 신분으로 혜택 주는 곳들이 꽤나 많으니 확인해보면 좋을 거 같습니다.")
            }
            "FINAL" -> {
                binding.headerTitle.text = "파견교 단점"
                setupItemSelection(binding.check1, binding.checkTv1, 1, "전공수업이 많이 없고 도시에 있고 관광지도 많기 때문에 공부에 소홀해질 가능성이 다른 곳보다 큽니다.")
                setupItemSelection(binding.check2, binding.checkTv2, 2, "교내 행사가 적어 교환학생이란 느낌을 받은 적이 거의 없어 가끔 학교에 불만을 느끼기 쉬울 것 같습니다")
                setupItemSelection(binding.check3, binding.checkTv3, 3, "업무 처리가 한국처럼 빠르지 않기에 어떠한 일이 생겼을 때 이를 해결하기 매우 어렵습니다.")
                setupItemSelection(binding.check4, binding.checkTv4, 4, "기숙사에서 식사를 제공해 주지 않기 때문에 항상 사 먹거나 만들어 먹어야 합니다.")
                setupItemSelection(binding.check5, binding.checkTv5, 5, "교내에서 진행하는 프로그램 내용은 좋으나 약간 허술한 점이 있어서 참여율이 매우 낮습니다.")
                setupItemSelection(binding.check6, binding.checkTv6, 6, "시내와 학교의 거리가 꽤 멀어, 트램을 타고 이동해야 합니다.")
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
        //setupItemSelection(newCheckImage, newTextView, itemIndex, isMerit = true)
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
        val seventhStep = WriteReportStep8Fragment()
        val bundle = Bundle()
        bundle.putParcelable("report", report) // Report 객체를 번들에 저장
        seventhStep.arguments = bundle

        bundle.putLong("reportId", reportId ?: 0)
        bundle.putString("reportType", reportType ?: "DAILY")

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, seventhStep) // R.id.main_frm은 프래그먼트를 교체할 컨테이너 ID
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
