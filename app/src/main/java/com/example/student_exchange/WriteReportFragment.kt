package com.example.student_exchange

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.student_exchange.adapter.ReportTypeDto
import com.example.student_exchange.databinding.FragmentWriteReportBinding
import com.example.student_exchange.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteReportFragment : Fragment() {

    private lateinit var binding: FragmentWriteReportBinding
    private var selectedButton: Button? = null
    private var nextstepButton : Button? = null
    private var points: Int = 50000

    private var generatedReportId: Long? = null
    private var selectedReportType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteReportBinding.inflate(inflater, container, false)

        // points 값을 직접 50000으로 설정합니다.
        Log.d("WriteReportFragment", "Points set to: $points")

        showReportDialog()

        nextstepButton = binding.nextBtn
        setupReportButtons()

        nextstepButton?.setOnClickListener {
            goToNextStep()
        }

        binding.saveButton.setOnClickListener {
            SavingDialog().showSavingDialog(requireContext(), layoutInflater) {
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

    private fun showReportDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_open_report, null)

        // Build the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // Find and set up the "취소" button
        val cancelButton: Button = dialogView.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            activity?.onBackPressed()
            dialog.dismiss()
        }

        // Find and set up the "열람" button
        val viewButton: Button = dialogView.findViewById(R.id.open_button)
        viewButton.setOnClickListener {
            if (points >= 3000) {
                val userId = 1 // userId를 1로 설정

                // createReport 메서드 호출 (POST 요청)
                RetrofitInstance.reportApi.createReport(userId).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()?.string()
                            if (responseBody != null) {
                                try {
                                    // JSON 객체에서 reportId를 추출
                                    val jsonObject = JSONObject(responseBody)
                                    val reportId = jsonObject.getLong("reportId")
                                    Log.d("ReportDialog", "보고서 생성 성공: reportId = $reportId")
                                    generatedReportId = reportId // reportId를 저장
                                } catch (e: Exception) {
                                    Log.e("ReportDialog", "JSON 파싱 오류: $e")
                                }
                            } else {
                                Log.e("ReportDialog", "서버 응답 본문이 비어 있습니다.")
                            }
                        } else {
                            Log.e("ReportDialog", "보고서 생성 실패: ${response.errorBody()?.string()}")
                        }
                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("ReportDialog", "API 호출 실패", t)
                        dialog.dismiss()
                    }
                })
            } else {
                Toast.makeText(context, "포인트가 부족하여 보고서를 열람할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
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

    private fun setupReportButtons() {
        binding.dailyReport.setOnClickListener{ handleReportButtonClick(binding.dailyReport, "DAILY") }
        binding.midReport.setOnClickListener{ handleReportButtonClick(binding.midReport, "INTERIM") }
        binding.finalReport.setOnClickListener{ handleReportButtonClick(binding.finalReport, "FINAL") }
    }

    private fun handleReportButtonClick(button: Button, reportType: String) {
        // 이미 선택된 버튼이 있는 경우 선택 해제
        selectedButton?.let {
            it.setBackgroundResource(R.drawable.report_category_default) // 기본 배경 리소스 설정
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.font)) // 기본 텍스트 색상 설정
            it.tag = false
        }

        // 선택된 버튼이 현재 버튼과 동일한 경우 선택 취소
        if (selectedButton == button) {
            selectedButton = null
            handleNextStepButton(false)
        } else {
            // 새로운 버튼 선택
            button.setBackgroundResource(R.drawable.report_category_pressed) // 선택된 배경 리소스 설정
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // 선택된 텍스트 색상 설정
            button.tag = true
            selectedButton = button
            selectedReportType = reportType
            handleNextStepButton(true)
        }
    }

    private fun handleNextStepButton(isSelected: Boolean) {
        nextstepButton?.let {
            if (isSelected) {
                it.setBackgroundResource(R.drawable.report_button_next_focus) // 선택된 배경 리소스 설정
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // 선택된 텍스트 색상 설정
                it.isEnabled = true // 버튼 활성화
            } else {
                it.setBackgroundResource(R.drawable.report_button_next_previous) // 기본 배경 리소스 설정
                it.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // 기본 텍스트 색상 설정
                it.isEnabled = false // 버튼 비활성화
            }
        }
    }

    private fun goToNextStep() {

        val reportId = generatedReportId
        val reportType = selectedReportType

        if (reportId != null && reportType != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitInstance.reportApi.sendReportType(ReportTypeDto(reportId, reportType)).execute()
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Log.d("WriteReportFragment", "ReportType 전송 성공: reportId = ${response.body()?.reportId}")
                            navigateToNextStep()
                        }
                    } else {
                        Log.e("WriteReportFragment", "ReportType 전송 실패: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("WriteReportFragment", "API 호출 실패", e)
                }
            }
        } else {
            Log.e("WriteReportFragment", "Report ID 또는 Report Type이 선택되지 않았습니다.")
        }
    }

    private fun navigateToNextStep() {
        val secondStep = WriteReportStep2Fragment()

        val bundle = Bundle()
        bundle.putLong("reportId", generatedReportId ?: 0)
        bundle.putString("reportType", selectedReportType)
        secondStep.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, secondStep) // R.id.main_frm은 프래그먼트를 교체할 컨테이너 ID
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
