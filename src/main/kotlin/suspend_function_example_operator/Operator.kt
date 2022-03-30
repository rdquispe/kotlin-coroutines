package suspend_function_example_operator

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import log
import kotlin.coroutines.CoroutineContext

class Operator : CoroutineScope {

    companion object {
        const val OPERATION_IS_EVEN = "Operation [Is Even]"
        const val OPERATION_AVERAGE = "Operation [Average]"
        const val OPERATION_SQUARE = "Operation [Square]"
    }

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception Handler Caught: [ $exception ] with suppressed ${exception.suppressed.contentToString()}")
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + exceptionHandler

    fun sumIsEvenAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_IS_EVEN)
        log("$OPERATION_IS_EVEN: Sum = $sum")
        sum % 2 == 0
    }

    fun sumAverageAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_AVERAGE)
        log("$OPERATION_AVERAGE: Sum = $sum")
        sum.toFloat() / 3
    }

    fun sumSquareAsync() = async {
        val sum = sumThreeRandomNumbers(OPERATION_SQUARE)
        log("$OPERATION_SQUARE: Sum = $sum")
        sum * sum
    }

    private suspend fun sumThreeRandomNumbers(operation: String): Int = coroutineScope {
        val r1 = async { myRandomNumberFail(operation) }
        val r2 = async { myRandomNumberSuccess(operation) }
        val r3 = async { myRandomNumberSuccess(operation) }

        r1.await() + r2.await() + r3.await()
    }

    private suspend fun myRandomNumberSuccess(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(2000) //Simulating a heavy computation
        val random = (1..100).random()
        log("$operation Random Number = $random")
        random
    }

    private suspend fun myRandomNumberFail(operation: String): Int = withContext(Dispatchers.Default) {
        log("$operation Retrieving random number...")
        delay(1000) //Simulating a heavy computation
        throw IllegalStateException("$operation Retrieving random number failed.")
    }

    fun release() {
        this.job.cancel()
    }

}

fun main() {
    log("Start")

    val operator = Operator()

    runBlocking {
        val defIsEven = operator.sumIsEvenAsync()
        val defAverage = operator.sumAverageAsync()
        val defSquare = operator.sumSquareAsync()

        launch {
            try {
                log("${Operator.OPERATION_IS_EVEN} Result = ${defIsEven.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_IS_EVEN} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_IS_EVEN}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_AVERAGE} Result = ${defAverage.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_AVERAGE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_AVERAGE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }

        launch {
            try {
                log("${Operator.OPERATION_SQUARE} Result = ${defSquare.await()}")
            } catch (e: Exception) {
                log("${Operator.OPERATION_SQUARE} Caught: [ $e ]")
            } finally {
                log("--- NOTHING CONTAINING \"${Operator.OPERATION_SQUARE}\" SHOULD APPEAR AFTER THIS LINE ---")
                delay(3000)
            }
        }
    }

    operator.release()

    log("End")
}