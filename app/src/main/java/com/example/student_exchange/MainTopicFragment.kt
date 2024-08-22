package com.example.student_exchange

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainTopicFragment : BottomSheetDialogFragment() {

    // Define an interface to communicate with the MainWriteFragment
    interface OnTopicSelectedListener {
        fun onTopicSelected(selectedTopic: String)
    }

    private var listener: OnTopicSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check if the host fragment (MainWriteFragment) implements the interface
        listener = targetFragment as? OnTopicSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_topic, container, false)

        // Set click listeners on each TextView for different topics
        view.findViewById<TextView>(R.id.tvCost).setOnClickListener {
            listener?.onTopicSelected("가성비")
            dismiss() // Close the dialog
        }

        view.findViewById<TextView>(R.id.tvLocal).setOnClickListener {
            listener?.onTopicSelected("로컬")
            dismiss()
        }

        view.findViewById<TextView>(R.id.tvFood).setOnClickListener {
            listener?.onTopicSelected("맛집")
            dismiss()
        }

        view.findViewById<TextView>(R.id.tvLong).setOnClickListener {
            listener?.onTopicSelected("장기여행")
            dismiss()
        }

        view.findViewById<TextView>(R.id.tvHistory).setOnClickListener {
            listener?.onTopicSelected("역사")
            dismiss()
        }

        return view
    }
}
