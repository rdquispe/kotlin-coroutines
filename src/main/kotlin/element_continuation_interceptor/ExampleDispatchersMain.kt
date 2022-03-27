package element_continuation_interceptor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log

// Para Android
fun main() {
    log("Start")

    runBlocking {
        val repetitions = 5

        launch(Dispatchers.Main) {
            repeat(repetitions) {
                log("Launch 1: $it")
            }
        }
    }

    log("End")
}