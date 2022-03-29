package supervisor_job_and_scope

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext

class MyObjectExampleJob : CoroutineScope {

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch {

        log("Job A start")

        val jobA1 = launch {
            log("Job A1 start")
            delay(5000)
            log("Job A1 end")
        }

        val jobA2 = async<Int> {
            log("Job A2 start")
            delay(3000)
            log("Job A2: before throwing exception")
            throw IndexOutOfBoundsException("Job A2: Working with coroutine exceptions.")
        }

        jobA1.invokeOnCompletion { exception -> if (exception != null) log("Job A1 On Completion - Exception: [ $exception ]") }
        jobA2.invokeOnCompletion { exception -> if (exception != null) log("Job A2 On Completion - Exception: [ $exception ]") }

        joinAll(jobA1, jobA2)
        log("Job A end")
    }

    fun mySecondTaskAsync() = async {
        log("Job B start")

        val jobB1 = launch {
            log("Job B1 start")
            delay(5000)
            log("Job B1 end")
        }

        jobB1.invokeOnCompletion { exception -> if (exception != null) log("Job B1 On Completion - Exception: [ $exception ]") }

        jobB1.join()

        log("Job B end")
        (1..100).random()
    }

    fun myThirdTask() = launch(Job()) {

        log("Job C start")

        val jobC1 = async {
            log("Job C1 start")
            delay(5000)
            log("Job C1 end")
            (1..100).random()
        }

        val jobC2 = launch {
            log("Job C2 start")
            delay(5000)
            log("Job C2 end")
        }

        jobC1.invokeOnCompletion { exception -> if (exception != null) log("Job C1 On Completion - Exception: [ $exception ]") }
        jobC2.invokeOnCompletion { exception -> if (exception != null) log("Job C2 On Completion - Exception: [ $exception ]") }

        try {
            log("Job C: Job C1 deferred value contains: ${jobC1.await()}")
        } catch (exception: Exception) {
            log("Job C Caught: [ $exception ]")
        }

        jobC2.join()
        log("Job C end")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val myObject = MyObjectExampleJob()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()
        val jobB = myObject.mySecondTaskAsync()
        val jobC = myObject.myThirdTask()

        jobA.invokeOnCompletion { exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
        jobB.invokeOnCompletion { exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }
        jobC.invokeOnCompletion { exception -> if (exception != null) log("Job C On Completion - Exception: [ $exception ]") }

        try {
            log("RunBlocking: Job B deferred value contains: ${jobB.await()}")
        } catch (exception: Exception) {
            log("RunBlocking Caught: [ $exception ]")
        }

        joinAll(jobA, jobC)

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}