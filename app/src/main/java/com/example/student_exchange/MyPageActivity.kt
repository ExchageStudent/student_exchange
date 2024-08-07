package com.example.student_exchange

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_page)

        // Fragment 추가 코드
        if (savedInstanceState == null) {
            val fragment = MyPostsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // 'fragment_container'는 Fragment를 담을 컨테이너의 ID입니다
                .commit()
        }

        // Edge-to-edge 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
