package element_coroutine_exception_handler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext

fun main() {
    log("Start")

    val myObject = MyObject5()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()
        val jobB = myObject.mySecondTask()

        jobA.invokeOnCompletion { exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
        jobB.invokeOnCompletion { exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }

        joinAll(jobA, jobB)
        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObject5 : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTask() = launch {

        log("Job A start")

        val jobA1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job A1: repetition $it")
            }
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

    fun mySecondTask() = launch {
        log("Job B start")

        val jobB1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job B1: repetition $it")
            }
        }
        jobB1.invokeOnCompletion { exception ->
            if (exception != null) log("Job B1 On Completion - Exception: [ $exception ]")
        }

        jobB1.join()
        log("Job B end")
    }

    fun release() {
        this.job.cancel()
    }

}