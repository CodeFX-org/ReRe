package org.codefx.gh.rere

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClients
import rx.Observable
import rx.apache.http.ObservableHttp
import rx.apache.http.ObservableHttpResponse
import java.nio.charset.Charset

class Http(val httpClient: CloseableHttpAsyncClient) : AutoCloseable {

    init {
        httpClient.start()
    }

    constructor() : this(defaultHttpClient())

    fun get(path: String): Observable<ObservableHttpResponse> {
        return ObservableHttp
                .createGet(path, httpClient)
                .toObservable()
    }

    fun getContent(path: String): Observable<String> {
        return get(path)
                .flatMap { it.content }
                .map { it.toString(Charset.defaultCharset()) }
    }

    override fun close() {
        httpClient.close()
    }

}

private fun defaultHttpClient(): CloseableHttpAsyncClient {
    return HttpAsyncClients.custom().build()
}
