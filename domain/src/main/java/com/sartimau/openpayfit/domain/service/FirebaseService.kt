package com.sartimau.openpayfit.domain.service

import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.utils.CoroutineResult

interface FirebaseService {
    fun saveLocation(location: Location)
    suspend fun getLocations(): CoroutineResult<HashMap<String, Location>>

    suspend fun saveImages(selectedImages: List<ByteArray>)
}
