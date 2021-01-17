package customConsole

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.turbo.TurboFilter
import ch.qos.logback.core.spi.FilterReply
import org.fusesource.jansi.Ansi
import org.slf4j.Marker

/**
 * @author zp4rker
 */
class LogInterceptor : TurboFilter() {

    override fun decide(marker: Marker?, logger: Logger?, level: Level?, format: String?, params: Array<out Any>?, t: Throwable?): FilterReply {
        logger ?: return FilterReply.DENY

        if (level == Level.TRACE || level == Level.DEBUG) return FilterReply.DENY

        val nameRaw = logger.name.run { split(".").getOrElse(2) { this } }.run {
            when {
                length > 6 -> "${this.substring(0..3)}.."
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

        val name = Ansi.ansi().reset().fgBrightBlack().a(nameRaw).reset()
        val lvlString = when (lvl) {
            Level.INFO -> Ansi.ansi().fgGreen().a(lvl.levelStr).reset()
            Level.DEBUG, Level.TRACE -> Ansi.ansi().fgBlack().a(lvl.levelStr).reset()
            Level.WARN -> Ansi.ansi().fgYellow().a(lvl.levelStr).reset()
            Level.ERROR -> Ansi.ansi().fgDefault().bgRed().a(lvl.levelStr).reset()
            else -> Ansi.ansi().fgDefault().a(lvl.levelStr).reset()
        }

        println("$name\t$lvlString\t$message")
        t?.let { println("$name\t${Ansi.ansi().fgDefault().bgRed().a("STACK").reset()}\t${it.stackTraceToString()}") }

        return FilterReply.DENY
    }

}