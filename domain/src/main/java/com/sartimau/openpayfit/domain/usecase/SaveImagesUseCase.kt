package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.service.FirebaseService
import javax.inject.Inject

interface SaveImagesUseCase {
    suspend operator fun invoke(selectedImages: List<ByteArray>)
}

class SaveImagesUseCaseImpl @Inject constructor(private val firebaseService: FirebaseService) : SaveImagesUseCase {
    override suspend operator fun invoke(selectedImages: List<ByteArray>) {
        firebaseService.saveImages(selectedImages)
    }
}
