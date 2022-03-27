package element_continuation_interceptor

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    val repetitions = 5

    runBlocking {
        launch {
            repeat(repetitions) {
                log("Launch 1: $it")
                delay(50)
            }
        }

        launch {
            repeat(repetitions) {
                log("Launch 2: ${repetitions - it}")
                delay(50)
            }
        }
    }

    log("End")
}