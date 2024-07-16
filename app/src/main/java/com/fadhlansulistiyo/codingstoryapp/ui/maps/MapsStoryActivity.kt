package com.fadhlansulistiyo.codingstoryapp.ui.maps

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fadhlansulistiyo.codingstoryapp.R
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.response.ListStoryItem
import com.fadhlansulistiyo.codingstoryapp.databinding.ActivityMapsStoryBinding
import com.fadhlansulistiyo.codingstoryapp.ui.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsStoryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var _binding: ActivityMapsStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MapsStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapsStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        observeMapStories()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        setMapStyle()

        viewModel.mapsStories.value?.let { result ->
            if (result is ResultState.Success) {
                addMarkers(result.data)
            }
        }
    }

    private fun observeMapStories() {
        viewModel.mapsStories.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    showToast(MAPS_LOADED_SUCCESSFULLY)
                    result.data.let { storyList ->
                        if (::mMap.isInitialized) {
                            addMarkers(storyList)
                        }
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun addMarkers(mapStories: List<ListStoryItem>) {
        for (data in mapStories) {
            val lat = data.lat
            val lon = data.lon
            if (lat != null && lon != null) {
                val markerOptions = MarkerOptions()
                    .position(LatLng(lat, lon))
                    .title(data.name)
                    .snippet(data.description)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                mMap.addMarker(markerOptions)
                boundsBuilder.include(LatLng(lat, lon))
            }
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

        /**
        * This code block is used for testing the display of maps stories in camera view.
        * Because sometimes there are stories located in countries outside Indonesia,
        * so the camera view (newLatLngBounds) doesn't zoom the stories properly.
        */
        /*mapStories.lastOrNull()?.let { firstStory ->
            firstStory.lat?.let { lat ->
                firstStory.lon?.let { lon ->
                    val lastLocation = LatLng(lat, lon)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
                }
            }
        }*/
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                showToast("Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            showToast("Can't find style. Error: $exception")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.linearProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val MAPS_LOADED_SUCCESSFULLY = "Maps Stories loaded successfully"
    }
}