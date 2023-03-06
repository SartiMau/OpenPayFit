package com.sartimau.openpayfit.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.sartimau.openpayfit.databinding.ActivityCameraBinding
import com.sartimau.openpayfit.domain.utils.OPEN_PAY_FIT_TAG
import com.sartimau.openpayfit.fragment.ImagesFragment.Companion.EXTRA_PICTURE_TAKEN
import java.io.File

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var imageCapture: ImageCapture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener { takePhoto() }
    }

    override fun onResume() {
        super.onResume()

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreviewView.display.rotation)
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview.setSurfaceProvider(binding.cameraPreviewView.surfaceProvider)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview)
            } catch (ex: Exception) {
                Log.e(OPEN_PAY_FIT_TAG, "Failed to bind camera", ex)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val outputFile = File(externalMediaDirs.first(), "photo.jpg")
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(outputFile)
            .build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(outputFile)

                    val returnIntent = Intent()
                    returnIntent.putExtra(EXTRA_PICTURE_TAKEN, savedUri)
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    Log.d(OPEN_PAY_FIT_TAG, "Photo saved: $savedUri")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(OPEN_PAY_FIT_TAG, "Failed to save photo", exception)
                }
            }
        )
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, CameraActivity::class.java)
    }
}
