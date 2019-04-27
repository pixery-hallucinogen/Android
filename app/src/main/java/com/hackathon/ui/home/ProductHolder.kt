package com.hackathon.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.product_list_item.view.*

class ProductHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val card: CardView = view.card
    val image: ImageView = view.imageView
    val name: TextView = view.productName
    val price: TextView = view.price
    val currentPrice: TextView = view.currentPrice
    val taksit: TextView = view.taksit
}