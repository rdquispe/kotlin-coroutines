package sequential

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    runBlocking {
        log("Start of runBlocking")
        val number = async {
            log("Before delay.")
            delay(1000)
            log("After delay.")
            (1..100).random()
        }
        log("Continue with runBlocking...")
        log("End of runBlocking with value ${number.await()}")
    }

    log("End")
}