package element_continuation_interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.ContinuationInterceptor

fun main() {
    log("Start")

    runBlocking {

        launch(Dispatchers.Default) {

            log("Parent-Before | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

            launch {
                log("Child | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")
            }

            log("Parent-After | ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]}")

        }

    }

    log("End")
}