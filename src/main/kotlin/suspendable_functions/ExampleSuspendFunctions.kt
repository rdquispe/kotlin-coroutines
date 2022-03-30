package suspendable_functions

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {
        log("RunBlocking start")
        mySuspendFunction()
        log("RunBlocking end")
    }

    log("End")
}

suspend fun mySuspendFunction() {
    log("MyFunction start")
    repeat(5) {
        delay(300)
        log("MyFunction: repetition $it")
    }
    log("MyFunction end")
}