package element_continuation_interceptor

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {

        val myThreadContext = newSingleThreadContext("MyThread")

        val job = launch(myThreadContext) {

            repeat(5) {
                log("Launch: $it")
                delay(50)
            }

        }

        job.join()

        myThreadContext.close()

    }

    log("End")
}