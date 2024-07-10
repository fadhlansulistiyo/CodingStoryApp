package com.fadhlansulistiyo.codingstoryapp.maps

import com.fadhlansulistiyo.codingstoryapp.data.response.ListStoryItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapHandler {
    private val boundsBuilder = LatLngBounds.Builder()

    fun addMarkers(map: GoogleMap, stories: List<ListStoryItem>) {
        for (story in stories) {
            val lat = story.lat
            val lon = story.lon
            if (lat != null && lon != null) {
                val markerOptions = MarkerOptions()
                    .position(LatLng(lat, lon))
                    .title(story.name)
                    .snippet(story.description)
                map.addMarker(markerOptions)
            }
        }

        val bounds = boundsBuilder.build()
        val padding = 100
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
    }
}