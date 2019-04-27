package com.hackathon.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hackathon.R
import com.hackathon.databinding.SplashFragmentBinding
import com.hackathon.di.ILogger
import com.hackathon.ui.base.BaseFragment
import com.hackathon.ui.splash.SplashViewModel
import org.koin.android.ext.android.inject


class SplashFragment: BaseFragment<SplashViewModel>(SplashViewModel::class) {
    private val logger: ILogger by inject()
    private lateinit var dataBinding: SplashFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        dataBinding = SplashFragmentBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        dataBinding.lifecycleOwner = this

        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.secondaryColor)
        }

        viewModel.isLoggedIn(requireContext())

        return dataBinding.root
    }

    override fun bindCommands() {
        super.bindCommands()

        viewModel.onAuthResult.runWhenFinished(this,
                onSuccess = {
                    if(it)
                        navigateToHome()
                    else
                        navigateToLogin()
                },
                onError = {
                    navigateToLogin()
                })
    }

    private fun navigateToHome() {
        navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
    }

    private fun navigateToLogin() {
        navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
    }
}
