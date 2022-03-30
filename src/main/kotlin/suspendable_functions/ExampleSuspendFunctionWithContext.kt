package suspendable_functions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import log
import kotlin.system.measureTimeMillis

fun main() {
    log("Start")

    val time = measureTimeMillis {
        runBlocking {
            launch(Dispatchers.Default) {
                log("Launch start")
                val r1 = myRandomNumber()
                val r2 = myRandomNumber()
                val r3 = myRandomNumber()
                log("Launch Result = ${r1 + r2 + r3} | R1 = $r1 | R2 = $r2 | R3 = $r3")
                log("Launch end")
            }
        }
    }

    log("Total Time = $time milliseconds.")
    log("End")
}

suspend fun myRandomNumber(): Int = withContext(Dispatchers.Default) {
    log("Retrieving random number...")
    delay(2000) //Simulating a heavy computation
    val random = (1..100).random()
    log("Random Number = $random")
    random
}