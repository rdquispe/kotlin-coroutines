import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(3) {
                log("Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        log("RunBlocking : Before calling .join()")
        job.join()
        log("RunBlocking : Job is not active")
    }

    log("End")
}