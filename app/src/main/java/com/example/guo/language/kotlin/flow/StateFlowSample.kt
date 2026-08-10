package com.example.guo.language.kotlin.flow

import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * StateFlow 和 SharedFlow 是 Flow API，允许数据流以最优方式发出状态更新并向多个使用方发出值
 *
 * ### SharedFlow
 * 表示只读状态，具有单个可更新数据值，该数据值向其收集器发出对值的更新。状态流是热流，因为其活动实例独立于收集器的存在而存在。其当前值可通过 value 属性检索。
 *
 * **状态流永远不会完成**
 * 对状态流的 Flow.collect 调用永远不会正常完成，由 Flow.launchIn 函数启动的协程也不会正常完成。状态流的活动收集器称为订阅者。
 *
 * ### 可变状态流
 * 是使用具有初始值的 MutableStateFlow(value) 构造函数创建的。可变状态流的值可以通过设置其 value 属性来更新。对值的更新总是合并的。因此，慢速收集器会跳过快速更新，但始终收集最近发出的值。
 *
 * ### StateFlow
 * 可用作表示任何类型状态的数据模型类。可以使用流上的各种运算符定义派生值，其中组合运算符特别适用于使用任意函数组合来自多个状态流的值。
 * 例如，以下类封装了一个整数状态，并在每次调用 inc 时增加其值：

 *
 * ```
 * class CounterModel {
 *     private val _counter = MutableStateFlow(0) // private mutable state flow
 *     val counter = _counter.asStateFlow() // publicly exposed as read-only state flow
 *
 *     fun inc() {
 *         _counter.update { count -> count + 1 } // atomic, safe for concurrent use
 *     }
 * }
 * ```
 *
 * Having two instances of the above `CounterModel` class one can define the sum of their counters like this:
 *
 * ```
 * val aModel = CounterModel()
 * val bModel = CounterModel()
 * val sumFlow: Flow<Int> = aModel.counter.combine(bModel.counter) { a, b -> a + b }
 * ```
 *
 * As an alternative to the above usage with the `MutableStateFlow(...)` constructor function,
 * any _cold_ [Flow] can be converted to a state flow using the [stateIn] operator.
 *
 * ### Strong equality-based conflation
 *
 * Values in state flow are conflated using [Any.equals] comparison in a similar way to
 * [distinctUntilChanged] operator. It is used to conflate incoming updates
 * to [value][MutableStateFlow.value] in [MutableStateFlow] and to suppress emission of the values to collectors
 * when new value is equal to the previously emitted one. State flow behavior with classes that violate
 * the contract for [Any.equals] is unspecified.
 *
 * ### State flow is a shared flow
 *
 * State flow is a special-purpose, high-performance, and efficient implementation of [SharedFlow] for the narrow,
 * but widely used case of sharing a state. See the [SharedFlow] documentation for the basic rules,
 * constraints, and operators that are applicable to all shared flows.
 *
 * State flow always has an initial value, replays one most recent value to new subscribers, does not buffer any
 * more values, but keeps the last emitted one, and does not support [resetReplayCache][MutableSharedFlow.resetReplayCache].
 * A state flow behaves identically to a shared flow when it is created
 * with the following parameters and the [distinctUntilChanged] operator is applied to it:
 *
 * ```
 * // MutableStateFlow(initialValue) is a shared flow with the following parameters:
 * val shared = MutableSharedFlow(
 *     replay = 1,
 *     onBufferOverflow = BufferOverflow.DROP_OLDEST
 * )
 * shared.tryEmit(initialValue) // emit the initial value
 * val state = shared.distinctUntilChanged() // get StateFlow-like behavior
 * ```
 *
 * Use [SharedFlow] when you need a [StateFlow] with tweaks in its behavior such as extra buffering, replaying more
 * values, or omitting the initial value.
 *
 * ### StateFlow vs ConflatedBroadcastChannel
 *
 * Conceptually, state flow is similar to [ConflatedBroadcastChannel]
 * and is designed to completely replace it.
 * It has the following important differences:
 *
 * * `StateFlow` is simpler, because it does not have to implement all the [Channel] APIs, which allows
 *   for faster, garbage-free implementation, unlike `ConflatedBroadcastChannel` implementation that
 *   allocates objects on each emitted value.
 * * `StateFlow` always has a value which can be safely read at any time via [value] property.
 *    Unlike `ConflatedBroadcastChannel`, there is no way to create a state flow without a value.
 * * `StateFlow` has a clear separation into a read-only `StateFlow` interface and a [MutableStateFlow].
 * * `StateFlow` conflation is based on equality like [distinctUntilChanged] operator,
 *    unlike conflation in `ConflatedBroadcastChannel` that is based on reference identity.
 * * `StateFlow` cannot be closed like `ConflatedBroadcastChannel` and can never represent a failure.
 *    All errors and completion signals should be explicitly _materialized_ if needed.
 *
 * `StateFlow` is designed to better cover typical use-cases of keeping track of state changes in time, taking
 * more pragmatic design choices for the sake of convenience.
 *
 * To migrate [ConflatedBroadcastChannel] usage to [StateFlow], start by replacing usages of the `ConflatedBroadcastChannel()`
 * constructor with `MutableStateFlow(initialValue)`, using `null` as an initial value if you don't have one.
 * Replace [send][ConflatedBroadcastChannel.send] and [trySend][ConflatedBroadcastChannel.trySend] calls
 * with updates to the state flow's [MutableStateFlow.value], and convert subscribers' code to flow operators.
 * You can use the [filterNotNull] operator to mimic behavior of a `ConflatedBroadcastChannel` without initial value.
 *
 * ### Concurrency
 *
 * All methods of state flow are **thread-safe** and can be safely invoked from concurrent coroutines without
 * external synchronization.
 *
 * ### Operator fusion
 *
 * Application of [flowOn][Flow.flowOn], [conflate][Flow.conflate],
 * [buffer] with [CONFLATED][Channel.CONFLATED] or [RENDEZVOUS][Channel.RENDEZVOUS] capacity,
 * [distinctUntilChanged][Flow.distinctUntilChanged], or [cancellable] operators to a state flow has no effect.
 *
 * ### Implementation notes
 *
 * State flow implementation is optimized for memory consumption and allocation-freedom. It uses a lock to ensure
 * thread-safety, but suspending collector coroutines are resumed outside of this lock to avoid dead-locks when
 * using unconfined coroutines. Adding new subscribers has `O(1)` amortized cost, but updating a [value] has `O(N)`
 * cost, where `N` is the number of active subscribers.
 *
 * ### Not stable for inheritance
 *
 * **`The StateFlow` interface is not stable for inheritance in 3rd party libraries**, as new methods
 * might be added to this interface in the future, but is stable for use.
 * Use the `MutableStateFlow(value)` constructor function to create an implementation.
 */

fun main() {
    val aModel = CounterModel()
    val bModel = CounterModel()
    val sumFlow: Flow<Int> = aModel.counter.combine(bModel.counter) { a, b ->
        a + b
    }

//    CoroutineScope(Dispatchers.Default).launch {
//        sumFlow.collect {
//            println(it)
//        }
//        aModel.inc()
//    }

}

class CounterModel {
    private val _counter = MutableStateFlow(0) // private mutable state flow
    val counter = _counter.asStateFlow() // publicly exposed as read-only state flow

    fun inc() {
        _counter.update { count ->
            count + 1
        } // atomic, safe for concurrent use
    }
}

