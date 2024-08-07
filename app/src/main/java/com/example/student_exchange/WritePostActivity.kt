package com.example.student_exchange

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.student_exchange.databinding.ActivityWritePostBinding

class WritePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWritePostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivSave.setOnClickListener {
            // Save the post
        }

        binding.tvSelectTopic.setOnClickListener {
            showTopicSelectionDialog()
        }
    }

    private fun showTopicSelectionDialog() {
        val dialog = TopicSelectionDialogFragment()
        dialog.show(supportFragmentManager, "TopicSelectionDialogFragment")
    }
}
