package sequential

fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}

fun log(character: Char) {
    print("$character")
}

fun main() {
    log("Start")

    Thread.sleep(1000)
    log("After Thread.sleep.")

    log("End")
}