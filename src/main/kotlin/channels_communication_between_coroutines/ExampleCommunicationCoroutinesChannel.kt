package channels_communication_between_coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import log

fun main() {
    log("Start")

    val mutex = Mutex()

    val channel = Channel<Int>()

    var produced = 0
    var consumed = 0

    val amounts = IntArray(5)

    runBlocking {
        launch(Dispatchers.Default) {
            val producers = List(100_000) {
                launch {
                    val number = (1..100).random()
                    channel.send(number)
                    mutex.withLock {
                        produced ++
                    }
                }
            }

            producers.forEach { it.join() }
            channel.close()
            log("Producers finished!")
        }

        launch(Dispatchers.Default) {
            val consumers = List(amounts.size) {
                launch {
                    for (i in channel) {
                        mutex.withLock {
                            consumed ++
                            amounts[it] ++
                        }
                    }
                }
            }

            consumers.forEach { it.join() }
            log("Consumers finished!")
        }
    }

    log("Produced: $produced")
    log("Consumed: $consumed")

    var total = 0
    log("----- AMOUNTS ------")
    amounts.forEachIndexed { index, amount ->
        total += amount
        log("Index #$index: $amount")
    }
    log("--------------------")
    log("TOTAL = $total")
    log("--------------------")

    log("End")
}