package channels_contructor_produce

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {

        val channel = Channel<Int>()

        launch {
            (1..10).forEach {
                delay(500)
                channel.send(it)
            }
            channel.close()
        }

        launch {
            for (i in channel) {
                log("Received: $i")
            }
        }

    }

    log("End")
}