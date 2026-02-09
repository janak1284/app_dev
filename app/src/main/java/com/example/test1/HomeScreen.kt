package com.example.test1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

private fun playAlarm(context: Context) {
    // Play default notification sound
    val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val r = RingtoneManager.getRingtone(context, notification)
    r.play()

    // Vibrate the phone
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        // Deprecated in API 26
        @Suppress("DEPRECATION")
        vibrator.vibrate(500)
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    locationViewModel: LocationViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentLocation by locationViewModel.currentLocation.collectAsState()
    val selectedDestination by locationViewModel.selectedDestination.collectAsState()

    val fusedLocationClient: FusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    locationViewModel.updateCurrentLocation(it)
                }
            }
        }
    }

    // Effect to manage location updates lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            val locationRequest =
                                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                                    .setMinUpdateIntervalMillis(500)
                                    .build()
                            fusedLocationClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.getMainLooper()
                            )
                        }
                    }
                    Lifecycle.Event.ON_STOP -> {
                        fusedLocationClient.removeLocationUpdates(locationCallback)
                    }
                    else -> {}
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen")
        Spacer(Modifier.height(16.dp))

        currentLocation?.let {
            Text(text = "Current Latitude: ${String.format("%.4f", it.latitude)}")
            Text(text = "Current Longitude: ${String.format("%.4f", it.longitude)}")
        } ?: Text(text = "Fetching Current Location...")

        Spacer(Modifier.height(16.dp))
        var alarmThreshold by remember { mutableStateOf(100f) } // Default to 100 meters
        var distanceRemainingMeters by remember { mutableStateOf<Float?>(null) }
        var alarmTriggered by remember { mutableStateOf(false) }

        // Calculate distance whenever currentLocation or selectedDestination changes
        LaunchedEffect(currentLocation, selectedDestination) {
            if (currentLocation != null && selectedDestination != null) {
                val results = FloatArray(1)
                Location.distanceBetween(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude,
                    selectedDestination!!.latitude,
                    selectedDestination!!.longitude,
                    results
                )
                distanceRemainingMeters = results[0]
            } else {
                distanceRemainingMeters = null
            }
        }

        selectedDestination?.let {
            Text(text = "Destination: ${String.format("%.4f", it.latitude)}, ${String.format("%.4f", it.longitude)}")
            distanceRemainingMeters?.let { distance ->
                Text(text = "Distance Remaining: ${String.format("%.0f", distance)} meters")

                // Alarm logic
                if (distance <= alarmThreshold && !alarmTriggered) {
                    playAlarm(context) // Trigger alarm sound and vibration
                    alarmTriggered = true
                    println("ALARM: You are within ${alarmThreshold.toInt()} meters of your destination!")
                } else if (distance > alarmThreshold && alarmTriggered) {
                    // Reset alarm if moved away from threshold
                    alarmTriggered = false
                }
            } ?: Text(text = "Calculating Distance...")
        } ?: Text(text = "Destination Status: Not Set")

        Spacer(Modifier.height(16.dp))
        Text(text = "Alarm Threshold: ${alarmThreshold.toInt()} meters")
        Slider(
            value = alarmThreshold,
            onValueChange = { alarmThreshold = it },
            valueRange = 10f..1000f, // 10 meters to 1000 meters
            steps = 99, // 1000 - 10 / 10 = 99
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate("map") }) {
            Text("Open Map")
        }
    }
}