import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

fun main() {
    val aContext = CoroutineName("A")
    val bContext = CoroutineName("B")
    val result = aContext + bContext
    println("Context right: $result")

    runBlocking(CoroutineName("RunBlocking")) {

        log(
            "runBlocking Context | " +
                "Job: ${this.coroutineContext[Job]} , " +
                "CoroutineContext: ${this.coroutineContext} , " +
                "CoroutineName: ${this.coroutineContext[CoroutineName]}"
        )

        val runBlockingContextAndName1 = this.coroutineContext + CoroutineName("New Name")

        log(
            "runBlocking Context | " +
                "Job: ${runBlockingContextAndName1[Job]} , " +
                "RunBlockingContextAndName1: $runBlockingContextAndName1 , " +
                "CoroutineName: ${runBlockingContextAndName1[CoroutineName]}"
        )

        val runBlockingContextAndName2 = CoroutineName("New Name") + this.coroutineContext

        log(
            "runBlocking Context | " +
                "Job: ${runBlockingContextAndName2[Job]} , " +
                "RunBlockingContextAndName2: $runBlockingContextAndName2 , " +
                "CoroutineName: ${runBlockingContextAndName2[CoroutineName]}"
        )
    }
}