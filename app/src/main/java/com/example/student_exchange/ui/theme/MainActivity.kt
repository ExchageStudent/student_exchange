package com.example.exchange

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.exchange.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.CommunityFragment
import com.example.student_exchange.HomeFragment
import com.example.student_exchange.MypageFragment
import com.example.student_exchange.RecordFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var travelFragment: TravelFragment
    private val scheduleList = mutableListOf<Schedule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelFragment = TravelFragment()

        initBottomNavigation()

        // TravelFragment로 초기화
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, travelFragment)
            .commitAllowingStateLoss()
    }

    fun addSchedule(schedule: Schedule) {
        scheduleList.add(schedule)
        travelFragment.updateSchedules(scheduleList)
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.travelFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, travelFragment)
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.communityFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, CommunityFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.recordFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, RecordFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun hideBottomNavigation() {
        binding.mainBnv.visibility = View.GONE
    }
}
