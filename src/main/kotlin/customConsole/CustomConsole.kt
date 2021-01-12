package customConsole

import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole

/**
 * @author zp4rker
 */
fun main() {
    AnsiConsole.systemInstall()
    print("The letter $")
    for (letter in 'a'..'z') {
        print(Ansi.ansi().cursorLeft(1).eraseLine(Ansi.Erase.FORWARD).a(letter))
    }
}