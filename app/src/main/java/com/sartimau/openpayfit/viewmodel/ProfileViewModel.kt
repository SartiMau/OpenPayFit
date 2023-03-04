package com.sartimau.openpayfit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.domain.usecase.GetPopularPersonListUseCase
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getPopularPersonListUseCase: GetPopularPersonListUseCase
) : BaseViewModel() {

    private var mutableState = MutableLiveData<ProfileData>()
    val state: LiveData<ProfileData> = mutableState

    fun fetchProfileInfo() = viewModelScope.launch {
        loading.value = true
        withContext(Dispatchers.IO) { getPopularPersonListUseCase() }.let { result ->
            when (result) {
                is CoroutineResult.Success -> {
                    if (result.data.isNotEmpty()) {
                        mutableState.value =
                            ProfileData(state = ProfileState.SHOW_INFO, mostPopularPerson = result.data.maxBy { it.popularity })
                    } else {
                        mutableState.value = ProfileData(state = ProfileState.EMPTY_STATE)
                    }
                    loading.value = false
                }
                is CoroutineResult.Failure -> {
                    mutableState.value = ProfileData(state = ProfileState.ON_ERROR, exception = result.exception)
                    loading.value = false
                }
            }
        }
    }

    data class ProfileData(
        val state: ProfileState,
        val mostPopularPerson: PopularPerson? = null,
        val exception: Exception? = null
    )

    enum class ProfileState {
        SHOW_INFO,
        ON_ERROR,
        EMPTY_STATE
    }
}
