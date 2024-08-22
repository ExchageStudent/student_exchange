package com.example.student_exchange

import MyPostsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView // Import ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.databinding.FragmentMyPostsBinding
import com.example.student_exchange.model.MyPostsItem

class MyPostsFragment : Fragment() {

    private var _binding: FragmentMyPostsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // View Binding을 통해 프래그먼트 레이아웃을 인플레이트합니다.
        _binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 가짜 포스트 데이터를 생성합니다.
        val posts = listOf(
            MyPostsItem(
                title = "7일 동안 파리 정복하기✨",
                detail = "파리 6박 7일",
                imageUrls = listOf(
                    "app/src/main/res/drawable/germany.png",
                    "app/src/main/res/drawable/city_paris.png",
                    "app/src/main/res/drawable/switz.png"
                ),
                viewCount = 120,
                scrapCount = 120,
                tag = "UCLA"
            ),
            MyPostsItem(
                title = "헬싱키, 한 템포 쉬고 싶을 때 찾는 곳",
                detail = "헬싱키 여행 후기",
                imageUrls = listOf(
                    "https://example.com/image1.jpg"
                ),
                viewCount = 95,
                scrapCount = 45,
                tag = "UCLA"
            )
        )

        // RecyclerView 설정
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewPosts.adapter = MyPostsAdapter(posts)

        // 'backArrow'라는 ImageView를 찾아 초기화합니다.
        val backArrow: ImageView = view.findViewById(R.id.backArrow)

        // 'backArrow' 클릭 리스너 설정
        backArrow.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
