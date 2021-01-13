package customConsole

import org.fusesource.jansi.Ansi

/**
 * @author zp4rker
 */
class CustomLogger {

    private val prompt: String = Ansi.ansi().bold().fgGreen().a("cmd>").reset().toString()

    init {
        printPrompt()
    }

    fun log(x: Any?) {
        print(Ansi.ansi().restoreCursorPosition().eraseLine(Ansi.Erase.FORWARD))
        println(x.toString())
        printPrompt()
    }

    private fun printPrompt() {
        print(Ansi.ansi().saveCursorPosition())
        print("$prompt ")
    }

}