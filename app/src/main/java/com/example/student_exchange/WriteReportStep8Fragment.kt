package com.example.student_exchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.student_exchange.adapter.ReportStageDto
import com.example.student_exchange.adapter.StageResponseDto
import com.example.student_exchange.databinding.FragmentWriteReportStep6Binding
import com.example.student_exchange.databinding.FragmentWriteReportStep8Binding
import com.example.student_exchange.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteReportStep8Fragment : Fragment() {

    private lateinit var binding: FragmentWriteReportStep8Binding

    private var reportId: Long = 0
    private lateinit var reportType: String
    private var selectedOptions = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReportStep8Binding.inflate(inflater, container, false)

        // reportId 받아오기
        reportId = arguments?.getLong("reportId") ?: 0
        reportType = arguments?.getString("reportType") ?: "DAILY"

        fetchReportStage(reportId)

        val mainActivity = activity as MainActivity
        val report = mainActivity.getReport()

        // reportType에 따라 TextView의 텍스트 설정
        binding.reportType.text = when (reportType) {
            "DAILY" -> "생활보고서"
            "INTERIM" -> "중간보고서"
            "FINAL" -> "마감보고서"
            else -> "레포트"
        }

        /*
        binding.addEtcActivity.setOnClickListener {
            showAddReviewDialog()
        } */

        binding.endBtn.setOnClickListener {
            fetchReportStage(reportId)

            // Fragment 전환 코드
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            val fragmentRecord = RecordFragment()  // fragment_record가 해당 Fragment 클래스의 이름이라 가정

            fragmentTransaction.replace(R.id.main_frm, fragmentRecord)
            fragmentTransaction.addToBackStack(null)  // 뒤로 가기를 하면 이전 프래그먼트로 돌아가기 위해 사용
            fragmentTransaction.commit()

        }

        binding.saveButton.setOnClickListener {
            SavingDialog().showSavingDialog(requireContext(), layoutInflater) {
                Log.d("Saving report", "Report saved successfully: $report")
                fetchReportStage(reportId)
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

    private fun fetchReportStage(reportId: Long) {
        Log.d("WriteReportStep8Fragment", "Fetching report with reportId: $reportId")
        val call = RetrofitInstance.reportApi.getReport(reportId)

        call.enqueue(object : Callback<List<StageResponseDto>> {
            override fun onResponse(call: Call<List<StageResponseDto>>, response: Response<List<StageResponseDto>>) {
                if (response.isSuccessful) {
                    val reportStages = response.body()
                    if (reportStages != null) {
                        displayReportData(reportStages)
                    } else {
                        Log.e("WriteReportStep8Fragment", "Received empty report stages")
                    }
                } else {
                    Log.e("WriteReportStep8Fragment", "Failed to fetch report: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<StageResponseDto>>, t: Throwable) {
                Log.e("WriteReportStep8Fragment", "API call failed: ${t.message}")
            }
        })
    }

    private fun displayReportData(reportStages: List<StageResponseDto>) {
        reportStages.forEachIndexed { index, stage ->
            when (index) {
                0 -> displayStageData(binding.step1, binding.step1Box, stage)
                1 -> displayStageData(binding.step2, binding.step2Box, stage)
                2 -> displayStageData(binding.step3, binding.step3Box, stage)
                3 -> displayStageData(binding.step4, binding.step4Box, stage)
                4 -> displayStageData(binding.step5, binding.step5Box, stage)
                5 -> displayStageData(binding.step6, binding.step6Box, stage)

                else -> Log.e("WriteReportStep8Fragment", "Unexpected stage index: $index")
            }
        }
    }

    private fun displayStageData(stepTextView: TextView, stepBox: TextView, stage: StageResponseDto) {
        // content 값을 stepTextView에 설정
        stepTextView.text = stage.content ?: "No content available"

        // 선택된 옵션들을 stepBox에 설정
        val selectedOptionsText = stage.selectedOptions?.joinToString(", ") { stage.options[it] } ?: "No options selected"
        stepBox.text = selectedOptionsText
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

    override fun onResume() {
        super.onResume()
        // 하단 네비게이션 바 숨기기
        (activity as MainActivity).hideBottomNavigation()
    }
}