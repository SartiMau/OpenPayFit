package com.sartimau.openpayfit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sartimau.openpayfit.domain.entity.Location
import com.sartimau.openpayfit.domain.usecase.GetLocationsUseCase
import com.sartimau.openpayfit.domain.utils.CoroutineResult
import com.sartimau.openpayfit.viewmodel.ProfileViewModel.ProfileState.ON_ERROR
import com.sartimau.openpayfit.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase
) : BaseViewModel() {

    private var mutableState = MutableLiveData<MapData>()
    val state: LiveData<MapData> = mutableState

    fun fetchLocations() = viewModelScope.launch {
        withContext(Dispatchers.IO) { getLocationsUseCase() }.let { result ->
            when (result) {
                is CoroutineResult.Success -> {
                    mutableState.value = MapData(state = MapState.UPDATE_MAP, locations = result.data)
                }
                is CoroutineResult.Failure -> {
                    mutableState.value = MapData(state = MapState.ON_ERROR, exception = result.exception)
                }
            }
        }
    }

    data class MapData(
        val state: MapState,
        val locations: HashMap<String, Location> = hashMapOf(),
        val exception: Exception? = null
    )

    enum class MapState {
        UPDATE_MAP,
        ON_ERROR
    }
}
