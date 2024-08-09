package com.example.exchange

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.exchange.databinding.DialogSaveReportBinding

class SavingDialog {

    // 저장 중 다이얼로그를 표시하고 애니메이션을 시작하는 함수
    fun showSavingDialog(context: Context, inflater: LayoutInflater, onCompletion: (() -> Unit)? = null) {
        // DataBinding을 사용하여 dialog_save_report 레이아웃을 인플레이트
        val dialogBinding = DialogSaveReportBinding.inflate(inflater)

        // 다이얼로그 생성 및 표시
        val savingDialog = AlertDialog.Builder(context, R.style.SavingDialog)
            .setView(dialogBinding.root)
            .setCancelable(false) // 사용자가 다이얼로그를 취소할 수 없도록 설정
            .create()
        savingDialog.show()

        // 다이얼로그 크기 설정
        val window = savingDialog.window
        window?.setLayout(700, 1100)

        // 로딩 애니메이션 시작
        startLoadingAnimation(dialogBinding.dot1, dialogBinding.dot2, dialogBinding.dot3, context)

        // 저장 프로세스를 시뮬레이션 (여기서는 2초 후에 완료된 것으로 가정)
        Handler(Looper.getMainLooper()).postDelayed({
            // 저장이 완료되면 다이얼로그를 닫음
            savingDialog.dismiss()

            // 완료 후 추가 작업이 필요하면 수행
            onCompletion?.invoke()
        }, 2000) // 2초 지연을 두어 시뮬레이션
    }

    // 로딩 애니메이션을 시작하는 함수
    private fun startLoadingAnimation(dot1: View, dot2: View, dot3: View, context: Context) {
        val dots = arrayOf(dot1, dot2, dot3)

        // ValueAnimator를 사용하여 0부터 2까지의 값을 애니메이션함
        val animator = ValueAnimator.ofInt(0, 2)
        animator.duration = 600 // 한 사이클(점1 -> 점3)의 총 애니메이션 시간 (600ms)
        animator.repeatCount = ValueAnimator.INFINITE // 무한 반복
        animator.addUpdateListener { animation ->
            val position = animation.animatedValue as Int
            for (i in dots.indices) {
                if (i == position) {
                    // 현재 활성화된 위치에 있는 점은 파란색으로 변경
                    dots[i].background = ContextCompat.getDrawable(context, R.drawable.circle_main)
                } else {
                    // 그 외의 점들은 회색으로 설정
                    dots[i].background = ContextCompat.getDrawable(context, R.drawable.circle_gray)
                }
            }
        }
        // 애니메이션 시작
        animator.start()
    }
}
