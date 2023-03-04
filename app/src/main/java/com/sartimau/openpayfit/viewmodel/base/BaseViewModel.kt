package com.sartimau.openpayfit.viewmodel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    protected val loading: MutableLiveData<Boolean> = MutableLiveData()
    fun isLoading(): LiveData<Boolean> = loading
}
