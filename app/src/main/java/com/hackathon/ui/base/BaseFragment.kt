package com.hackathon.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.hackathon.data.error.BaseError
import com.hackathon.lib.listener.OnBackPressedListener
import com.hackathon.utils.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModelByClass
import kotlin.reflect.KClass

abstract class BaseFragment<out VM : BaseViewModel>(
        viewModelClazz: KClass<VM>
) : Fragment(), OnBackPressedListener {
    protected var initialized: Boolean = false
        private set
    protected val viewModel: VM by viewModelByClass(viewModelClazz)

    protected val navController by lazy {
        findNavController()
    }

    private var isOverriddenBackPressedListener = false

    protected fun navigate(directions: NavDirections) {
        this.navController.navigate(directions)
    }

    protected fun navigate(actionId: Int, bundle: Bundle) {
        this.navController.navigate(actionId, bundle)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.onScreenCreated()
        this.bindCommands()

        this.initialized = true

        return null
    }

    @CallSuper
    protected open fun bindCommands() {

    }

    @CallSuper
    protected fun setOnBackPressedListener(listener: OnBackPressedListener) {
        (activity as BaseActivity).onBackPressedListener = listener
        isOverriddenBackPressedListener = true
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()

        if (isOverriddenBackPressedListener)
            (activity as BaseActivity).onBackPressedListener = null
    }

    @CallSuper
    override fun onBackPressed() {
        goBack()
    }

    fun goBack() {
        (activity as AppCompatActivity).onSupportNavigateUp()
    }

    fun hideKeyboard() {
        (activity as AppCompatActivity).hideKeyboard()
    }

    fun showStatusBar() {
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun hideStatusBar() {
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN  //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}