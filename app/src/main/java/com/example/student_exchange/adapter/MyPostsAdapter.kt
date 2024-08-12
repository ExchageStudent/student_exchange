import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.student_exchange.R
import com.example.student_exchange.model.MyPostsItem

class MyPostsAdapter(private val postList: List<MyPostsItem>) : RecyclerView.Adapter<MyPostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_myposts, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val myPost = postList[position]

        // Title and subtitle
        holder.tvTitle.text = myPost.title
        holder.tvSubtitle.text = myPost.subtitle

        // Image handling
        val imageUrls = myPost.imageUrls
        if (imageUrls.isNotEmpty()) {
            holder.image1.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(imageUrls[0])
                .into(holder.image1)
        } else {
            holder.image1.visibility = View.GONE
        }

        if (imageUrls.size > 1) {
            holder.image2.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(imageUrls[1])
                .into(holder.image2)
        } else {
            holder.image2.visibility = View.GONE
        }

        if (imageUrls.size > 2) {
            holder.image3.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(imageUrls[2])
                .into(holder.image3)
        } else {
            holder.image3.visibility = View.GONE
        }

        // View and scrap counts
        holder.tvViewCount.text = myPost.viewCount.toString()
        holder.tvScrapCount.text = myPost.scrapCount.toString()
    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image1: ImageView = itemView.findViewById(R.id.image1)
        val image2: ImageView = itemView.findViewById(R.id.image2)
        val image3: ImageView = itemView.findViewById(R.id.image3)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
        val tvViewCount: TextView = itemView.findViewById(R.id.tvViewCount)
        val tvScrapCount: TextView = itemView.findViewById(R.id.tvScrapCount)
    }
}