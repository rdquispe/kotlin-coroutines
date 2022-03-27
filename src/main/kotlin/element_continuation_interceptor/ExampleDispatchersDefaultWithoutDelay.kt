package element_continuation_interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {
        val repetitions = 100

        launch(Dispatchers.Default) {
            repeat(repetitions) {
                log("Launch 1: $it")
            }
        }

        launch(Dispatchers.Default) {
            repeat(repetitions) {
                log("Launch 2: ${repetitions - it}")
            }
        }
    }

    log("End")
}