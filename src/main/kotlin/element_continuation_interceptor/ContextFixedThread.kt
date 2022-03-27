package element_continuation_interceptor

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {

        launch(newFixedThreadPoolContext(10, "MyThreadPool")) {

            repeat(5) {
                log("Launch: $it")
                delay(50)
            }

        }

    }

    log("End")
}