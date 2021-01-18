package customConsole

import io.leego.banana.BananaUtils
import io.leego.banana.Font
import io.leego.banana.Layout
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.AnnotatedEventManager
import net.dv8tion.jda.api.hooks.SubscribeEvent
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author zp4rker
 */
fun main() {
    if (!AnsiConsole.isInstalled()) AnsiConsole.systemInstall()

    val name = linedText("CustomConsole")

    println("\n${BananaUtils.bananaify(name, Font.BIG_MONEY_NW, Layout.SMUSH_R, Layout.SMUSH_R).trimEnd()}")

    println(BananaUtils.bananaify("v${BananaUtils::class.java.`package`.implementationVersion}", Font.RECTANGLES).trimEnd() + " \tby zp4rker#3333\n")

    val readyListener = object {
        @SubscribeEvent
        fun onReady(e: ReadyEvent) {
            with(LoggerFactory.getLogger("test")) {
                info("This is an info log")
                debug("This is a debug log")
                warn("This is a warn log")
                error("This is an error log")
            }
            e.jda.shutdownNow()
        }
    }

    with(JDABuilder.createLight(System.getenv("TOKEN"))) {
        setEventManager(AnnotatedEventManager())
        addEventListeners(readyListener)
    }.build()
}

private fun linedText(original: String): String {
    fun components(original: String): List<String> {
        val list = mutableListOf<String>()
        for ((i, c) in original.toCharArray().withIndex()) {
            if ((c.isUpperCase() || c.isWhitespace())) {
                if (i == 0 || (original.elementAt(i - 1).isUpperCase() && original.elementAtOrElse(i + 1) { 'A' }.isUpperCase())) continue
                list.add(original.substring(0, i))
                list.addAll(components(original.substring(if (c.isWhitespace()) i + 1 else i, original.length)))
                break
            }
        }
        if (list.isEmpty()) list.add(original)
        return list
    }

    return components(original).joinToString("\n")
}