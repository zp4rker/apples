package customConsole

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

/**
 * @author zp4rker
 */
class LogBlocker : Filter<ILoggingEvent>() {

    override fun decide(event: ILoggingEvent): FilterReply {
        return if (event.loggerName.contains(".")) FilterReply.DENY else FilterReply.ACCEPT
    }
}