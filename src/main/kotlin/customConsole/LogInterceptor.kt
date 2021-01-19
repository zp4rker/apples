package customConsole

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.turbo.TurboFilter
import ch.qos.logback.core.spi.FilterReply
import org.fusesource.jansi.Ansi
import org.slf4j.Marker
import java.io.File
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * @author zp4rker
 */
class LogInterceptor : TurboFilter() {

    override fun decide(marker: Marker?, logger: Logger?, level: Level?, format: String?, params: Array<out Any>?, t: Throwable?): FilterReply {
        logger ?: return FilterReply.DENY

        if (level == Level.TRACE || level == Level.DEBUG) return FilterReply.DENY

        val nameRaw = logger.name.run { split(".").getOrElse(2) { this } }.run {
            when {
                length > 7 -> "${this.substring(0..4)}.."
                length < 4 -> "$this\t"
                else -> this
            }
        }
        val lvl = level ?: Level.INFO
        val message = format?.run {
            if (params == null) {
                this
            } else {
                this.let {
                    var s = it
                    for (param in params) {
                        s = s.replaceFirst("{}", param.toString())
                    }
                    s
                }
            }
        } ?: "No message"

        with(Ansi.ansi()) {
            reset()

            fgBrightBlack()
            a("$nameRaw\t")

            when (lvl) {
                Level.INFO -> fgBrightGreen()
                Level.WARN -> fgBrightYellow()
                Level.ERROR -> fgBrightRed()
                else -> fgBlack()
            }
            a(lvl.levelStr)

            reset()
            a("\t$message")

            if (t != null) {
                a("\n${t.stackTraceToString()}")
            }

            log(this)

            if (message == "Finished Loading!") log()
        }

        return FilterReply.DENY
    }

    companion object {
        fun log(output: Any = "") {
            if (output.toString().contains("\n")) {
                output.toString().split("\n").forEach(::log)
                return
            }

            println(output)

            val logOut = output.toString().replace(Regex("\u001B\\[[;\\d]*m"), "")

            val logFile = File("logs/log.txt").also { if (!it.exists()) it.createNewFile() }
            val timestamp = if (logOut != "") {
                "[${OffsetDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))}] \t"
            } else {
                ""
            }
            logFile.appendText("$timestamp$logOut\n")
        }
    }

}