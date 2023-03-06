package com.sartimau.openpayfit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.usecase.GetLocationsUseCase
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.testObserver
import com.sartimau.openpayfit.viewmodel.MapViewModel.MapState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MapViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getLocationsUseCase: GetLocationsUseCase

    private lateinit var viewModel: MapViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = MapViewModel(getLocationsUseCase)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch locations success test`() {
        runTest(UnconfinedTestDispatcher()) {
            val liveDataTest = viewModel.state.testObserver()
            val locations = hashMapOf<String, Location>()

            coEvery { getLocationsUseCase() } returns CoroutineResult.Success(locations)

            viewModel.fetchLocations().join()

            assertEquals(MapState.UPDATE_MAP, liveDataTest.observedValues[0]?.state)
        }
    }

    @Test
    fun `fetch locations failure test`() {
        runTest(UnconfinedTestDispatcher()) {
            val liveDataTest = viewModel.state.testObserver()
            val exception = Exception()

            coEvery { getLocationsUseCase() } returns CoroutineResult.Failure(exception)

            viewModel.fetchLocations().join()

            assertEquals(MapState.ON_ERROR, liveDataTest.observedValues[0]?.state)
            assertEquals(exception, liveDataTest.observedValues[0]?.exception)
        }
    }
}
