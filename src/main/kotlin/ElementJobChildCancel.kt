import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    log("Start")

    runBlocking {
        val job = launch {

            val childJob = launch {
                repeat(10) {
                    log("Child rep #$it : I'm active")
                    delay(1000)
                }

                log("Child : I'm finishing.")
            }

            delay(2500)
            while (childJob.isActive) {
                log("Launch : Job is active")
                delay(1000)
                log("Launch : Cancelling Job")
                childJob.cancel()
            }

            if (childJob.isCancelled) {
                log("Launch : Job is cancelled")
            }

            if (childJob.isCompleted) {
                log("Launch : Job is completed")
            }

            log("Launch : I'm finishing.")
        }

        delay(200)
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