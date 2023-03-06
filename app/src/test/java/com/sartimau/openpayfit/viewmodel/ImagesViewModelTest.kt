package com.sartimau.openpayfit.viewmodel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sartimau.openpayfit.domain.usecase.SaveImagesUseCase
import com.sartimau.openpayfit.testObserver
import com.sartimau.openpayfit.viewmodel.ImagesViewModel.ImagesState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ImagesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var saveImagesUseCase: SaveImagesUseCase
    @MockK
    private lateinit var bitmap: Bitmap

    private lateinit var viewModel: ImagesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = ImagesViewModel(saveImagesUseCase)

        val liveDataTest = viewModel.state.testObserver()

        assertEquals(ImagesState.SHOW_IMAGES, liveDataTest.observedValues[0]?.state)
        assertEquals(0, liveDataTest.observedValues[0]?.images?.size)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `on camera selected test`() {
        val liveDataTest = viewModel.state.testObserver()

        viewModel.onCameraSelected()

        assertEquals(ImagesState.OPEN_CAMERA, liveDataTest.observedValues[1]?.state)
    }

    @Test
    fun `on gallery selected test`() {
        val liveDataTest = viewModel.state.testObserver()

        viewModel.onGallerySelected()

        assertEquals(ImagesState.OPEN_GALLERY, liveDataTest.observedValues[1]?.state)
    }

    @Test
    fun `on save selected with network connection test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            viewModel.onSaveSelected(true).join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(ImagesState.SHOW_IMAGES, liveDataTest.observedValues[1]?.state)
            assertEquals(0, liveDataTest.observedValues[1]?.images?.size)
            assertEquals(ImagesState.SAVED_IMAGES, liveDataTest.observedValues[2]?.state)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `on save selected without network connection test`() {
        val liveDataTest = viewModel.state.testObserver()

        viewModel.onSaveSelected(false)

        assertEquals(ImagesState.ON_ERROR, liveDataTest.observedValues[1]?.state)
    }

    @Test
    fun `on image selected and clear test`() {
        val liveDataTest = viewModel.state.testObserver()
        val imagesList = listOf(bitmap)

        viewModel.onImagesSelected(imagesList)

        assertEquals(ImagesState.SHOW_IMAGES, liveDataTest.observedValues[1]?.state)
        assertEquals(1, liveDataTest.observedValues[1]?.images?.size)

        viewModel.onClearSelected()

        assertEquals(ImagesState.SHOW_IMAGES, liveDataTest.observedValues[2]?.state)
        assertEquals(0, liveDataTest.observedValues[2]?.images?.size)
    }
}
