package hijri

import java.time.OffsetDateTime
import kotlin.math.floor

/**
 * @author zp4rker
 */

fun main() {
    val date = getHijriDate(OffsetDateTime.now().withDayOfMonth(17).withMonth(2).withYear(2004))

    println(date.run { "${day.toString().padStart(2, '0')}-${month.toString().padStart(2, '0')}-$year" })
}

private fun gToJ(date: OffsetDateTime): Float {
    val d = date.dayOfMonth
    var m = date.monthValue
    var y = date.year

    if (m <= 2) {
        m += 12
        y -= 1
    }

    val a = floor(y / 100F)

    val b = when {
        y > 1582 || (y == 1582 && m > 10) || (y == 1582 && m == 10 && d > 14) -> 2 - a + floor(a / 4F)
        else -> 0F
    }

    return (floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + d + b - 1524.5).toFloat()
}

private fun getHijriDate(date: OffsetDateTime = OffsetDateTime.now(), adjustment: Int = 0): HijriDate {
    val jd = gToJ(date)

    var a = floor(jd + (3 - floor(date.year / 1000F)) + adjustment) - 1948440 + 10632
    val b = floor((a - 1) / 10631F)
    a = a - 10631 * b + 354
    val c = floor((10985 - a) / 5316F) * floor((50 * a) / 17719F) + floor(a / 5670F) * floor((43 * a) / 15238F)
    a = a - floor((30 - c) / 15F) * floor((17719 * c) / 50F) - floor(c / 16F) * floor((15238 * c) / 43F) + 29

    val m = floor((24 * a) / 709F)
    val d = a - floor((709 * m) / 24F)
    val y = 30 * b + c - 30

    return HijriDate(d.toInt(), m.toInt(), y.toInt())
}