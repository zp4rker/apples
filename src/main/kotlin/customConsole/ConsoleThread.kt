package customConsole

import customConsole.LogInterceptor.Companion.log
import org.fusesource.jansi.Ansi

/**
 * @author zp4rker
 */
class ConsoleThread : Thread() {

    private var running = true

    override fun run() {
        while (running) {
            handleInput(System.console().readLine())
        }
    }

    private fun handleInput(input: String) {
        when (input.toLowerCase()) {
            "stop", "end", "quit", "exit" -> running = false
            else -> log(Ansi.ansi().cursorUpLine().eraseLine(Ansi.Erase.FORWARD).a("Unknown command \"$input\"").reset())
        }
    }

}