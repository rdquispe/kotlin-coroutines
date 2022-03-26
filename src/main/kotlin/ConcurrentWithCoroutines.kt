package sequential

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    log("Start")

    GlobalScope.launch {
        delay(1000)
        log("After Thread.sleep.")
    }

    Thread.sleep(2000) // Adding fix

    log("End")
}