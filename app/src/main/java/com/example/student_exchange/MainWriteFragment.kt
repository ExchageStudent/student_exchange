import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.student_exchange.MainTopicFragment
import com.example.student_exchange.R

class MainWriteFragment : Fragment() {

    private lateinit var ivSave: ImageView
    private lateinit var ivFinish: ImageView
    private lateinit var ivBack: ImageView
    private lateinit var tvSelectCountry: TextView
    private lateinit var tvSelectTopic: TextView
    private lateinit var tvSelectDate: TextView
    private lateinit var etContent: EditText
    private lateinit var addPhotoButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_write, container, false)
        ivSave = view.findViewById(R.id.ivSave)
        ivFinish = view.findViewById(R.id.ivFinish)
        ivBack = view.findViewById(R.id.ivBack)
        tvSelectCountry = view.findViewById(R.id.tvSelectCountry)
        tvSelectTopic = view.findViewById(R.id.tvSelectTopic)
        tvSelectDate = view.findViewById(R.id.tvSelectDate)
        etContent = view.findViewById(R.id.etContent)
        addPhotoButton = view.findViewById(R.id.addPhotoButton)

        setupListeners()
        return view
    }

    private fun setupListeners() {
        // 뒤로가기 버튼 클릭 시 MainActivity로 이동
        ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // 임시 저장 버튼 클릭 시 처리
        ivSave.setOnClickListener {
            saveDraft()
        }

        // 글 저장 버튼 클릭 시 처리
        ivFinish.setOnClickListener {
            savePost()
        }

        // 국가 선택 시 AddLocationFragment 표시
        tvSelectCountry.setOnClickListener {
            val fragment = AddLocationFragment()
            fragmentshow(parentFragmentManager, fragment.tag)
        }

        // 주제 선택 시 MainTopicFragment 팝업 표시
        tvSelectTopic.setOnClickListener {
            val fragment = MainTopicFragment()
            fragment.show(parentFragmentManager, fragment.tag)
        }

        // 날짜 선택 시 DatePickerDialogFragment 팝업 표시
        tvSelectDate.setOnClickListener {
            val datePickerDialogFragment = DatePickerDialogFragment { selectedDateRange ->
                tvSelectDate.text = selectedDateRange
                tvSelectDate.setTextColor(resources.getColor(R.color.main, null))
            }
            datePickerDialogFragment.show(childFragmentManager, "datePicker")
        }

        // 사진 첨부 버튼 클릭 시 사진 추가
        addPhotoButton.setOnClickListener {
            // 갤러리에서 사진 선택 처리
            selectPhotoFromGallery()
        }
    }

    // 임시 저장 처리
    private fun saveDraft() {
        // 임시 저장 로직 추가
    }

    // 글 저장 처리
    private fun savePost() {
        // 글 저장 로직 추가
    }

    // 사진 선택 및 추가 처리
    private fun selectPhotoFromGallery() {
        // 갤러리에서 사진을 선택하고 결과를 처리하는 로직 추가
        // 예를 들어, startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_CODE)
    }

    // 선택된 사진을 에디터에 추가하고 커서를 이동하는 메소드
    private fun insertPhotoIntoEditor(imageUri: Uri) {
        val start = etContent.selectionStart
        val spannableString = SpannableString("\n[사진]\n")
        val imageSpan = ImageSpan(requireContext(), imageUri)
        spannableString.setSpan(imageSpan, 1, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        etContent.text.insert(start, spannableString)
        etContent.setSelection(start + spannableString.length)
    }

    companion object {
        private const val REQUEST_CODE = 100
    }
}
