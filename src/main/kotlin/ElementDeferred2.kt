import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    runBlocking {
        val result = async {
            log("Async : I'm starting.")
            val a = (1..50).random()
            log("Async : A = $a")
            delay(1000)
            val b = (1..50).random()
            log("Async : B = $b")
            delay(1000)
            log("Async : I'm finishing.")
            a + b
        }

        log("RunBlocking : Before calling .await()")
        log("RunBlocking : Result is ${result.await()}")
    }

    log("End")
}