package org.codefx.gh

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val RELEASE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM uuuu")

interface Release {

    val name: String
    val date: ZonedDateTime
    val url: String

    fun asString(): String {
        return "$name (${date.format(RELEASE_DATE_FORMATTER)}, $url)"
    }

}

data class Tag(
        override val name: String,
        override val date: ZonedDateTime,
        // tags have no separate URL so use the commit URL instead
        val commitUrl: String)
    : Release {
    override val url = commitUrl
}

data class FullRelease(
        override val name: String,
        override val date: ZonedDateTime,
        val notes: String,
        override val url: String)
    : Release {

    override fun toString(): String {
        return "FullRelease(name='$name', date=$date, url='$url')"
    }
}
