package com.example.student_exchange

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.student_exchange.databinding.ActivityMainBinding
import com.example.student_exchange.model.Report
import com.example.student_exchange.model.Schedule

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var travelFragment: RecordFragment
    private val scheduleList = mutableListOf<Schedule>()
    private val report = Report()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelFragment = RecordFragment()

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

    fun getReport(): Report {
        return report // Report 객체를 반환
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.realmainActivity -> {
                    startActivity(Intent(this, RealMainActivity::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.recordFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, RecordFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.communityActivity -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.mypageActivity -> {
                    startActivity(Intent(this, MyPageActivity::class.java))
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun hideBottomNavigation() {
        binding.mainBnv.visibility = View.VISIBLE
    }
}