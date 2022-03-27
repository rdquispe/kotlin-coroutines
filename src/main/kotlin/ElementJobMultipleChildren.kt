import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Cancel padre
fun main() {
    log("Start")

    runBlocking {
        var childJob1: Job? = null
        var childJob2: Job? = null
        var childChildJob: Job? = null
        val job = launch {

            childJob1 = launch {

                childChildJob = launch {
                    repeat(5) {
                        log("Child1-Child rep #$it : I'm active")
                        delay(1000)
                    }
                    log("Child1-Child : I'm finishing.")
                }

                repeat(5) {
                    log("Child 1 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 1 : I'm finishing.")
            }

            childJob2 = launch {
                repeat(5) {
                    log("Child 2 rep #$it : I'm active")
                    delay(1000)
                }
                log("Child 2 : I'm finishing.")
            }

            log("Launch : I'm finishing.")
        }

        delay(1500)
        while (job.isActive) {
            log("RunBlocking : Job is active")
            delay(1000)
            log("RunBlocking : Cancelling Job")
            job.cancel()
        }

        if (childJob1?.isCancelled == true) {
            log("RunBlocking : Child Job 1 is cancelled")
        }

        if (childJob1?.isCompleted == true) {
            log("RunBlocking : Child Job 1 is completed")
        }

        if (childChildJob?.isCancelled == true) {
            log("RunBlocking : Child-Child Job is cancelled")
        }

        if (childChildJob?.isCompleted == true) {
            log("RunBlocking : Child-Child Job is completed")
        }

        if (childJob2?.isCancelled == true) {
            log("RunBlocking : Child Job 2 is cancelled")
        }

        if (childJob2?.isCompleted == true) {
            log("RunBlocking : Child Job 2 is completed")
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