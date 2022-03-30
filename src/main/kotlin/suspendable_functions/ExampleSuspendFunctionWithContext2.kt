package suspendable_functions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
                val def1 = async { myRandomNumber2() }
                val def2 = async { myRandomNumber2() }
                val def3 = async { myRandomNumber2() }
                val r1 = def1.await()
                val r2 = def2.await()
                val r3 = def3.await()
                log("Launch Result = ${r1 + r2 + r3} | R1 = $r1 | R2 = $r2 | R3 = $r3")
                log("Launch end")
            }
        }
    }

    log("Total Time = $time milliseconds.")
    log("End")
}

suspend fun myRandomNumber2(): Int = withContext(Dispatchers.Default) {
    log("Retrieving random number...")
    delay(2000) //Simulating a heavy computation
    val random = (1..100).random()
    log("Random Number = $random")
    random
}