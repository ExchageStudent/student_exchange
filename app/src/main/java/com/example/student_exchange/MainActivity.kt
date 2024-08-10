package com.example.student_exchange

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.student_exchange.databinding.ActivityMainBinding
import com.example.student_exchange.model.Schedule

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

        // 초기 Fragment로 TravelFragment 설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, travelFragment)
            .commitAllowingStateLoss()
    }

    fun addSchedule(schedule: Schedule) {
        scheduleList.add(schedule)
        travelFragment.updateSchedules(scheduleList)
    }

    private fun initBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.travelFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, travelFragment)
                        .commitAllowingStateLoss()
                    true
                }
                R.id.communityActivity -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    true
                }
                R.id.recordFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, RecordFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.mypageActivity -> {
                    startActivity(Intent(this, MyPageActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    fun hideBottomNavigation() {
        binding.mainBnv.visibility = View.GONE
    }
}
