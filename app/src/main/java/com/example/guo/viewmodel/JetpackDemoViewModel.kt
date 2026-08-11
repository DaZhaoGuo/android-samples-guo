package com.example.guo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.guo.language.kotlin.coroutine.CoroutineSample
import com.example.guo.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class JetpackDemoViewModel : BaseViewModel() {

    var counter = MutableLiveData<Int>()

    fun plus() {
        launchOnUI {
            CoroutineSample().test1()
        }

        viewModelScope.launch {
            CoroutineSample().test2()
        }
        counter.value = 3
    }
}