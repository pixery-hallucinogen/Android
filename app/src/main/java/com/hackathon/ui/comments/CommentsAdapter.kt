package com.hackathon.ui.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.R
import com.hackathon.data.model.Comment
import com.squareup.picasso.Picasso
import org.koin.standalone.KoinComponent


class CommentsAdapter(private val context: Context, private val myDataset: List<Comment>) : RecyclerView.Adapter<CommentsHolder>(), KoinComponent {
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CommentsHolder {
        val postLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_layout, parent, false) as View

        return CommentsHolder(postLayout)
    }

    override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
        holder.card.preventCornerOverlap = false
        holder.card.useCompatPadding = true

        holder.person.text = myDataset[position].userName
        if (myDataset[position].userPhoto.isNullOrEmpty())
            holder.profileImage.setBackgroundResource(R.drawable.fake_profile)
        else
            Picasso.get().load(myDataset[position].userPhoto).noFade().error(R.drawable.fake_profile).into(holder.profileImage)
        holder.comment.text = myDataset[position].comment

    }

    override fun getItemCount() = myDataset.size
}