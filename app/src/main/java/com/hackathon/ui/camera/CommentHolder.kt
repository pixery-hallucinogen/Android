package com.hackathon.ui.camera

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_list_item.view.*
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class CommentHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val card: CardView = view.card
    val rating: MaterialRatingBar = view.rating
    val username: TextView = view.username
    val comment: TextView = view.comment
    val date: TextView = view.date
}