package channels_communication_between_coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import log
import java.util.concurrent.ConcurrentLinkedQueue

fun main() {
    log("Start")

    val mutex = Mutex()

    val queue = ConcurrentLinkedQueue<Int>()
    var closed = false

    var produced = 0
    var consumed = 0

    val amounts = IntArray(5)

    runBlocking {
        launch(Dispatchers.Default) {
            val producers = List(100_000) {
                launch {
                    val number = (1..100).random()
                    if (queue.offer(number)) {
                        mutex.withLock {
                            produced ++
                        }
                    }
                }
            }

            producers.forEach { it.join() }
            closed = true
            log("Producers finished!")
        }

        launch(Dispatchers.Default) {
            val consumers = List(amounts.size) {
                launch {
                    while (! closed || ! queue.isEmpty()) {
                        if (! queue.isEmpty()) {
                            val number = queue.poll()
                            if (number != null) {
                                mutex.withLock {
                                    consumed ++
                                    amounts[it] ++
                                }
                            }
                        }
                    }
                }
            }

            consumers.forEach { it.join() }
            log("Consumers finished!")
        }
    }

    log("Queue size: ${queue.size}")
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