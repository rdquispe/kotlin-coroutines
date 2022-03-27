package element_continuation_interceptor

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {

        val myThreadPoolContext = newFixedThreadPoolContext(10, "MyThreadPool")

        val job = launch(myThreadPoolContext) {

            repeat(5) {
                log("Launch: $it")
                delay(50)
            }

        }

        job.join()

        myThreadPoolContext.close()

    }

    log("End")
}