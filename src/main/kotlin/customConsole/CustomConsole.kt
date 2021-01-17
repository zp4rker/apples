package customConsole

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.hooks.SubscribeEvent
import org.slf4j.LoggerFactory
import java.io.PrintStream
import java.lang.IllegalStateException

/**
 * @author zp4rker
 */
fun main() {
    val readyListener = object {
        @SubscribeEvent
        fun onReady(e: ReadyEvent) {
            with(LoggerFactory.getLogger("test")) {
                info("hi")
                warn("this is a warning!")
                error("oh no!!!", IllegalStateException("THIS IS SO WRONG"))
            }
            e.jda.shutdownNow()
        }
    }

    with(JDABuilder.createLight(System.getenv("TOKEN"))) {
        setEventManager(AnnotatedEventManager())
        addEventListeners(readyListener)
    }.build()

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