import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    runBlocking {
        var childJob: Job? = null
        val job = launch {

            childJob = launch {
                repeat(10) {
                    log("Child rep #$it : I'm active")
                    delay(1000)
                }

                log("Child : I'm finishing.")
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

        if (childJob?.isCancelled == true) {
            log("RunBlocking : Child Job is cancelled")
        }

        if (childJob?.isCompleted == true) {
            log("RunBlocking : Child Job is completed")
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