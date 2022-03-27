package element_coroutine_exception_handler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext

fun main() {
    log("Start")

    val myObject = MyObject4()

    runBlocking {
        log("RunBlocking start")

        val def = myObject.myFirstTaskAsync()
        val job = myObject.mySecondTask()

        job.invokeOnCompletion { exception ->
            if (exception != null) log("Job On Completion - Exception: [ $exception ]")
        }

        try {
            log("RunBlocking: Def value = ${def.await()}")
        } catch (e: Exception) {
            log("RunBlocking Catch: [ ${e.message} ]")
        }

        job.join()

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObject4 : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTaskAsync() = async<Int> {
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