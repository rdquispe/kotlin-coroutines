package supervisor_job_and_scope

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import log

fun main() {
    log("Start")

    runBlocking {

        log("RunBlocking start")

        val handler = CoroutineExceptionHandler { _, exception ->
            log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
        }

        supervisorScope {

            val jobA = launch(Dispatchers.Default + handler) {
                repeat(5) {
                    delay(1000)
                    log("Job A: $it")
                }
            }

            val jobB = launch(Dispatchers.Default + handler) {
                log("Job B start")
                delay(3000)
                log("Job B: before throwing exception")
                throw IndexOutOfBoundsException("Job B: Working with coroutine exceptions.")
            }

            jobA.invokeOnCompletion { exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
            jobB.invokeOnCompletion { exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }

            repeat(6) {
                delay(700)
                log("RunBlocking: $it")
            }
        }

        log("RunBlocking end")
    }

    log("End")
}