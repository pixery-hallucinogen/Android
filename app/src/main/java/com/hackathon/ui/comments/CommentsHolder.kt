package com.hackathon.ui.comments

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.comment_layout.view.*

class CommentsHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val card: CardView = view.card
    val profileImage: CircleImageView = view.profile_image
    val comment: TextView = view.text
    val person: TextView = view.person
}