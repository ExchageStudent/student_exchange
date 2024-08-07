package com.example.student_exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.student_exchange.databinding.DialogSelectTopicBinding

class TopicSelectionFragment : DialogFragment() {

    private lateinit var binding: DialogSelectTopicBinding

    override fun onCreateView(
        inflater: LayoutInflater
