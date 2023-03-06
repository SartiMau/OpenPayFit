package com.sartimau.openpayfit.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.sartimau.openpayfit.domain.usecase.SaveLocationUseCase
import com.sartimau.openpayfit.utils.NotificationUtils
import com.sartimau.openpayfit.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class TimeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var saveLocationUseCase: SaveLocationUseCase

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let { ctx ->
            // Get current location
            if (PermissionUtils.isLocationPermissionGranted(ctx)) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            runBlocking {
                                // Save location on Firebase Firestore
                                saveLocationUseCase(com.sartimau.openpayfit.domain.entity.Location(it.latitude, it.longitude))
                            }
                            // Send notification
                            NotificationUtils.createNotificationCompat(ctx)
                        }
                    }
            }
        }

        Log.i("OpenPayFit", "Executed each 5 minutes")
    }
}
