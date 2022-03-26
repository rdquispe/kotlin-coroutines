package sequential

import kotlin.concurrent.thread

fun main() {
    log("Start")

    thread {
        Thread.sleep(1000)
        log("After Thread.sleep.")
    }

    log("End")
}