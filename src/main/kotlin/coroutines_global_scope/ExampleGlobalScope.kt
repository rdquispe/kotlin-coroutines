package coroutines_global_scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext

// TODO: Usar el global scope es peligroso usarlo con precaucion
fun main() = runBlocking {
    log("Start")

    val stepTime: Long = 2500
    val obj = FileReader()

    log("Reading files")
    obj.readFileWithLocalScope("Users.txt")
    obj.readFileWithGlobalScope("Customers.txt")

    log("Waiting for $stepTime milliseconds...")
    delay(stepTime)

    log("Reading another file")
    obj.readFileWithGlobalScope("Services.txt")

    log("Waiting for $stepTime milliseconds...")
    delay(stepTime)

    log("Releasing resources...")
    obj.release()
    log("Resources have been released!")

    log("Waiting for $stepTime milliseconds before closing...")
    delay(stepTime)

    log("End")
}

class FileReader : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun readFileWithLocalScope(filename: String) = launch {
        for (i in 1..Int.MAX_VALUE) {
            delay(500)
            log("Reading file \"$filename\" with local scope: Line #$i")
        }
    }

    fun readFileWithGlobalScope(filename: String) = GlobalScope.launch {
        for (i in 1..Int.MAX_VALUE) {
            delay(500)
            log("Reading file \"$filename\" with global scope: Line #$i")
        }
    }

    fun release() {
        this.job.cancel()
    }

}