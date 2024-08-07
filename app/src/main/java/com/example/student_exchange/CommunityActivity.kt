package com.example.student_exchange

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class CommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        if (savedInstanceState == null) {
            // Hot Section Fragment 추가
            val hotFragmentContainer = findViewById<FrameLayout>(R.id.fragmentHotSectionContainer)
            supportFragmentManager.beginTransaction()
                .replace(hotFragmentContainer.id, HotSectionFragment())
                .commit()

            // Post Section Fragment 추가
            val postFragmentContainer = findViewById<FrameLayout>(R.id.fragmentPostSectionContainer)
            supportFragmentManager.beginTransaction()
                .replace(postFragmentContainer.id, PostSectionFragment())
                .commit()
        }
    }
}
