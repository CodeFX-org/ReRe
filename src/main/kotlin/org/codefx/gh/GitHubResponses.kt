package org.codefx.gh

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import rx.Observable
import java.time.ZonedDateTime

/*
 * Parses GitHub API JSON responses.
 */

// CONTENT URLs

fun contentUrlsFromSearchResponse(searchResult: String): Observable<ContentUrls> {
    return Observable.from(parseContentUrlsFromSearchResponse(searchResult))
}

private fun parseContentUrlsFromSearchResponse(searchResult: String): List<ContentUrls> {
    return JsonParser()
            .parse(searchResult).asJsonObject
            .get("items").asJsonArray
            .map { it.asJsonObject }
            .map(::parseContentUrls)
}

private fun parseContentUrls(project: JsonObject): ContentUrls {
    return ContentUrls(
            fullReleasesUrl = project.get("releases_url").asString.removeSuffix("{/id}"),
            tagsUrl = project.get("tags_url").asString)
}

// FULL RELEASES

fun fullReleases(releases: String): Observable<FullRelease> {
    return Observable.from(parseFullReleases(releases))
}

private fun parseFullReleases(releases: String): List<FullRelease> {
    return JsonParser()
            .parse(releases).asJsonArray
            .map { it.asJsonObject }
            .map(::parseFullRelease)

}

private fun parseFullRelease(release: JsonObject): FullRelease {
    return FullRelease(
            name = release.get("name").asString,
            date = ZonedDateTime.parse(release.get("published_at").asString),
            notes = release.get("body").asString,
            url = release.get("html_url").asString)
}

// TAGS

fun tags(tags: String): Observable<String> {
    return Observable.from(parseTags(tags))
}

private fun parseTags(tags: String): List<String> {
    return JsonParser()
            .parse(tags).asJsonArray
            .map { it.toString() }
}

fun extractCommitApiUrl(tag: String): String {
    return JsonParser()
            .parse(tag).asJsonObject
            .get("commit").asJsonObject
            .get("url").asString
}

fun extractTagName(tag: String): String {
    return JsonParser()
            .parse(tag).asJsonObject
            .get("name").asString
}

fun tag(tagName: String, commit: String): Observable<Tag> {
    return Observable.just(parseTag(tagName, commit))
}

private fun parseTag(tagName: String, commit: String): Tag {
    val json = JsonParser()
            .parse(commit).asJsonObject
    return Tag(
            name = tagName,
            date = ZonedDateTime.parse(json
                    .get("commit").asJsonObject
                    .get("committer").asJsonObject
                    .get("date").asString),
            commitUrl = json.get("html_url").asString)
}
