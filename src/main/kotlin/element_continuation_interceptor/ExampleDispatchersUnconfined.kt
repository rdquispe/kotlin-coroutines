package element_continuation_interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {
        val repetitions = 5

        launch (Dispatchers.Unconfined) {
            repeat(repetitions) {
                log("Launch 1: $it")

                if(it == 2) {
                    log("Launch 1: Before suspending coroutine...")
                    delay(50)
                    log("Launch 1: After suspending coroutine...")
                }
            }
        }
    }

    log("End")
}