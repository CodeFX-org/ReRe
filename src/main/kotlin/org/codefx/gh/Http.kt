package org.codefx.gh

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.nio.client.HttpAsyncClient
import rx.Observable
import rx.apache.http.ObservableHttp
import rx.apache.http.ObservableHttpResponse
import java.nio.charset.Charset

class Http(val root: String, val httpClient: CloseableHttpAsyncClient) {

    constructor(root: String) : this(root, defaultHttpClient())

    fun get(path: String): Observable<ObservableHttpResponse> {
        return ObservableHttp
                .createGet(root + path, httpClient)
                .toObservable()
    }

    fun getContent(path: String): Observable<String> {
        return get(path)
                .flatMap { it.content }
                .map { it.toString(Charset.defaultCharset()) }
    }

    fun close() {
        httpClient.close()
    }

}

private fun defaultHttpClient(): CloseableHttpAsyncClient {
    val httpClient = HttpAsyncClients.custom()
            .setUserAgent("User-Agent: nicolaiparlog")
            .build()
    httpClient.start();
    return httpClient
}

