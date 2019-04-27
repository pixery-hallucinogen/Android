package com.hackathon.ui.camera

import android.Manifest
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.hackathon.R
import com.hackathon.data.error.ServerError
import com.hackathon.databinding.CameraFragmentBinding
import com.hackathon.di.ILogger
import com.hackathon.ui.base.BaseFragment
import com.squareup.picasso.Picasso
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.koin.android.ext.android.inject
import java.util.Arrays.asList

class CameraFragment : BaseFragment<CameraViewModel>(CameraViewModel::class), ZXingScannerView.ResultHandler {
    private val logger: ILogger by inject()
    private lateinit var dataBinding: CameraFragmentBinding
    private lateinit var vibrator: Vibrator
    private var storeId: Int = -1
    private var productId: Int = -1
    private var cameraState: CameraState = CameraState.SCAN

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        dataBinding = CameraFragmentBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(requireActivity(), R.color.secondaryColor)
        }
        askPermission()

        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        dataBinding.cameraView.setFormats(asList(BarcodeFormat.QR_CODE))
        dataBinding.closeButton.setOnClickListener {
            dataBinding.motionLayout.transitionToStart()
            cameraState = CameraState.SCAN
        }
        dataBinding.commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        dataBinding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                if (cameraState == CameraState.SCAN) {
                    p0?.progress = 0f
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (p0?.progress == 0f) {
                    cameraState = CameraState.SCAN
                }
            }

        })

        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()
        dataBinding.cameraView.setResultHandler(this)
        dataBinding.cameraView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        dataBinding.cameraView.stopCamera()
    }

    override fun bindCommands() {
        super.bindCommands()

        viewModel.onCommentsFetched.runWhenFinished(this,
                onSuccess = {
                    vibrate()
                    dataBinding.commentsRecyclerView.adapter = CommentAdapter(it)
                    cameraState = CameraState.COMMENT
                    dataBinding.fastPurchaseButton.setOnClickListener {
                        viewModel.purchase(productId, storeId, dataBinding.numberPicker.value)
                        dataBinding.progressLayout.visibility = View.VISIBLE
                    }
                    dataBinding.productName.text = it.product.first().productName
                    Picasso.get().load(it.product.first().imageUrl).into(dataBinding.productImage)
                    dataBinding.rating.text = String.format("%.2f", it.star)
                    dataBinding.productRating.rating = it.star
                    dataBinding.progressLayout.visibility = View.GONE
                    slideToHalf()
                },
                onError = {
                    dataBinding.progressLayout.visibility = View.GONE
                    cameraState = CameraState.SCAN
                })

        viewModel.onPurchased.runWhenFinished(this,
                onSuccess = {
                    Toast.makeText(requireContext(), getString(R.string.successfullyPurchased), Toast.LENGTH_LONG).show()
                    dataBinding.progressLayout.visibility = View.GONE
                    dataBinding.motionLayout.transitionToStart()
                    cameraState = CameraState.SCAN
                },
                onError = {
                    Toast.makeText(requireContext(), getString(R.string.errorOccurred), Toast.LENGTH_LONG).show()
                    dataBinding.progressLayout.visibility = View.GONE
                })
    }

    override fun handleResult(rawResult: Result?) {
        dataBinding.cameraView.resumeCameraPreview(this)
        if (cameraState == CameraState.SCAN && rawResult != null) {
            vibrate()
            dataBinding.progressLayout.visibility = View.VISIBLE
            val data = rawResult.text.split(",")
            viewModel.getComments(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]))
            storeId = Integer.parseInt(data[1])
            productId = Integer.parseInt(data[0])
            cameraState = CameraState.GET
        }
    }

    private fun askPermission() {
        val permission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    99)
        } else if (permission == PackageManager.PERMISSION_DENIED) {
            AlertDialog.Builder(requireContext())
                    .setTitle(R.string.errorOccurred)
                    .setMessage(R.string.cameraPermission)
                    .setCancelable(false)
                    .setPositiveButton(R.string.close) { _, _ ->
                        goBack()
                    }.create()
                    .show()
        }
    }

    private fun slideToHalf() {
        if (dataBinding.motionLayout.progress == 0.6F) {
            return
        }
        ValueAnimator.ofFloat(dataBinding.motionLayout.progress, 0.6F).also {
            it.addUpdateListener { valueAnimator ->
                val progress = valueAnimator.animatedValue as Float
                dataBinding.motionLayout.progress = progress
            }
            it.duration = 300L
            it.interpolator = AccelerateInterpolator()
            it.start()
        }
    }

    @Suppress("DEPRECATION")
    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    private enum class CameraState {
        SCAN,
        GET,
        COMMENT
    }
}
