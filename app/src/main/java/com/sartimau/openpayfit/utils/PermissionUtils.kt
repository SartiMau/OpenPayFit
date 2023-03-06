package com.sartimau.openpayfit.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sartimau.openpayfit.domain.utils.OPEN_PAY_FIT_TAG

object PermissionUtils {
    private const val REQUEST_CODE_PERMISSIONS = 19515

    private val CAMERA_REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    private val LOCATION_REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun checkRequiredPermissions(context: Activity) {
        // Location
        if (!isLocationPermissionGranted(context)) {
            try {
                ActivityCompat.requestPermissions(context, LOCATION_REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            } catch (e: Exception) {
                Log.e(OPEN_PAY_FIT_TAG, "Location permission error")
            }
        }

        // Camera
        if (!isCameraPermissionGranted(context)) {
            try {
                ActivityCompat.requestPermissions(context, CAMERA_REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            } catch (e: Exception) {
                Log.e(OPEN_PAY_FIT_TAG, "Location permission error")
            }
        }
    }

    fun isCameraPermissionGranted(context: Context) = CAMERA_REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationPermissionGranted(context: Context) = LOCATION_REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
