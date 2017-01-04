package org.codefx.gh

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

data class Arguments(val token: String, val since: ZonedDateTime)

fun parse(args: Array<String>): Arguments {
    return Arguments(
            extractToken(args) ?: throw IllegalArgumentException("Specify a token with -t or --token"),
            extractSince(args))
}

private fun extractToken(args: Array<String>): String? {
    return valueFor(args, "-t", "--token")
}

private fun extractSince(args: Array<String>): ZonedDateTime {
    val since = valueFor(args, "-s", "--since")
    if (since != null)
        return ZonedDateTime.parse(since, DateTimeFormatter.ISO_DATE)

    val during = parseDuring(valueFor(args, "-d", "--during") ?: "14d")
    return ZonedDateTime.now().minus(during.first, during.second)
}

private fun parseDuring(during: String): Pair<Long, ChronoUnit> {
    try {
        val amount = during.dropLast(1).toLong()
        val unit = when (during.takeLast(1)) {
            "h" -> ChronoUnit.HOURS
            "d" -> ChronoUnit.DAYS
            "w" -> ChronoUnit.WEEKS
            "m" -> ChronoUnit.MONTHS
            else -> throw DateTimeParseException(
                    "Time span must end with h (hours), d (days), or w (weeks).", during, during.length - 1)
        }
        return Pair(amount, unit)
    } catch (ex: NumberFormatException) {
        throw DateTimeParseException(
                "Time span must begin with an integer and end with h (hours), d (days), or w (weeks).", during, 0, ex)
    }
}

private fun valueFor(args: Array<String>, vararg flags: String): String? {
    val index = flags.map { args.indexOf(it) }.max()?.inc()
    return if (index == null || index == 0 || index == args.size) null else args[index]
}

