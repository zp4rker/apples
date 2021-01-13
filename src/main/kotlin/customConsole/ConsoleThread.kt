package customConsole

import org.fusesource.jansi.Ansi
import java.util.*

/**
 * @author zp4rker
 */
class ConsoleThread : Thread() {

    private val logger = CustomLogger()

    private var running = true

    override fun run() {
        val scanner = Scanner(System.`in`)

        while (running) {
            handleInput(scanner.next())
        }
    }

    private fun handleInput(input: String) {
        when (input.toLowerCase()) {
            "stop", "end", "quit", "exit" -> running = false
            else -> logger.log(Ansi.ansi().fgBrightBlack().a("Ran command: $input").reset())
        }
    }

}