package com.example.student_exchange

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_exchange.adapter.MyPageMineAdapter
import com.example.student_exchange.model.MyPageMineItem

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_page)

        val myPageMineItems = listOf(
            MyPageMineItem(R.drawable.parisimage, "7일 동안 파리\n정복하기"),
            MyPageMineItem(R.drawable.sample_image, "헬싱키,한 템포\n쉬고 싶을 때 찾는 곳"),

        )

        val recyclerViewMyPosts = findViewById<RecyclerView>(R.id.rvMyPosts)
        recyclerViewMyPosts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewMyPosts.adapter = MyPageMineAdapter(myPageMineItems)

        val recyclerViewScrapedPosts = findViewById<RecyclerView>(R.id.rvScrapedPosts)
        recyclerViewScrapedPosts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewScrapedPosts.adapter = MyPageMineAdapter(myPageMineItems)

        // Edge-to-edge handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up click listeners for sections
        findViewById<RelativeLayout>(R.id.mypostsSection)?.setOnClickListener {
            loadMyPostsFragment()
        }

        findViewById<RelativeLayout>(R.id.tvScrap)?.setOnClickListener {
            loadScrapListFragment()
        }

        findViewById<RelativeLayout>(R.id.tvGrade)?.setOnClickListener {
            loadGradeFragment()
        }

        findViewById<RelativeLayout>(R.id.tvMoney)?.setOnClickListener {
            loadMoneyFragment()
        }

        findViewById<ImageButton>(R.id.btnSettings)?.setOnClickListener {
            loadSettingsFragment()
        }
    }

    private fun loadMyPostsFragment() {
        val fragment = MyPostsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        hideMainLayout()
    }

    private fun loadScrapListFragment() {
        val fragment = ScrapListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        hideMainLayout()
    }

    private fun loadGradeFragment() {
        val fragment = GradeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        hideMainLayout()
    }

    private fun loadMoneyFragment() {
        val fragment = MoneyFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        hideMainLayout()
    }

    private fun loadSettingsFragment() {
        val fragment = SettingsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        hideMainLayout()
    }

    private fun hideMainLayout() {
        findViewById<ScrollView>(R.id.scrollView)?.visibility = View.GONE
    }

    private fun showMainLayout() {
        findViewById<ScrollView>(R.id.scrollView)?.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showMainLayout()  // Return to main layout when back is pressed
    }
}
