package channels_contructor_produce

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext

// TODO: Si alguno de los receptores falla mientras está obteniendo los datos con la función consumeEach, provocará que el canal se cierre
class Sender : CoroutineScope {

    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun processData(): ReceiveChannel<Int> = produce {
        (1..10).forEach {
            delay(500)
            log("Sender: Sending $it...")
            send(it)
        }
    }

}

class Receiver(private val id: Int) : CoroutineScope {

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Receiver #$id Exception Caught: [ $exception ]")
    }
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun processData(channel: ReceiveChannel<Int>, fail: Boolean) = launch {
        var i = 0
        channel.consumeEach {
            i ++
            if (fail && i == 3)
                throw ArithmeticException("Receiver #$id: Playing with exceptions.")

            log("Receiver #$id: $it received!")
        }
    }

}

fun main() {
    log("Start")

    val sender = Sender()
    val receiver1 = Receiver(1)
    val receiver2 = Receiver(2)

    runBlocking {
        val channel = sender.processData()
        val jobReceiver1 = receiver1.processData(channel, false)
        val jobReceiver2 = receiver2.processData(channel, true)

        joinAll(jobReceiver1, jobReceiver2)
    }

    log("End")
}