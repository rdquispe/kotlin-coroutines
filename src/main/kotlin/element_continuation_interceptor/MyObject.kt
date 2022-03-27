package element_continuation_interceptor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import log
import kotlin.coroutines.CoroutineContext

class MyObject : CoroutineScope {

    companion object {
        const val WAITING_TIME_MILLIS: Long = 5000
        //const val WAITING_TIME_MILLIS: Long = 2500 // Generate error
    }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private fun notifyUI(number: Int) {
        log("MyObject: UI has been notified with number $number")
    }

    fun simulateLongTask() {
        val myMainThread = newSingleThreadContext("MyMainThread")
        val updaterJob = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("MyObject: Task 1 | Before notifying UI with number $it")
                withContext(myMainThread) {
                    //withContext(Dispatchers.Main) { //Use this CoroutineDispatcher on Android
                    notifyUI(it)
                }
                log("MyObject: Task 1 | After UI notification with number $it")
            }
        }

        updaterJob.invokeOnCompletion { _ ->
            log("MyObject: Task 1 | Closing dedicated thread")
            //myMainThread.close() //Line commented because of lack of permissions
            log("MyObject: Task 1 | Dedicated thread closed!")
        }
    }

    fun simulateDataRetrievalAsync() = async<Int> {
        val repetitions = 10
        repeat(repetitions) {
            delay(300)
            log("MyObject: Task 2 | Simulating data retrieval... ${(it + 1) * 100 / repetitions}%")
        }

        (1..Int.MAX_VALUE).random()
    }

    fun release() {
        log("MyObject: Releasing resources...")
        this.job.cancel()
        log("MyObject: Resources released!!!")
    }

}

fun main() {
    log("Start")

    val myObject = MyObject()

    runBlocking {

        log("RunBlocking start")

        myObject.simulateLongTask()
        val data = myObject.simulateDataRetrievalAsync()

        launch {
            try {
                log("Launch 1: Data retrieved [ ${data.await()} ]")
            } catch (e: Exception) {
                log("Launch 1 Catch [ ${e.message} ]")
            }
        }

        val job = launch {
            log("Launch 2: waiting for ${MyObject.WAITING_TIME_MILLIS / 1000} seconds before releasing resources")
            delay(MyObject.WAITING_TIME_MILLIS)
            log("Launch 2: Releasing 'MyObject'")
            myObject.release()
        }

        job.join()
        log("RunBlocking: waiting for 3 seconds to make sure 'MyObject' has been released. All coroutines should have stopped by now.")
        delay(3000)

        log("RunBlocking end")
    }

    log("End")
}