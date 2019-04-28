package com.hackathon.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hackathon.R
import com.hackathon.databinding.MapsFragmentBinding
import com.hackathon.di.ILogger
import com.hackathon.ui.base.BaseFragment
import org.koin.android.ext.android.inject


class MapsFragment : BaseFragment<MapsViewModel>(MapsViewModel::class), OnMapReadyCallback {
    private val logger: ILogger by inject()
    private lateinit var dataBinding: MapsFragmentBinding
    private var lat: Float = 0f
    private var lon: Float = 0f
    private var userName: String = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Base Fragment on create view, calls view model on screen created
        super.onCreateView(inflater, container, savedInstanceState)

        // Bind View Model to the layout
        dataBinding = MapsFragmentBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lat = arguments?.getFloat("lat") ?: 0f
        lon = arguments?.getFloat("lon") ?: 0f
        userName = arguments?.getString("userName") ?: ""
        viewModel.triggetLatLon(lat, lon)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        viewModel.onLatLonReceived.runWhenFinished(this,
                onSuccess = {
                    dataBinding.progressBar.visibility = View.GONE
                    googleMap?.addMarker(
                            MarkerOptions()
                                    .position(LatLng(it[0].toDouble(), it[1].toDouble()))
                                    .title(userName))
                    googleMap?.moveCamera(CameraUpdateFactory.zoomTo(12F))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it[0].toDouble(), it[1].toDouble())))
                }, onError = {
            dataBinding.progressBar.visibility = View.GONE
            
        })
    }
}