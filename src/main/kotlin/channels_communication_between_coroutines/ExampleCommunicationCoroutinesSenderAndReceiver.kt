package channels_communication_between_coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import log

class Sender {
    companion object {
        const val MESSAGES_AMOUNT = 1000
    }

    suspend fun processAction(channel: SendChannel<Int>) = withContext(Dispatchers.Default) {
        repeat(MESSAGES_AMOUNT) {
            channel.send((1..100).random())
        }
    }
}

class Receiver {
    var messagesAmount = 0
        private set

    suspend fun processAction(channel: ReceiveChannel<Int>) = withContext(Dispatchers.Default) {
        for (i in channel) {
            messagesAmount ++
        }
    }
}

fun main() {
    log("Start")

    val channel = Channel<Int>()
    val sendersAmount = 100
    val receiversAmount = 5

    val amounts = IntArray(receiversAmount)

    runBlocking {
        launch {
            val senders = List(sendersAmount) {
                val sender = Sender()
                launch { sender.processAction(channel) }
            }

            senders.forEach { it.join() }
            channel.close()
            log("Senders finished!")
        }

        launch {
            val receivers = List(receiversAmount) {
                val receiver = Receiver()
                launch {
                    receiver.processAction(channel)
                    amounts[it] = receiver.messagesAmount
                }
            }

            receivers.forEach { it.join() }
            log("Receivers finished!")
        }

    }

    var total = 0
    log("----- AMOUNTS ------")
    amounts.forEachIndexed { index, amount ->
        total += amount
        log("Index #$index: $amount")
    }
    log("--------------------")
    log("TOTAL = $total")
    log("--------------------")

    if (total == sendersAmount * Sender.MESSAGES_AMOUNT) {
        log("Final State: SUCCESS")
    } else {
        log("Final State: FAIL")
    }

    log("--------------------")
    log("End")
}