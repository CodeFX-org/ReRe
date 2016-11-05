package org.codefx.gh

fun main(args: Array<String>) {
    val http = Http("https://api.github.com")

    http.getContent("/zen")
            .toBlocking()
            .subscribe(::println)

    http.close()
}
