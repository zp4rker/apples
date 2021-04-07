package mainClassFinder

import java.util.*
import kotlin.concurrent.thread

/**
 * @author zp4rker
 */
fun main() {
    thread(isDaemon = true) {
        Scanner(System.`in`).nextLine()
    }

    thread(isDaemon = true) {
        while (true) {}
    }

    println("Main class is ${Thread.getAllStackTraces().entries.first { it.key.name == "main" }.value.last().className}")
}