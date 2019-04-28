package com.hackathon.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.nearby_post_layout.view.*

class NearbyPostHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val card: CardView = view.card
    val location: TextView = view.locationText
    val profileImage: CircleImageView = view.profile_image
    val image: ImageView = view.imageView
    val person: TextView = view.person
}