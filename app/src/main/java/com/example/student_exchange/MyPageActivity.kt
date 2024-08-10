package com.example.student_exchange

import android.os.Bundle
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.student_exchange.model.MyPageMineItem

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_page)

        // Add the MyPostsFragment only if there is no saved instance state
        if (savedInstanceState == null) {
            val fragment = MyPostsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        // Edge-to-edge handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val items = listOf(
            MyPageMineItem(R.drawable.parisimage, "7일 동안 파리 정복하기✨"),
            MyPageMineItem(R.drawable.sample_image, "Bruno 대학교 맛집 추천"),
            MyPageMineItem(R.drawable.parisimage, "7일 동안 파리 정복하기✨")
        )
        // Set up click listener for the relative layout to load MyPostsFragment
        val relativeLayout = findViewById<RelativeLayout>(R.id.mypostsSection)
        relativeLayout?.setOnClickListener {
            loadMyPostsFragment()
        }
        // "스크랩한 글" RelativeLayout에 클릭 리스너 설정
        val scrapLayout = findViewById<RelativeLayout>(R.id.tvScrap)  // 또는 R.id.btnNextScrap 사용 가능
        scrapLayout?.setOnClickListener {
            loadScrapListFragment()
        }

        val gradeLayout = findViewById<RelativeLayout>(R.id.tvGrade)
        gradeLayout?.setOnClickListener {
            loadGradeFragment()
        }

        val moneyLayout = findViewById<RelativeLayout>(R.id.tvMoney)
        moneyLayout?.setOnClickListener {
            loadMoneyFragment()
        }

        // "설정" ImageButton 클릭 리스너 설정
        val settingsButton = findViewById<ImageButton>(R.id.btnSettings)
        settingsButton?.setOnClickListener {
            loadSettingsFragment()
        }
    }

    private fun loadMyPostsFragment() {
        val fragment = MyPostsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun loadScrapListFragment() {
        val fragment = ScrapListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)  // 백 스택에 추가하여 뒤로 가기 기능 활성화
            .commit()
    }

    private fun loadGradeFragment() {
        val fragment = GradeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)  // 백 스택에 추가하여 뒤로 가기 기능 활성화
            .commit()
    }
    private fun loadMoneyFragment() {
        val fragment = MoneyFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)  // 백 스택에 추가하여 뒤로 가기 기능 활성화
            .commit()
    }

    private fun loadSettingsFragment() {
        val fragment = SettingsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // 뒤로가기 시 이전 프래그먼트로 돌아가기 위해 스택에 추가
            .commit()
    }
}
