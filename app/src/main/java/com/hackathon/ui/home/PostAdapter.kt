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
        holder.card.setOnClickListener {
            Notify.send(context, "Size özel kampanya", "Daha önce ilgilenmiş olduğunuz ${myDataset[position].postName} ürününde size özel kampanya.")
        }

        Picasso.get().load(myDataset[position].media).into(holder.image)
        holder.price.text = myDataset[position].price
        if (myDataset[position].currentPrice != null) {
            holder.price.paintFlags = holder.price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.currentPrice.text = myDataset[position].currentPrice
            holder.currentPrice.visibility = View.VISIBLE
        } else {
            holder.currentPrice.visibility = View.GONE
        }
        holder.taksit.text = myDataset[position].taksit
    }

    override fun getItemCount() = myDataset.size
}