package com.sartimau.openpayfit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.usecase.GetPopularPersonListUseCase
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.testObserver
import com.sartimau.openpayfit.viewmodel.ProfileViewModel.ProfileState
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ProfileViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getPopularPersonListUseCase: GetPopularPersonListUseCase

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = ProfileViewModel(getPopularPersonListUseCase)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch profile info success with data test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val popularPerson = PopularPerson(
                id = POPULAR_PERSON_ID,
                adult = false,
                gender = POPULAR_PERSON_GENDER,
                knownFor = emptyList(),
                knownForDepartment = POPULAR_PERSON_DEPARTMENT,
                name = POPULAR_PERSON_NAME,
                popularity = POPULAR_PERSON_LOW_POPULARITY,
                profilePath = POPULAR_PERSON_PROFILE_PATH
            )
            val mostPopularPerson = PopularPerson(
                id = POPULAR_PERSON_ID,
                adult = false,
                gender = POPULAR_PERSON_GENDER,
                knownFor = emptyList(),
                knownForDepartment = POPULAR_PERSON_DEPARTMENT,
                name = POPULAR_PERSON_NAME,
                popularity = POPULAR_PERSON_HIGH_POPULARITY,
                profilePath = POPULAR_PERSON_PROFILE_PATH
            )

            val popularPersonList = listOf(popularPerson, mostPopularPerson)

            coEvery { getPopularPersonListUseCase() } returns CoroutineResult.Success(popularPersonList)

            viewModel.fetchProfileInfo().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(ProfileState.SHOW_INFO, liveDataTest.observedValues[0]?.state)
            assertEquals(mostPopularPerson, liveDataTest.observedValues[0]?.mostPopularPerson)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch profile info success without data test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val popularPersonList = listOf<PopularPerson>()

            coEvery { getPopularPersonListUseCase() } returns CoroutineResult.Success(popularPersonList)

            viewModel.fetchProfileInfo().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(ProfileState.EMPTY_STATE, liveDataTest.observedValues[0]?.state)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    @Test
    fun `fetch profile info failure test`() {
        runTest(UnconfinedTestDispatcher()) {
            val loadingLiveDataTest = viewModel.isLoading().testObserver()
            val liveDataTest = viewModel.state.testObserver()

            val exception = Exception()

            coEvery { getPopularPersonListUseCase() } returns CoroutineResult.Failure(exception)

            viewModel.fetchProfileInfo().join()

            assertTrue(loadingLiveDataTest.observedValues[0]!!)
            assertEquals(ProfileState.ON_ERROR, liveDataTest.observedValues[0]?.state)
            assertEquals(exception, liveDataTest.observedValues[0]?.exception)
            assertFalse(loadingLiveDataTest.observedValues[1]!!)
        }
    }

    companion object {
        private const val POPULAR_PERSON_ID = 9292
        private const val POPULAR_PERSON_GENDER = 1
        private const val POPULAR_PERSON_DEPARTMENT = "Acting"
        private const val POPULAR_PERSON_NAME = "Pedro Pascal"
        private const val POPULAR_PERSON_LOW_POPULARITY = 52.43
        private const val POPULAR_PERSON_HIGH_POPULARITY = 543.87
        private const val POPULAR_PERSON_PROFILE_PATH = "POPULAR_PERSON_PROFILE_PATH"
    }
}
