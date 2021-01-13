package customConsole

import org.fusesource.jansi.AnsiConsole
import java.io.PrintStream
import java.lang.StringBuilder

/**
 * @author zp4rker
 */
fun main() {
    val old = System.out
    val log = StringBuilder()

    System.setOut(object : PrintStream(System.out) {
        override fun write(buf: ByteArray, off: Int, len: Int) {
            super.write(buf, off, len)
            val out = String(buf).substring(0, len)
            if (out != "\n") log.append("wrote $out\n")
        }
    })

    println("a really long string")
    println("test")
    println(1)
    println(false)

    System.setOut(old)

    println(log)

    /*AnsiConsole.systemInstall()
    ConsoleThread().start()*/

    /*print(Ansi.ansi().fgGreen().a("The letter $").reset())
    for (letter in 'a'..'z') {
        print(Ansi.ansi().cursorLeft(1).eraseLine(Ansi.Erase.FORWARD))
        print(Ansi.ansi().bold().fgBrightYellow().a(letter).reset())
        Thread.sleep(300)
    }
    println()

    print(Ansi.ansi().fgGreen().a("The number $").reset())
    for (number in '0'..'9') {
        print(Ansi.ansi().cursorLeft(1).eraseLine(Ansi.Erase.FORWARD))
        print(Ansi.ansi().bold().fgBrightYellow().a(number).reset())
        Thread.sleep(300)
    }*/
}