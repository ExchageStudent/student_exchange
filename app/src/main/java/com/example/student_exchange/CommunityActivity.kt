package com.example.student_exchange

import HotSectionFragment
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.adapter.HotAdapter
import com.example.student_exchange.databinding.ActivityCommunityBinding
import com.example.student_exchange.model.HotItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class CommunityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar
        binding.toolbarIcon0.setColorFilter(ContextCompat.getColor(this, R.color.titleblack))

        // Set up Fragments
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentHotSectionContainer, HotSectionFragment())
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentPostSectionContainer, PostSectionFragment())
                .commit()
        }

        // Toolbar Icon Click
        binding.toolbarIcon0.setOnClickListener {
            showWritePostFragment()
        }

        // Bottom Navigation setup
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
        setupRecyclerView() // Ensure RecyclerView setup is done
    }

    private fun showWritePostFragment() {
        // Hide other parts of the UI
        hideMainLayout()

        val fragment = WritePostFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentPostSectionContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentPostSectionContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun hideMainLayout() {
        binding.fragmentHotSectionContainer.visibility = View.GONE
        binding.fragmentPostSectionContainer.visibility = View.GONE
        binding.mainBnv.visibility = View.GONE
        binding.toolbar.visibility = View.GONE
        // Assuming 'fragmentContainer' should be replaced with 'fragmentPostSectionContainer'
        binding.fragmentPostSectionContainer.visibility = View.VISIBLE
    }

    private fun showMainLayout() {
        binding.fragmentHotSectionContainer.visibility = View.VISIBLE
        binding.fragmentPostSectionContainer.visibility = View.VISIBLE
        binding.mainBnv.visibility = View.VISIBLE
        binding.toolbar.visibility = View.VISIBLE
        // Assuming 'fragmentContainer' should be replaced with 'fragmentPostSectionContainer'
        binding.fragmentPostSectionContainer.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
            showMainLayout()  // Return to main layout when back is pressed
        } else {
            super.onBackPressed()
        }
    }

    // ViewPager2에 어댑터 설정
//        val adapter = MyPagerAdapter(this)
//        viewPager.adapter = adapter

//        // TabLayout과 ViewPager2 연결
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            when (position) {
//                0 -> tab.text = "전체"
//                1 -> tab.text = "UCLA"
//                2 -> tab.text = "서울여대"
//                3 -> tab.text = "꿀팁"
//                4 -> tab.text = "후기"
//            }
//        }.attach()


    private fun setupRecyclerView() {
        val items = listOf(
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer)
        )

        val hotAdapter = HotAdapter(items)
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = hotAdapter
    }

    fun hideBottomNavigation() {
        binding.mainBnv.visibility = View.GONE
    }
}