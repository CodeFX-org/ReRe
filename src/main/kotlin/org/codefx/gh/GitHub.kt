package org.codefx.gh

import rx.Observable

val GITHUB_URL = "https://api.github.com"

class GitHub(val http : Http) : AutoCloseable {

    constructor() : this(Http(GITHUB_URL))

    fun zen() : Observable<String> {
        return http.getContent("/zen")
    }

    fun searchRepositoriesByLanguage(lang : String, sortBy : SortBy? = null, sortOrder : SortOrder? = null, perPage: Int = 100) : Observable<String> {
        var searchPath = "/search/repositories"
        searchPath += "?q=language:$lang"
        sortBy?.run { searchPath += "&sort=" + sortBy.inUrl }
        sortOrder?.run { searchPath += "&order=" + sortOrder.inUrl }
        perPage.run { searchPath += "&per_page=" + perPage}
        return http.getContent(searchPath)
    }

    override fun close() {
        http.close()
    }

}

enum class SortBy(val inUrl : String) {
    STARS("stars")
}

enum class SortOrder(val inUrl : String) {
    ASCENDING("asc"), DESCENDING("desc")
}