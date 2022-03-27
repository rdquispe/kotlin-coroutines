package element_coroutine_exception_handler

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext


fun main() {
    log("Start")

    val myObject = MyObject8()

    runBlocking {
        log("RunBlocking start")

        val job = myObject.myFirstTask()
        job.invokeOnCompletion { exception -> if (exception != null) log("Job On Completion - Exception: [ $exception ]") }
        job.join()

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObject8 : CoroutineScope {

    private val job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler: [ ${exception.message} ]")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch {
        log("Job start")
        delay(1000)
        log("Job: before throwing exception")
        throw IndexOutOfBoundsException("Job: Working with coroutine exceptions.")
    }

    fun release() {
        this.job.cancel()
    }

}