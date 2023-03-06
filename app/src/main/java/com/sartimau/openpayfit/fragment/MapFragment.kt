package com.sartimau.openpayfit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds.Builder
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sartimau.openpayfit.R
import com.sartimau.openpayfit.databinding.FragmentMapBinding
import com.sartimau.openpayfit.dialog.ErrorDialog
import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.utils.ZERO_DOUBLE
import com.sartimau.openpayfit.viewmodel.MapViewModel
import com.sartimau.openpayfit.viewmodel.MapViewModel.MapData
import com.sartimau.openpayfit.viewmodel.MapViewModel.MapState.ON_ERROR
import com.sartimau.openpayfit.viewmodel.MapViewModel.MapState.UPDATE_MAP
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()

    private lateinit var binding: FragmentMapBinding

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe({ lifecycle }, ::updateUi)
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchLocations()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add initial marker
        val location = LatLng(ZERO_DOUBLE, ZERO_DOUBLE)
        mMap.addMarker(MarkerOptions().position(location))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun updateUi(data: MapData) {
        when (data.state) {
            UPDATE_MAP -> updateMapMarkers(data.locations)
            ON_ERROR -> ErrorDialog.newInstance().show(childFragmentManager, ErrorDialog.TAG)
        }
    }

    private fun updateMapMarkers(locations: HashMap<String, Location>) {
        if (::mMap.isInitialized && locations.isNotEmpty()) {
            mMap.clear()

            val markers = mutableListOf<Marker?>()

            locations.keys.forEach { key ->
                locations[key]?.let { location ->
                    val latLng = LatLng(location.latitude, location.longitude)

                    // Save marker and draw it
                    markers.add(mMap.addMarker(MarkerOptions().position(latLng).title(key)))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
            }

            // Zoom until all markers are visible
            val builder = Builder()
            for (marker in markers) {
                marker?.let { builder.include(it.position) }
            }
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0)
            mMap.animateCamera(cu)
        }
    }
}
