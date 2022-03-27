import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    log("Start")

    val myJob = Job()

    runBlocking {

        val job1 = launch(myJob) {
            repeat(5) {
                log("Launch 1 rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch 1 : I'm finishing.")
        }

        val job2 = launch(myJob) {
            repeat(5) {
                log("Launch 2 rep #$it : I'm active.")
                delay(1000)
            }

            log("Launch 2 : I'm finishing.")
        }

        launch {
            delay(1500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        delay(100)
        while (myJob.isActive) {
            log("RunBlocking : MyJob is active.")
            delay(1000)
        }

        if (job1.isActive) {
            log("RunBlocking : Job 1 is isActive")
        }

        if (job1.isCancelled) {
            log("RunBlocking : Job 1 is cancelled")
        }

        if (job1.isCompleted) {
            log("RunBlocking : Job 1 is completed")
        }

        if (job2.isActive) {
            log("RunBlocking : Job 2 is isActive")
        }

        if (job2.isCancelled) {
            log("RunBlocking : Job 2 is cancelled")
        }

        if (job2.isCompleted) {
            log("RunBlocking : Job 2 is completed")
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