package com.example.student_exchange

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.student_exchange.adapter.CommunityAdapter
import com.example.student_exchange.model.HotItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

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
