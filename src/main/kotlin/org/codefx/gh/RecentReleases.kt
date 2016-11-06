package org.codefx.gh

fun main(args: Array<String>) {
    val gh = GitHub()

    gh.zen()
            .toBlocking().subscribe(::println)
    gh.searchRepositoriesByLanguage("java", SortBy.STARS, SortOrder.DESCENDING, 100)
            .flatMap(::releaseUrlsFromSearchResponse)
            .toBlocking()
            .subscribe(::println)

    gh.close()
}
