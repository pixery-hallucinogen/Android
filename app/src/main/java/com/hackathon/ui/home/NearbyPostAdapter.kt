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


class NearbyPostAdapter(private val homeFragment: HomeFragment, private val myDataset: List<Post>) : RecyclerView.Adapter<NearbyPostHolder>(), KoinComponent {
    private val viewModel: HomeViewModel by inject()

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): NearbyPostHolder {
        val postLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.nearby_post_layout, parent, false) as View

        return NearbyPostHolder(postLayout)
    }

    override fun onBindViewHolder(holder: NearbyPostHolder, position: Int) {
        holder.card.preventCornerOverlap = false
        holder.card.useCompatPadding = true

        holder.person.text = myDataset[position].userName
        Picasso.get().load(myDataset[position].media).into(holder.image)
        if (myDataset[position].userPhoto == null)
            holder.profileImage.setBackgroundResource(R.drawable.fake_profile)
        else
            Picasso.get().load(myDataset[position].userPhoto).noFade().error(R.drawable.fake_profile).into(holder.profileImage)
        holder.location.text = myDataset[position].location
    }

    override fun getItemCount() = myDataset.size
}