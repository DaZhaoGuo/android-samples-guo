package com.example.guo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.guo.language.kotlin.coroutine.CoroutineSample
import com.example.guo.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JetpackDemoViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "JetpackDemoViewModel"
    }

    private var testJob: Job? = null

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

    fun testDelay() {
        // 希望只有一个任务，重复调用时取消上次
        testJob?.cancel()

        testJob = viewModelScope.launch() {
            // 不会阻塞
            delay(7000)
            Log.d(TAG, "sample: ====")
        }
    }
}