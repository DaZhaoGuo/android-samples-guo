package com.example.guo.language.kotlin.coroutine

import kotlinx.coroutines.*

class CoroutineSample {

    /**
     * launch: 协程构建器，它与其余代码同时启动一个新的协程，该协程继续独立工作
     * delay(): 将协程暂停特定时间。挂起协程不会阻塞底层线程，但允许其他协程运行并使用底层线程来执行其代码
     * runBlocking{} 协程构建器，桥接了常规程序和协程代码。 阻塞，线程安全
     */

    fun test1() = runBlocking {//阻塞，线程安全 //this: CoroutineScope
        launch {//启动一个新的协程并继续
            delay(1000L) //非阻塞延迟1s
            println("World!") //延迟后打印
        }
        println("Hello") //主协程继续执行，而前一个协程被延迟

        /**
         * 结果：
         * Hello
         * World!
         */

    }

    fun test2() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
    }

    suspend fun test3() {                                // A function that can be suspended and resumed later
        val start = System.currentTimeMillis()
        coroutineScope {                                // Create a scope for starting coroutines
            for (i in 1..10) {
                launch {                                // Start 10 concurrent tasks
                    delay(3000L - i * 300)              // Pause their execution
                    log(start, "Countdown: $i")
                }
            }
        }
        // Execution continues when all coroutines in the scope have finished
        log(start, "Liftoff!")
    }




    fun log(start: Long, msg: String) {
        println(
            "$msg " +
                    "(on ${Thread.currentThread().name}) " +
                    "after ${(System.currentTimeMillis() - start) / 1000F}s"
        )
    }
}