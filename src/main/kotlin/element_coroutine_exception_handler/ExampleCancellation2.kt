package element_coroutine_exception_handler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext

fun main() {
    log("Start")

    val myObject = MyObject2()

    runBlocking {
        log("RunBlocking start")

        val job1 = myObject.myFirstTask()
        val job2 = myObject.mySecondTask()

        job1.invokeOnCompletion { exception ->
            if (exception != null) log("Job 1 On Completion - Exception: [ $exception ]")
        }

        job2.invokeOnCompletion { exception ->
            if (exception != null) log("Job 2 On Completion - Exception: [ $exception ]")
        }

        joinAll(job1, job2)

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObject2 : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTask() = launch {
        delay(3000)
        log("MyTask 1: before throwing exception")
        throw IndexOutOfBoundsException("MyTask: Working with coroutine exceptions.")
    }

    fun mySecondTask() = launch {
        (1..Int.MAX_VALUE).forEach {
            delay(1000)
            log("MyTask 2: repetition $it")
        }
    }

    fun release() {
        this.job.cancel()
    }

}