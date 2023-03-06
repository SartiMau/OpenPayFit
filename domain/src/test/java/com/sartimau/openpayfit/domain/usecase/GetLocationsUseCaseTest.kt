package com.sartimau.openpayfit.domain.usecase

import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.service.FirebaseService
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class GetLocationsUseCaseTest {

    @MockK
    private lateinit var firebaseService: FirebaseService

    private lateinit var getLocationsUseCase: GetLocationsUseCase

    @Before
    fun init() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        getLocationsUseCase = GetLocationsUseCaseImpl(firebaseService)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `if use case returns success`() {
        runTest(UnconfinedTestDispatcher()) {
            val response = HashMap<String, Location>()

            coEvery { firebaseService.getLocations() } returns CoroutineResult.Success(response)

            val result = getLocationsUseCase()

            coVerify { firebaseService.getLocations() }
            assertEquals(response, (result as CoroutineResult.Success).data)
        }
    }

    @Test
    fun `if use case returns fail`() {
        runTest(UnconfinedTestDispatcher()) {
            val response = Exception()

            coEvery { firebaseService.getLocations() } returns CoroutineResult.Failure(response)

            val result = getLocationsUseCase()

            coVerify { firebaseService.getLocations() }
            assertEquals(response, (result as CoroutineResult.Failure).exception)
        }
    }
}
