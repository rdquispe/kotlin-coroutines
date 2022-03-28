package element_coroutine_name

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
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

fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}

fun log(character: Char) {
    print("$character")
}

fun main() {
    log("Start")

    val myObject = MyObject2()

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()
        val jobB = myObject.mySecondTask()

        joinAll(jobA, jobB)

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

class MyObject2 : CoroutineScope {

    private val job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun myFirstTask() = launch(CoroutineName("My Coroutine A")) {

        log("Job A start : Name: [ ${this.coroutineContext[CoroutineName]} ]")

        val jobA1 = launch(CoroutineName("My Coroutine A1")) {
            repeat(3) {
                delay(1000)
                log("Job A1: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        val jobA2 = async(CoroutineName("My Coroutine A2")) {
            repeat(3) {
                delay(700)
                log("Job A2: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
            (1..100).random()
        }

        joinAll(jobA1, jobA2)
        log("Job A end : Name: [ ${this.coroutineContext[CoroutineName]} ]")
    }

    fun mySecondTask() = launch(CoroutineName("My Coroutine B")) {
        log("Job B start : Name: [ ${this.coroutineContext[CoroutineName]} ]")

        val jobB1 = launch(CoroutineName("My Coroutine B1")) {
            repeat(7) {
                delay(300)
                log("Job B1: Name: [ ${this.coroutineContext[CoroutineName]} ] : $it")
            }
        }

        jobB1.join()
        log("Job B end : Name: [ ${this.coroutineContext[CoroutineName]} ]")
    }

    fun release() {
        this.job.cancel()
    }

}