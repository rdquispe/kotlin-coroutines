package supervisor_job_and_scope

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import log
import kotlin.coroutines.CoroutineContext

fun main() {
    log("Start")

    val myObject = MyObjectExampleScope()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()

        jobA.invokeOnCompletion { exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }

        jobA.join()

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObjectExampleScope : CoroutineScope {

    private val job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch {
        log("Job A start")

        supervisorScope {
            val jobA1 = launch {
                repeat(5) {
                    delay(1000)
                    log("Job A1: $it")
                }
            }

            val jobA2 = launch {
                log("Job A2 start")
                delay(3000)
                log("Job A2: before throwing exception")
                throw IndexOutOfBoundsException("Job A2: Working with coroutine exceptions.")
            }

            jobA1.invokeOnCompletion { exception -> if (exception != null) log("Job A1 On Completion - Exception: [ $exception ]") }
            jobA2.invokeOnCompletion { exception -> if (exception != null) log("Job A2 On Completion - Exception: [ $exception ]") }

            repeat(6) {
                delay(700)
                log("Job A: $it")
            }
        }

        log("Job A end")
    }

    fun release() {
        this.job.cancel()
    }

}