package channels_contructor_produce

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log

fun main() {
    log("Start")

    runBlocking {

        val channel = produce {
            (1..10).forEach {
                delay(500)
                send(it)
            }
        }

        launch {
            for (i in channel) {
                log("Received: $i")
            }
        }

    }

    log("End")
}