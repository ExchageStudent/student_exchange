import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_exchange.R
import com.example.student_exchange.databinding.FragmentHotSectionBinding
import com.example.student_exchange.model.HotItem
import com.example.student_exchange.adapter.HotAdapter // Ensure this import is correct

class HotSectionFragment : Fragment() {

    private var _binding: FragmentHotSectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHotSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val items = listOf(
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer),
            HotItem("UCLA 근처 맛집 주천!!", 120, R.drawable.eye_viewer)
        )

        val adapter = HotAdapter(items)
        binding.fragmentHotSectionContainer.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        binding.fragmentHotSectionContainer.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}