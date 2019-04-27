package com.hackathon.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.hackathon.lib.listener.OnBackPressedListener

abstract class BaseActivity : AppCompatActivity() {
    var onBackPressedListener: OnBackPressedListener? = null

    override fun onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener?.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}