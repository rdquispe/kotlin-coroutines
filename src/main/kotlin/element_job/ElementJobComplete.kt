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

        delay(100)
        while (job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
        }

        if (job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if (job.isCompleted) {
            log("RunBlocking : Job is completed")
        }
    }

    log("End")
}