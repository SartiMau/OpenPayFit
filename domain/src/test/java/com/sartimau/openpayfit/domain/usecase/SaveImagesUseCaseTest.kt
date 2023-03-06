package com.sartimau.openpayfit.domain.usecase

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
class SaveImagesUseCaseTest {

    @MockK
    private lateinit var firebaseService: FirebaseService

    private lateinit var saveImagesUseCase: SaveImagesUseCase

    @Before
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        saveImagesUseCase = SaveImagesUseCaseImpl(firebaseService)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `if use case calls`() {
        runTest(UnconfinedTestDispatcher()) {
            val byteArrayList = listOf<ByteArray>()

            saveImagesUseCase(byteArrayList)

            coVerify { firebaseService.saveImages(byteArrayList) }
        }
    }
}
