package com.hackathon.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_post_layout.view.*

class PostHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val card: CardView = view.card
    val image: ImageView = view.imageView
    val map: ImageView = view.map
    val like: ImageView = view.like
    val liked: ImageView = view.liked
    val comments: ImageView = view.comments
    val profileImage: CircleImageView = view.profile_image
    val likeNumber: TextView = view.likeNumber
    val person: TextView = view.person
}