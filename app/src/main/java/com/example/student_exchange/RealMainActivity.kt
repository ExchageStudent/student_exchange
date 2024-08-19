package com.example.student_exchange

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.student_exchange.databinding.ActivityRealMainBinding

class RealMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityRealMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_main)

        // Implement click listener for 추천 코스
        findViewById<RelativeLayout>(R.id.purposeRecyclerView).setOnClickListener {
            val intent = Intent(this, PurpopseCourseFragment::class.java)
            startActivity(intent)
        }
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
                R.id.communityActivity -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.recordFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, RecordFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageActivity -> {
                    startActivity(Intent(this, MyPageActivity::class.java))
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    fun hideBottomNavigation() {
        binding.mainBnv.visibility = View.GONE
    }
}
