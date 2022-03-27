import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    log("Start")

    runBlocking {
        log("Before delay.")
        delay(1000)
        log("After delay.")
    }

    log("End")
}

