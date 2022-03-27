import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    val myJob = Job()

    runBlocking {

        val job = launch(myJob) {
            repeat(10) {
                log("Launch rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch : I'm finishing.")
        }

        launch {
            delay(3500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        delay(100)
        while (myJob.isActive) {
            log("RunBlocking : MyJob is active.")
            delay(1000)
        }

        if (job.isCancelled) {
            log("RunBlocking : Job is cancelled")
        }

        if (job.isCompleted) {
            log("RunBlocking : Job is completed")
        }

        if (myJob.isCancelled) {
            log("RunBlocking : MyJob is cancelled")
        }

        if (myJob.isCompleted) {
            log("RunBlocking : MyJob is completed")
        }
    }

    log("End")
}