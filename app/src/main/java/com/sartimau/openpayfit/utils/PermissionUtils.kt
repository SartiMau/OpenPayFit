package com.sartimau.openpayfit.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    private const val REQUEST_CODE_PERMISSIONS = 19515

    private val LOCATION_REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun checkLocationPermissions(context: Activity) {
        if (!isLocationPermissionGranted(context)) {
            try {
                ActivityCompat.requestPermissions(context, LOCATION_REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            } catch (e: Exception) {
                Log.e("OpenPayFit", "Location permission error")
            }
        }
    }

    fun isLocationPermissionGranted(context: Context) = LOCATION_REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
