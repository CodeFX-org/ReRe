package org.codefx.gh

import rx.Observable

fun main(args: Array<String>) {
    val gh = GitHub()

    gh.zen()
            .subscribe(::println)
    gh.searchRepositoriesByLanguage("java", SortBy.STARS, SortOrder.DESCENDING, 2)
//            .doOnNext(::println)
            .flatMap(::contentUrlsFromSearchResponse)
            .flatMap { contentUrl -> listReleases(gh, contentUrl) }
            .map { it.asString() }
            .toBlocking()
            .subscribe(::println)

    gh.close()
}

private fun listReleases(gh: GitHub, urls: ContentUrls): Observable<Release> {
    val release: Observable<Release> = gh.listFullReleases(urls.fullReleasesUrl)
//            .doOnNext(::println)
            .filter { it != "[]" }
            .flatMap(::fullReleases)
    return release.switchIfEmpty(listDetailedTags(gh, urls.tagsUrl))
}

private fun listDetailedTags(gh: GitHub, tagsUrl: String): Observable<Tag> {
    return gh.listTags(tagsUrl)
            .flatMap(::tags)
            .flatMap { tag ->
                val tagName = extractTagName(tag)
                val commitUrl = extractCommitApiUrl(tag)
                gh.showCommit(commitUrl)
                        .flatMap { tag(tagName, it) }
            }
}
