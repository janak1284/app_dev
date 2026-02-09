package com.example.test1

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationViewModel : ViewModel() {

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation

    private val _selectedDestination = MutableStateFlow<LatLng?>(null)
    val selectedDestination: StateFlow<LatLng?> = _selectedDestination

    fun updateCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

    fun updateSelectedDestination(latLng: LatLng) {
        _selectedDestination.value = latLng
    }
}