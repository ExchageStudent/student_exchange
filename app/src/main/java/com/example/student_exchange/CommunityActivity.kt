package com.example.student_exchange

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.student_exchange.adapter.CommunityAdapter
import com.example.student_exchange.model.HotItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)


        val imageView: ImageView = findViewById(R.id.toolbarIcon0)
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.titleblack))

        // Fragment 추가
        if (savedInstanceState == null) {
            val hotFragmentContainer = findViewById<FrameLayout>(R.id.fragmentHotSectionContainer)
            supportFragmentManager.beginTransaction()
                .replace(hotFragmentContainer.id, HotSectionFragment())
                .commit()

            val postFragmentContainer = findViewById<FrameLayout>(R.id.fragmentPostSectionContainer)
            supportFragmentManager.beginTransaction()
                .replace(postFragmentContainer.id, PostSectionFragment())
                .commit()
        }
        // toolbarIcon0 클릭 시 WritePostFragment로 이동
        val toolbarIcon: ImageView = findViewById(R.id.toolbarIcon0)  // 수정: view가 아닌 findViewById 사용
        toolbarIcon.setOnClickListener {
            // WritePostFragment로 이동
            val transaction = supportFragmentManager.beginTransaction() // 수정: parentFragmentManager가 아닌 supportFragmentManager 사용
            transaction.replace(R.id.fragment_container, WritePostFragment()) // 수정: R.id.fragment_container를 적절한 컨테이너 ID로 변경
            transaction.addToBackStack(null) // 뒤로가기 시 이전 프래그먼트로 돌아가기 위해 스택에 추가
            transaction.commit()
        }


        // TabLayout과 ViewPager2 설정
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

//        viewPager.adapter = CommunityAdapter(items = List(HotItem))

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "전체"
                1 -> tab.text = "UCLA"
                2 -> tab.text = "서울여대"
                3 -> tab.text = "꿀팁"
                4 -> tab.text = "후기"
            }
        }.attach()
    }
}
