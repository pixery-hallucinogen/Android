package com.hackathon.ui.home

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.R
import com.hackathon.data.model.Post
import com.hackathon.lib.notification.Notify
import com.squareup.picasso.Picasso


class PostAdapter(private val context: Context, private val myDataset: List<Post>) : RecyclerView.Adapter<PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PostHolder {
        val postLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_post_layout, parent, false) as View

        return PostHolder(postLayout)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.card.preventCornerOverlap = false
        holder.card.useCompatPadding = true

        //holder.card.setOnClickListener {
        //    Notify.send(context, "Size özel kampanya", "Daha önce ilgilenmiş olduğunuz ${myDataset[position].postName} ürününde size özel kampanya.")
        //}

        Picasso.get().load(myDataset[position].media).into(holder.image)
        holder.person.text = myDataset[position].userName
        Picasso.get().load(myDataset[position].userPhoto).noFade().into(holder.profileImage)
        holder.likeNumber.text = myDataset[position].likeCount.toString()

        if (myDataset[position].alreadyLiked) {
            holder.like.visibility = View.GONE
        } else {
            holder.liked.visibility = View.GONE
        }

    }

    override fun getItemCount() = myDataset.size
}