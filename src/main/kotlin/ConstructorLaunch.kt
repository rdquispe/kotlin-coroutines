package sequential

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    runBlocking {
        log("Start of runBlocking")
        launch {
            log("Before delay.")
            delay(1000)
            log("After delay.")
        }
        log("End of runBlocking")
    }

    log("End")
}