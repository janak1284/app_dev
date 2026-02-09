package com.example.test1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(
    navController: NavController,
    locationViewModel: LocationViewModel = viewModel()
) {
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMapClick = { latLng ->
                selectedLocation = latLng
            }
        ) {
            selectedLocation?.let {
                Marker(state = MarkerState(position = it), title = "Destination")
            }
        }
        Button(
            onClick = {
                selectedLocation?.let {
                    locationViewModel.updateSelectedDestination(it)
                }
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            enabled = selectedLocation != null // Enable button only if a location is selected
        ) {
            Text("Confirm Destination")
        }
    }
}