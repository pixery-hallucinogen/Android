package com.hackathon.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.R
import com.hackathon.databinding.HomeFragmentBinding
import com.hackathon.di.ILogger
import com.hackathon.ui.base.BaseFragment
import org.koin.android.ext.android.inject


class HomeFragment : BaseFragment<HomeViewModel>(HomeViewModel::class) {
    private val logger: ILogger by inject()
    private lateinit var dataBinding: HomeFragmentBinding

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

        dataBinding.progressLayout.visibility = View.VISIBLE
        dataBinding.recommendationsRecyclerView.layoutManager = LinearLayoutManager(context)

        dataBinding.cameraButton.setOnClickListener {
            navigateToCamera()
        }

        viewModel.fetchRecommendations()

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

        viewModel.onRecommendationsFetched.runWhenFinished(this,
                onSuccess = {
                    dataBinding.progressLayout.visibility = View.GONE
                    dataBinding.recommendationsRecyclerView.adapter = ProductAdapter(requireContext(), it)
                },
                onError = {
                    dataBinding.progressLayout.visibility = View.GONE
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

    private fun navigateToCamera() {
        navigate(HomeFragmentDirections.actionHomeFragmentToCameraFragment())
    }

    private fun navigateToLogin() {
        navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
    }
}
