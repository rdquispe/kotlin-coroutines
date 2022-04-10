package channels_contructor_produce

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext


class MyProducer : CoroutineScope {
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("MyProducer CoroutineExceptionHandler Caught: [ $exception ]")
    }
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun produceNumbers() = produce {
        (1..100).forEach {
            if (it == 5)
                throw ArithmeticException("Producer throwing exception.")

            delay(500)
            log("Producer: Sending number $it")
            send(it)
        }
    }

    fun printRandomNumbers() = launch {
        log("Launch start")
        repeat(1000) {
            delay(400)
            log("Launch: Random Number -> ${(1..100).random()}")
        }
        log("Launch end")
    }

    fun release() {
        this.job.cancel()
    }
}

fun main() {
    log("Start")

    val myProducer = MyProducer()

    runBlocking {
        log("RunBlocking start")

        val job = myProducer.printRandomNumbers()
        val channel = myProducer.produceNumbers()

        job.invokeOnCompletion { exception ->
            if (exception != null)
                log("Job printRandomNumbers On Completion : [ $exception ]")
        }

        try {
            for (i in channel) {
                log("RunBlocking: Number $i received!")
            }
        } catch (e: Exception) {
            log("RunBlocking: Exception Caught: [ $e ]")
        }

        log("RunBlocking: Before delaying for 3 seconds...")
        delay(3000)
        myProducer.release()

        log("RunBlocking end")
    }

    log("End")
}