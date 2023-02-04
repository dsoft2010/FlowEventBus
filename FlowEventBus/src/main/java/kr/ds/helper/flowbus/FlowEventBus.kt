package kr.ds.helper.flowbus

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

object FlowEventBus {
    private val mutableEvents = MutableSharedFlow<Any>()
    val events = mutableEvents.asSharedFlow()

    fun publish(event: Any) {
        CoroutineScope(Dispatchers.Default).launch {
            mutableEvents.emit(event)
        }
    }

    suspend inline fun <reified T> subscribe(crossinline onEvent: (T) ->Unit) {
        return events.filterIsInstance<T>().collect {
            onEvent(it)
        }
    }
}