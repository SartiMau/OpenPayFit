package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.service.FirebaseService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import javax.inject.Inject

interface GetLocationsUseCase {
    suspend operator fun invoke(): CoroutineResult<List<Location>>
}

class GetLocationsUseCaseImpl @Inject constructor(
    private val firebaseService: FirebaseService
) : GetLocationsUseCase {
    override suspend operator fun invoke(): CoroutineResult<List<Location>> = firebaseService.getLocations()
}
