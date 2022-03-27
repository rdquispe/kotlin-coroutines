package element_continuation_interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import log

fun main() {
    log("Start")

    runBlocking(newSingleThreadContext("MyOuterThread")) {

        log("RunBlocking: Step 1")

        withContext(newSingleThreadContext("MyInnerThread")) {
            log("WithContext 1: Starting...")
            delay(1000)
            log("WithContext 1: Finishing...")
        }

        log("RunBlocking: Step 2")

        withContext(Dispatchers.Default) {
            log("WithContext 2: Starting...")
            delay(1000)
            log("WithContext 2: Finishing...")
        }

        log("RunBlocking: Step 3")

    }

    log("End")
}