package suspendable_functions

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.system.measureTimeMillis

fun main() {
    log("Start")

    var launchOneTimeMillis: Long = 0
    var launchTwoTimeMillis: Long = 0

    val totalTime = measureTimeMillis {
        runBlocking {
            log("RunBlocking start")

            launch {
                launchOneTimeMillis = measureTimeMillis {
                    log("Launch 1 start")
                    myBlockingSuspendFunction()
                    log("Launch 1 end")
                }
            }

            launch {
                launchTwoTimeMillis = measureTimeMillis {
                    log("Launch 2 start")
                    repeat(5) {
                        delay(600)
                        log("Launch 2: repetition $it")
                    }
                    log("Launch 2 end")
                }
            }

            log("RunBlocking end")
        }
    }

    log("Times: Total = $totalTime | Launch 1 = $launchOneTimeMillis | Launch 2 = $launchTwoTimeMillis")
    log("End")

}

suspend fun myBlockingSuspendFunction() {
    log("MyFunction start")
    Thread.sleep(3000) //Simulating a heavy blocking task.
    log("MyFunction end")
}