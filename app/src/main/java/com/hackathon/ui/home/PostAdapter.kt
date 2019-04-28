package com.hackathon.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.R
import com.hackathon.data.model.Post
import com.squareup.picasso.Picasso
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class PostAdapter(private val homeFragment: HomeFragment, private val myDataset: List<Post>) : RecyclerView.Adapter<PostBaseHolder>(), KoinComponent {
    private val viewModel: HomeViewModel by inject()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PostBaseHolder {
        if (viewType == 0) {
            val postLayout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.main_post_layout, parent, false) as View

            return PostHolder(postLayout)
        } else {
            val postLayout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.main_post_video_layout, parent, false) as View

            return PostVideoHolder(postLayout)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (myDataset[position].media?.contains(".mp4")) 1 else 0
    }

    override fun onBindViewHolder(holder: PostBaseHolder, position: Int) {
        if (holder is PostVideoHolder) {
            val holder = holder as PostVideoHolder
            holder.card.preventCornerOverlap = false
            holder.card.useCompatPadding = true

            holder.like.setOnClickListener {
                viewModel.likePost(myDataset[position].id!!)
                holder.like.visibility = View.GONE
                holder.liked.visibility = View.VISIBLE
                holder.likeNumber.text = (myDataset[position].likeCount!! + 1).toString()
            }
            holder.comments.setOnClickListener {
                homeFragment.navigateToComments(myDataset[position].id!!)
            }
            holder.map.setOnClickListener {
                homeFragment.navigateToMaps(myDataset[position].latitude, myDataset[position].longitude, myDataset[position].userName
                        ?: "")
            }

            holder.person.text = myDataset[position].userName
            if (myDataset[position].userPhoto == null)
                holder.profileImage.setBackgroundResource(R.drawable.fake_profile)
            else
                Picasso.get().load(myDataset[position].userPhoto).noFade().error(R.drawable.fake_profile).into(holder.profileImage)
            holder.likeNumber.text = myDataset[position].likeCount.toString()

            if (myDataset[position].alreadyLiked!!) {
                holder.like.visibility = View.GONE
                holder.liked.visibility = View.VISIBLE
            } else {
                holder.like.visibility = View.VISIBLE
                holder.liked.visibility = View.GONE
            }
        } else {
            val holder = holder as PostHolder
            holder.card.preventCornerOverlap = false
            holder.card.useCompatPadding = true

            holder.like.setOnClickListener {
                viewModel.likePost(myDataset[position].id!!)
                holder.like.visibility = View.GONE
                holder.liked.visibility = View.VISIBLE
                holder.likeNumber.text = (myDataset[position].likeCount!! + 1).toString()
            }
            holder.comments.setOnClickListener {
                homeFragment.navigateToComments(myDataset[position].id!!)
            }
            holder.map.setOnClickListener {
                homeFragment.navigateToMaps(myDataset[position].latitude, myDataset[position].longitude, myDataset[position].userName
                        ?: "")
            }

            Picasso.get().load(myDataset[position].media).into(holder.image)
            holder.person.text = myDataset[position].userName
            if (myDataset[position].userPhoto == null)
                holder.profileImage.setBackgroundResource(R.drawable.fake_profile)
            else
                Picasso.get().load(myDataset[position].userPhoto).noFade().error(R.drawable.fake_profile).into(holder.profileImage)
            holder.likeNumber.text = myDataset[position].likeCount.toString()

            if (myDataset[position].alreadyLiked!!) {
                holder.like.visibility = View.GONE
                holder.liked.visibility = View.VISIBLE
            } else {
                holder.like.visibility = View.VISIBLE
                holder.liked.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = myDataset.size
}