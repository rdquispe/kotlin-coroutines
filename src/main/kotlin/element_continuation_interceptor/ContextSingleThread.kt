package element_continuation_interceptor

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {

        launch(newSingleThreadContext("MyThread")) {

            repeat(5) {
                log("Launch: $it")
                delay(50)
            }

        }

    }

    log("End")
}