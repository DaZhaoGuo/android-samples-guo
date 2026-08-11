package com.example.guo

import com.example.guo.language.kotlin.coroutine.CoroutineSample
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test

class CoroutineTest {
    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun test() {
        val cs = CoroutineSample()
        cs.test1()


//        GlobalScope.launch {
//            cs.test2()
//        }

//        cs.test3()

    }
}