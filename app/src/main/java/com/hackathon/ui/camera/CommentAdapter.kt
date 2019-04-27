package com.hackathon.ui.camera

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.R
import com.hackathon.data.model.GetCommentResponse

class CommentAdapter(private val myDataset: GetCommentResponse) : RecyclerView.Adapter<CommentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CommentHolder {
        val commentLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_list_item, parent, false) as View

        return CommentHolder(commentLayout)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.card.preventCornerOverlap = false
        holder.card.setPadding(0, 0, 0, 0)
        holder.card.useCompatPadding = true
        holder.card.setContentPadding(-6, -6, -6, -6)
        holder.rating.rating = myDataset.comments[position].star
        holder.comment.text = myDataset.comments[position].commentBody
        holder.username.text = myDataset.users[position]
        holder.date.text = myDataset.comments[position].date
    }

    override fun getItemCount() = myDataset.comments.size
}