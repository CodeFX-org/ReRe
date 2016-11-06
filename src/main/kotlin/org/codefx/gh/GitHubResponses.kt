package org.codefx.gh

import com.google.gson.JsonParser
import rx.Observable

fun parseReleaseUrlsFromSearchResponse(searchResponse: String): List<String> {
    val parser = JsonParser()
    return parser
            .parse(searchResponse).asJsonObject
            .get("items").asJsonArray
            .map { it.asJsonObject }
            .map { it.get("releases_url").asString }
            .map { it.removeSuffix("{/id}")}
}

fun releaseUrlsFromSearchResponse(searchResponse: String): Observable<String> {
    return Observable.from(parseReleaseUrlsFromSearchResponse(searchResponse))
}
