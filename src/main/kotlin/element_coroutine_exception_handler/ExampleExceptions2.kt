package element_coroutine_exception_handler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import log
import kotlin.coroutines.CoroutineContext

fun main() {
    log("Start")

    val myObject = MyObject6()
    val maxRepetitions = 8

    runBlocking {
        log("RunBlocking start")

        val jobA = myObject.myFirstTask()
        val jobB = myObject.mySecondTask()
        val jobC = myObject.myThirdTask()

        jobA.invokeOnCompletion {exception -> if (exception != null) log("Job A On Completion - Exception: [ $exception ]") }
        jobB.invokeOnCompletion {exception -> if (exception != null) log("Job B On Completion - Exception: [ $exception ]") }
        jobC.invokeOnCompletion {exception -> if (exception != null) log("Job C On Completion - Exception: [ $exception ]") }

        var rep = 0
        while(rep < maxRepetitions && (jobA.isActive || jobB.isActive || jobC.isActive)) {
            delay(1000)
            log("RunBlocking: States: ${getJobsState(jobA, jobB, jobC)}")
            rep++
        }

        log("RunBlocking end")
    }

    myObject.release()

    log("End")
}

fun getJobsState(jobA: Job, jobB: Job, jobC: Job): String {
    val jobAText = if(jobA.isActive) "Job A is active" else if(jobA.isCancelled) "Job A is cancelled" else if(jobA.isCompleted) "Job A is completed" else "Job A Unknown state"
    val jobBText = if(jobB.isActive) "Job B is active" else if(jobB.isCancelled) "Job B is cancelled" else if(jobB.isCompleted) "Job B is completed" else "Job B Unknown state"
    val jobCText = if(jobC.isActive) "Job C is active" else if(jobC.isCancelled) "Job C is cancelled" else if(jobC.isCompleted) "Job C is completed" else "Job C Unknown state"

    return "$jobAText | $jobBText | $jobCText"
}

class MyObject6: CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    fun myFirstTask() = launch {

        log("Job A start")

        val jobA1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job A1: repetition $it")
            }
        }

        val jobA2 = async<Int> {
            log("Job A2 start")
            delay(3000)
            log("Job A2: before throwing exception")
            throw IndexOutOfBoundsException("Job A2: Working with coroutine exceptions.")
        }

        jobA1.invokeOnCompletion {exception -> if (exception != null) log("Job A1 On Completion - Exception: [ $exception ]") }
        jobA2.invokeOnCompletion {exception -> if (exception != null) log("Job A2 On Completion - Exception: [ $exception ]") }

        joinAll(jobA1, jobA2)
        log("Job A end")
    }

    fun mySecondTask() = launch {
        log("Job B start")

        val jobB1 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job B1: repetition $it")
            }
        }
        jobB1.invokeOnCompletion {exception ->
            if (exception != null) log("Job B1 On Completion - Exception: [ $exception ]")
        }

        jobB1.join()
        log("Job B end")
    }

    fun myThirdTask() = launch(Job()) {
        log("Job C start")

        val jobC1 = async {
            log("Job C1 start")

            val repetitions = (1..5).random()
            repeat(repetitions) {
                delay(1000)
                log("Job C1: repetition $it")
            }

            repetitions
        }

        val jobC2 = launch {
            (1..Int.MAX_VALUE).forEach {
                delay(1000)
                log("Job C2: repetition $it")
            }
        }

        jobC1.invokeOnCompletion {exception -> if (exception != null) log("Job C1 On Completion - Exception: [ $exception ]") }
        jobC2.invokeOnCompletion {exception -> if (exception != null) log("Job C2 On Completion - Exception: [ $exception ]") }

        try {
            log("Job C: Value = ${jobC1.await()}")
        } catch (e: Exception) {
            log("Job C Catch: [ ${e.message} ]")
        }

        jobC2.join()
        log("Job C end")
    }

    fun release() {
        this.job.cancel()
    }

}