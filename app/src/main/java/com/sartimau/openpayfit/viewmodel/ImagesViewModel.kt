package com.sartimau.openpayfit.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sartimau.openpayfit.domain.usecase.SaveImagesUseCase
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.ON_ERROR
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.OPEN_CAMERA
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.OPEN_GALLERY
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.SAVED_IMAGES
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState.SHOW_IMAGES
import com.sartimau.openpayfit.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val saveImagesUseCase: SaveImagesUseCase
) : BaseViewModel() {

    private var mutableState = MutableLiveData<ImagesData>()
    val state: LiveData<ImagesData> = mutableState

    private val selectedImages: ArrayList<Bitmap> = ArrayList()

    init {
        mutableState.value = ImagesData(state = SHOW_IMAGES, images = selectedImages)
    }

    fun onCameraSelected() {
        mutableState.value = ImagesData(state = OPEN_CAMERA)
    }

    fun onGallerySelected() {
        mutableState.value = ImagesData(state = OPEN_GALLERY)
    }

    fun onSaveSelected(networkConnected: Boolean) = viewModelScope.launch {
        if (networkConnected) {
            loading.value = true
            withContext(Dispatchers.IO) { async { saveImagesUseCase(transformSelectedImages()) } }.await()

            onClearSelected()
            mutableState.value = ImagesData(state = SAVED_IMAGES)
            loading.value = false
        } else {
            mutableState.value = ImagesData(state = ON_ERROR)
        }
    }

    private fun transformSelectedImages(): List<ByteArray> {
        val byteArrayList = mutableListOf<ByteArray>()

        selectedImages.forEach { bitmap ->
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            byteArrayList.add(baos.toByteArray())
        }

        return byteArrayList
    }

    fun onImagesSelected(images: List<Bitmap>) {
        selectedImages.addAll(images)

        mutableState.value = ImagesData(state = SHOW_IMAGES, images = selectedImages)
    }

    fun onClearSelected() {
        selectedImages.clear()
        mutableState.value = ImagesData(state = SHOW_IMAGES, images = selectedImages)
    }

    data class ImagesData(
        val state: ImagesState,
        val images: List<Bitmap> = emptyList()
    )

    enum class ImagesState {
        SHOW_IMAGES,
        OPEN_CAMERA,
        OPEN_GALLERY,
        SAVED_IMAGES,
        ON_ERROR
    }
}
