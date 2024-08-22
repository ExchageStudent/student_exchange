package com.example.student_exchange

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.adapter.CourseAdapter
import com.example.student_exchange.adapter.PopularCoursesAdapter
import com.example.student_exchange.adapter.PurposeAdapter
import com.example.student_exchange.databinding.ActivityRealMainBinding
import com.example.student_exchange.model.AppDatabase
import com.example.student_exchange.model.Course
import com.example.student_exchange.model.PopularCourseItem
import com.example.student_exchange.model.PurposeItem
import com.example.student_exchange.model.TravelDto
import com.example.student_exchange.model.TravelTagDto
import com.example.student_exchange.model.TravelViewModel
import com.example.student_exchange.model.UserSubject
import com.example.student_exchange.repository.TravelRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class RealMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRealMainBinding
    private lateinit var travelViewModel: TravelViewModel
    private val TAG = "RealMainActivity" // Tag for logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel and Repository
        initializeViewModel()

        // Set up Bottom Navigation
        initBottomNavigation()

        // Set up RecyclerViews
        setupRecyclerViews()

        // Set up the ImageView click listener
        findViewById<ImageView>(R.id.main_write)?.setOnClickListener {
            createAndPostTravelDto()
        }
    }

    private fun initializeViewModel() {
        val appDatabase = AppDatabase.getDatabase(this)
        val travelDao = appDatabase.travelDao()
        val travelRepository = TravelRepository(travelDao)
        val factory = TravelViewModel.Factory(travelRepository)
        travelViewModel = ViewModelProvider(this, factory).get(TravelViewModel::class.java)
    }


    private fun createAndPostTravelDto() {
        // Sample list of TravelTagDto objects
        val sampleTags = listOf(
            TravelTagDto(
                id = 1,
                travelPostId = 1, // Assuming the travel post ID should be the same as the post being created
                subject = UserSubject(userId = 1, userName= "Adventure"),
                travelDateStart = "2024-08-22T10:00:00",
                travelDateEnd = "2024-08-30T10:00:00",
                countryName = "France",
                placeName = "Paris"
            ),
            TravelTagDto(
                id = 2,
                travelPostId = 1,
                subject = UserSubject(userId = 2, userName = "Relaxation"),
                travelDateStart = "2024-09-01T10:00:00",
                travelDateEnd = "2024-09-10T10:00:00",
                countryName = "Spain",
                placeName = "Barcelona"
            ),
            TravelTagDto(
                id = 3,
                travelPostId = 1,
                subject = UserSubject(userId = 3, userName = "Sightseeing"),
                travelDateStart = "2024-09-15T10:00:00",
                travelDateEnd = "2024-09-20T10:00:00",
                countryName = "Italy",
                placeName = "Rome"
            )
        )

        // Create a TravelDto instance
        val travelDto = TravelDto(
            id = 1,
            title = "제목",
            content = "코스, 소개, 후기 등 원하는 내용을 작성해주세요",
            pageView = 0,
            likes = 0,
            createdAt = "2024-08-22T10:00:00",
            updatedAt = "2024-08-22T10:00:00",
            tags = sampleTags // Assign the list of tags here
        )

        Log.d(TAG, "Attempting to create travel post: $travelDto")

        // Call createTravelPost on the ViewModel
        travelViewModel.createTravelPost(travelDto,
            onSuccess = {
                Log.d(TAG, "Travel post created successfully")
                loadMainWriteFragment()
            },
            onError = { errorMessage ->
                Log.e(TAG, "Failed to create travel post: $errorMessage")
            }
        )
    }


    private fun setupRecyclerViews() {
        // 1. purposeRecyclerView에 item_recommend를 가로 스크롤로 표시
        binding.purposeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val purposeAdapter = PurposeAdapter(getPurposeList())
        binding.purposeRecyclerView.adapter = purposeAdapter

        // 2. coursesRecyclerView에 item_purpose를 가로 스크롤로 표시
        binding.coursesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val coursesAdapter = CourseAdapter(getCoursesList())
        binding.coursesRecyclerView.adapter = coursesAdapter

        // 3. popularCoursesRecyclerView에 item_myposts를 세로 스크롤로 표시
        binding.popularCoursesRecyclerView.layoutManager = LinearLayoutManager(this)
        val popularCoursesAdapter = PopularCoursesAdapter(getPopularCoursesList())
        binding.popularCoursesRecyclerView.adapter = popularCoursesAdapter
    }

    private fun getPurposeList(): List<PurposeItem> {
        return listOf(
            PurposeItem("Title 1", "Subtitle 1", "sedocl", 100, 10, R.drawable.purpose_image),
            PurposeItem("Title 2", "Subtitle 2", "sedocl", 200, 20, R.drawable.purpose_2)
        )
    }

    private fun getCoursesList(): List<Course> {
        return listOf(
            Course("Title 1", "Subtitle 1", "sedocl", 100, 10),
            Course("Title 2", "Subtitle 2", "sedolc", 200, 20),
            Course("Title 3", "Subtitle 3", "sedolc", 300, 30)
        )
    }

    private fun getPopularCoursesList(): List<PopularCourseItem> {
        return listOf(
            PopularCourseItem("7일 동안 파리 정복하기", "파리 6박 7일", 120, 120, R.drawable.parisimage, R.drawable.profile_image),
            PopularCourseItem("7일 동안 파리 정복하기", "파리 6박 7일", 200, 20, R.drawable.switz, R.drawable.profile_image),
            PopularCourseItem("7일 동안 파리 정복하기", "파리 6박 7일", 300, 30, R.drawable.parisimage, R.drawable.profile_image)
        )
    }

    private fun loadMainWriteFragment() {
        val fragment = MainWriteFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        hideMainLayout()
    }

    private fun initBottomNavigation() {
        // Initialize your Bottom Navigation here
        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.travelFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, RecordFragment())
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
                R.id.mypageActivity -> {
                    startActivity(Intent(this, MyPageActivity::class.java))
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    fun hideBottomNavigation() {
        findViewById<BottomNavigationView>(R.id.main_bnv)?.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.mainBnv.visibility = View.VISIBLE
    }

    fun hideMainLayout() {
        findViewById<ScrollView>(R.id.scroll)?.visibility = View.GONE
    }

    fun showMainLayout() {
        findViewById<ConstraintLayout>(R.id.realMain)?.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showMainLayout()
    }
}
