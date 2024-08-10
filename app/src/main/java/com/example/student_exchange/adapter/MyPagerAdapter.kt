package com.example.student_exchange.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 5 // 탭의 수에 따라 다릅니다.
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
//            0 -> AllFragment()
//            1 -> UCLAFragment()
//            2 -> SWUFragment()
//            3 -> TipsFragment()
//            4 -> ReviewFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
