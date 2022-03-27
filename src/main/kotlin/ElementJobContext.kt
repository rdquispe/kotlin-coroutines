import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.ContinuationInterceptor


// Job propio termina en ejecutarse
fun main() {
    log("Start")

    val myJob = Job()

    log("MyJob: $myJob")

    runBlocking {
        log(
            "RunBlocking | " +
                "Job: ${this.coroutineContext[Job]} , " +
                "ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]} , " +
                "CoroutineExceptionHandler: ${this.coroutineContext[CoroutineExceptionHandler]} , " +
                "CoroutineName: ${this.coroutineContext[CoroutineName]}"
        )

        launch(myJob) {
            log(
                "Launch | " +
                    "Job: ${this.coroutineContext[Job]} , " +
                    "ContinuationInterceptor: ${this.coroutineContext[ContinuationInterceptor]} , " +
                    "CoroutineExceptionHandler: ${this.coroutineContext[CoroutineExceptionHandler]} , " +
                    "CoroutineName: ${this.coroutineContext[CoroutineName]}"
            )

            delay(1000)
            log("Launch : I'm finishing.")
        }

        launch {
            delay(5500)
            log("Before cancelling MyJob.")
            myJob.cancel()
        }

        delay(100)
        while (myJob.isActive) {
            log("RunBlocking : MyJob is active.")
            delay(1000)
        }

        log("RunBlocking : MyJob is not active.")
    }

    log("End")
}