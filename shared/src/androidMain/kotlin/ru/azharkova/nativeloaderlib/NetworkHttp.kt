package ru.azharkova.nativeloaderlib

import kotlinx.serialization.KSerializer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.Exception

actual class NetworkHttp {
    val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    actual suspend inline fun <reified T> request(
        path: String,
        method: Method,
        body: Any?, headers: Map<Any?, *>
    ): Result<T> {
        val requestBuilder = Request.Builder()
            .url(path)
        for (header in headers) {
            requestBuilder.addHeader(
                header.key.toString(),
                header.value.toString()
            )
        }

        requestBuilder.method(method.name, null)
        val request = requestBuilder.build()
        try {
            val response = client.newCall(request).await()
            val content = JsonDecoder
                .instance
                .decode<T>(
                    response.body!!.string()
                )
            return Result.success(content)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    actual suspend fun <T:Any> requestString(
        path: String,
        method: Method,
        body: Any?, headers: Map<Any?, *>
    ): Result<String> {
        val requestBuilder = Request.Builder()
            .url(path)
        for (header in headers) {
            requestBuilder.addHeader(
                header.key.toString(),
                header.value.toString()
            )
        }

        requestBuilder.method(method.name, null)
        val request = requestBuilder.build()
        try {
            val response = client.newCall(request).await()
            val content = response.body!!.string()
            return Result.success(content!!)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}