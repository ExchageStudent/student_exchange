package com.example.student_exchange

import MyPostsAdapter
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.adapter.HotAdapter
import com.example.student_exchange.adapter.PostsAdapter
import com.example.student_exchange.databinding.FragmentCommunityBinding
import com.example.student_exchange.model.HotItem
import com.example.student_exchange.model.MyPostsItem
import com.example.student_exchange.model.PostItem

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 더미 데이터 리스트 생성
        val hotItems = listOf(
            HotItem("UCLA \n 근처 맛집 추천!!", 150),
            HotItem("UCLA \n 근처 맛집 추천!!", 200),
            HotItem("UCLA \n 근처 맛집 추천!!", 50)

        )

        // PostItem 리스트 생성
        val postItems = listOf(
            PostItem(
                title = "근처 맛집 추천해요",
                details = "한 템포 쉬고 싶을 때 찾는 곳 헬싱키로 가벼워 진짜...",
                author = "jiunj123",
                date = "2일 전",
                views = 120,
                scrap = 50,
                topic = "UCLA"
            ),
            PostItem(
                title = "근처 맛집 추천해요",
                details = "한 템포 쉬고 싶을 때 찾는 곳 헬싱키로 가벼워 진짜...",
                author = "jiunj123",
                date = "2일 전",
                views = 120,
                scrap = 50,
                topic = "UCLA"
            ),
            PostItem(
                title = "근처 맛집 추천해요",
                details = "한 템포 쉬고 싶을 때 찾는 곳 헬싱키로 가벼워 진짜...",
                author = "jiunj123",
                date = "2일 전",
                views = 120,
                scrap = 50,
                topic = "UCLA"
            )
            // 추가 항목들을 이곳에 작성
        )

        val adapter = HotAdapter(hotItems)
        binding.hotItem.adapter = adapter
        binding.hotItem.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // RecyclerView 설정
        val postadapter = PostsAdapter(postItems)
        binding.totalPost.adapter = postadapter
        binding.totalPost.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // 탭 추가
        binding.tab.apply {
            addTab(newTab().setText("전체"))
            addTab(newTab().setText("UCLA"))
            addTab(newTab().setText("서울여대"))
            addTab(newTab().setText("꿀팁"))
            addTab(newTab().setText("후기"))
        }

        // 글 작성하기 버튼 클릭
        binding.writePost.setOnClickListener {
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.main_frm, WritePostFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
