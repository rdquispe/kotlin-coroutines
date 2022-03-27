package element_coroutine_exception_handler

import kotlinx.coroutines.CancellationException
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

    val myObject = MyObject()

    runBlocking {
        log("RunBlocking start")

        val job = myObject.myTask()
        job.join()

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObject : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myTask() = launch {
        delay(1000)
        log("MyTask: before throwing exception")
        throw CancellationException("MyTask: Working with coroutine exceptions.")
//        throw IndexOutOfBoundsException("MyTask: Working with coroutine exceptions.")
    }

    fun release() {
        this.job.cancel()
    }

}