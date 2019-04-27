package com.hackathon.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hackathon.R
import com.hackathon.databinding.LoginFragmentBinding
import com.hackathon.di.ILogger
import com.hackathon.ui.base.BaseFragment
import org.koin.android.ext.android.inject


class LoginFragment : BaseFragment<LoginViewModel>(LoginViewModel::class) {
    private val logger: ILogger by inject()
    private lateinit var dataBinding: LoginFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Base Fragment on create view, calls view model on screen created
        super.onCreateView(inflater, container, savedInstanceState)

        // Bind View Model to the layout
        dataBinding = LoginFragmentBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        dataBinding.lifecycleOwner = this

        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.secondaryColor)
        }

        dataBinding.username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.username = s.toString()
            }

        })
        dataBinding.password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.password = s.toString()
            }

        })
        dataBinding.loginButton.setOnClickListener {
            viewModel.isLoggedIn()
            hideKeyboard()
            dataBinding.progressLayout.visibility = View.VISIBLE
        }

        return dataBinding.root
    }

    override fun bindCommands() {
        super.bindCommands()

        viewModel.onLoggedIn.runWhenFinished(this,
                onSuccess = {
                    navigateToHome()
                },
                onError = {
                    dataBinding.progressLayout.visibility = View.GONE
                    val (errorTitle, errorContent) = it.parseError(requireContext())
                    AlertDialog.Builder(requireActivity())
                            .setTitle(errorTitle)
                            .setMessage(errorContent)
                            .setCancelable(true)
                            .setOnCancelListener {
                                dataBinding.progressLayout.visibility = View.GONE
                            }
                            .setPositiveButton(R.string.close) { dialog, _ ->
                                dialog.cancel()
                            }
                            .create()
                            .show()
                })
    }

    private fun navigateToHome() {
        navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }
}
