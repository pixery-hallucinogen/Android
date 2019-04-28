package com.hackathon.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.R
import com.hackathon.data.model.Post
import com.hackathon.databinding.HomeFragmentBinding
import com.hackathon.di.ILogger
import com.hackathon.justaline.DrawARActivity
import com.hackathon.ui.base.BaseFragment
import org.koin.android.ext.android.inject


class HomeFragment : BaseFragment<HomeViewModel>(HomeViewModel::class) {
    private val logger: ILogger by inject()
    private lateinit var dataBinding: HomeFragmentBinding
    private var posts: List<Post> = emptyList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        dataBinding = HomeFragmentBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.primaryDarkColor)
        }


        dataBinding.postRecyclerView.layoutManager = LinearLayoutManager(context)
        dataBinding.nearbyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.getAccount()
        viewModel.getNearbyPosts(41.0797675f, 29.0064777f)
        viewModel.getPosts()

        dataBinding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> TODO()
                R.id.bottom_nav_create -> {
                    startActivity(Intent(requireContext(), DrawARActivity::class.java))
                    true
                }
                else -> TODO()
//                R.id.bottom_nav_home -> "",
            }
        }

        return dataBinding.root
    }

    override fun bindCommands() {
        super.bindCommands()

        viewModel.onLoggedOut.runWhenFinished(this,
                onSuccess = {
                    navigateToLogin()
                },
                onError = {
                    navigateToLogin()
                })

        viewModel.onPostsFetched.runWhenFinished(this,
                onSuccess = {
                    this.posts = it.posts
                    dataBinding.postRecyclerView.adapter = PostAdapter(this, posts)
                    dataBinding.progressLayout.visibility = View.GONE
                },
                onError = {
                    dataBinding.progressLayout.visibility = View.GONE
                })

        viewModel.onNearbyPostsFetched.runWhenFinished(this,
                onSuccess = {
                    dataBinding.nearbyRecyclerView.adapter = NearbyPostAdapter(this, it.posts)
                },
                onError = {
                })

        viewModel.onPostLiked.runWhenFinished(this,
                onSuccess = {
                    logger.d("success")
                    val post = posts.first { item -> item.id == it.postId }
                    logger.d(post.likeCount.toString())
                    post.likeCount += 1
                    post.alreadyLiked = true
                    logger.d(post.likeCount.toString())
                    dataBinding.postRecyclerView.adapter?.notifyDataSetChanged()
                },
                onError = {
                })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_menu_logout -> {
                viewModel.logout(requireContext())
                true
            }
            else -> false
        }
    }

    fun navigateToComments(postId: Int) {
        val bundle = bundleOf("postId" to postId)
        navigate(R.id.comments_fragment, bundle)
    }


    fun navigateToMaps(lat: Double, lon: Double, userName: String) {
        val bundle = bundleOf("lat" to lat.toFloat(), "lon" to lon.toFloat(), "userName" to userName)
        navigate(R.id.maps_fragment, bundle)
    }

    private fun navigateToLogin() {
        navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
    }
}
