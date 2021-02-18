package salah

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import kotlin.math.*

/**
 * @author zp4rker
 */
val jDay = getJulianDay()

private val method = CalculationMethod(18.0, 18.0)
private val location = Location.getLocation()

private val timezone = TimeUnit.SECONDS.toHours(ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds.toLong())
private val longitudeDiff = ((timezone * 15) - location.longitude) / 15

private val timeEq = eqOfTime()

fun main() {
    val dhuhrTime = 12 + longitudeDiff + timeEq / 60
}

private fun getJulianDay(date: OffsetDateTime = OffsetDateTime.now()): Float {
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

private fun eqOfTime(jd: Float = jDay): Double {
    val n = jd - 2451544.5
    val g = 357.528 + 0.9856003 * n
    val c = 1.9148 * dsin(g) + 0.02 * dsin(2 * g) + 0.0003 * dsin(3 * g)
    val lamda = 280.47 + 0.9856003 * n + c
    val r = -2.468 * dsin(2 * lamda) + 0.053 * dsin(4 * lamda) + 0.0014 * dsin(6 * lamda)
    return (c + r) * 4
}

private fun dsin(deg: Double): Double = sin((deg * Math.PI) / 180)
private fun dcos(deg: Double): Double = cos((deg * Math.PI) / 180)

private fun angleToTime(angle: Double) {
    val delta = sunDeclination()
    // TODO: Continue here
}

private fun sunDeclination(jd: Float = jDay): Double {
    val n = jd - 2451544.5
    val epilson = 23.44 - 0.0000004 * n
    val l = 280.466 + 0.9856474 * n
    val g = 357.528 + 0.9856003 * n
    val lamda = l + 1.915 * dsin(g) + 0.02 * dsin(2 * g)
    val x = dsin(epilson) * dsin(lamda)
    return (180 / (4 * atan(1.0))) * atan(x / sqrt(-x * x + 1))
}