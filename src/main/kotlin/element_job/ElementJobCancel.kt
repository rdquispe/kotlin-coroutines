import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    runBlocking {
        val job = launch {
            repeat(10) {
                log("Launch rep #$it : I'm active")
                delay(1000)
            }
            log("Launch : I'm finishing.")
        }

        delay(2500)
        while (job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
            log("RunBlocking : Cancelling Job")
            job.cancel()
        }

        if (job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        } else {
            log("RunBlocking : Job is not cancelled")
        }
    }

    log("End")
}