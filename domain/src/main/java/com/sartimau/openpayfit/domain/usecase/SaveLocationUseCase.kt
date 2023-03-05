package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.service.FirebaseService
import javax.inject.Inject

interface SaveLocationUseCase {
    operator fun invoke(location: Location)
}

class SaveLocationUseCaseImpl @Inject constructor(private val firebaseService: FirebaseService) : SaveLocationUseCase {
    override operator fun invoke(location: Location) {
        firebaseService.saveLocation(location)
    }
}
