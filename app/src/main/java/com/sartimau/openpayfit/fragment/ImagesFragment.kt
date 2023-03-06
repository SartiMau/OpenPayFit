package com.sartimau.openpayfit.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sartimau.openpayfit.R
import com.sartimau.openpayfit.activity.CameraActivity
import com.sartimau.openpayfit.adapter.ImagesTabAdapter
import com.sartimau.openpayfit.databinding.FragmentImagesBinding
import com.sartimau.openpayfit.dialog.ErrorDialog
import com.sartimau.openpayfit.utils.NetworkUtils
import com.sartimau.openpayfit.viewmodel.ImagesViewModel
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesData
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.ON_ERROR
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.OPEN_CAMERA
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.OPEN_GALLERY
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.SAVED_IMAGES
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.SHOW_IMAGES
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesFragment : Fragment() {

    private val viewModel: ImagesViewModel by viewModels()

    private lateinit var binding: FragmentImagesBinding

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImages = mutableListOf<Bitmap>()

            if (result.data?.clipData != null) {
                // If the user selected multiple images
                val count: Int = result?.data?.clipData?.itemCount ?: 0
                for (i in 0 until count) {
                    val imageUri: Uri? = result.data?.clipData?.getItemAt(i)?.uri

                    imageUri?.let {
                        val bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(imageUri))
                        selectedImages.add(bitmap)
                    }
                }
            } else if (result.data?.data != null) {
                // If the user selected only one image
                val imageUri: Uri? = result.data?.data

                imageUri?.let {
                    val bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(imageUri))
                    selectedImages.add(bitmap)
                }
            }

            viewModel.onImagesSelected(selectedImages)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri = result.data?.extras?.get(EXTRA_PICTURE_TAKEN) as? Uri
            imageUri?.let {
                val bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(imageUri))

                viewModel.onImagesSelected(listOf(bitmap))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe({ lifecycle }, ::updateUi)

        binding.cameraButton.setOnClickListener { viewModel.onCameraSelected() }
        binding.galleryButton.setOnClickListener { viewModel.onGallerySelected() }
        binding.clear.setOnClickListener { viewModel.onClearSelected() }
        binding.saveButton.setOnClickListener {
            context?.let { ctx ->
                viewModel.onSaveSelected(NetworkUtils.isNetworkConnected(ctx))
            }
        }
    }

    private fun updateUi(data: ImagesData) {
        when (data.state) {
            SHOW_IMAGES -> refreshImages(data.images)
            OPEN_CAMERA -> {
                context?.let { ctx ->
                    cameraLauncher.launch(CameraActivity.newIntent(ctx))
                }
            }
            OPEN_GALLERY -> openGallery()
            SAVED_IMAGES -> Toast.makeText(context, getString(R.string.images_saved_toast), Toast.LENGTH_SHORT).show()
            ON_ERROR -> ErrorDialog.newInstance().show(childFragmentManager, ErrorDialog.TAG)
        }
    }

    private fun refreshImages(images: List<Bitmap>) {
        binding.recycler.apply {
            adapter = ImagesTabAdapter(images)
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        binding.imageCount.text = getString(R.string.images_tab_image_counts, images.size.toString())
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        val pickIntent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.gallery_intent_title))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        galleryLauncher.launch(chooserIntent)
    }

    companion object {
        const val EXTRA_PICTURE_TAKEN = "EXTRA_PICTURE_TAKEN"
    }
}
