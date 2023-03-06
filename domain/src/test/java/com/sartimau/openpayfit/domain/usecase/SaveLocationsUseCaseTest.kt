package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.service.FirebaseService
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SaveLocationsUseCaseTest {

    @MockK
    private lateinit var firebaseService: FirebaseService

    private lateinit var saveLocationUseCase: SaveLocationUseCase

    @Before
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        saveLocationUseCase = SaveLocationUseCaseImpl(firebaseService)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `if use case calls`() {
        runTest(UnconfinedTestDispatcher()) {
            val location = Location(ZERO_DOUBLE, ZERO_DOUBLE)

            saveLocationUseCase(location)

            coVerify { firebaseService.saveLocation(location) }
        }
    }

    companion object {
        private const val ZERO_DOUBLE = 0.0
    }
}
