package ru.azharkova.nativeloaderlib;

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer

public enum class Method {
    GET,
    POST,
    PUT,
    DELETE
}

public expect class NetworkHttp constructor() {
   public suspend inline fun <reified T> request(
        path: String,
        method: Method,
        body: Any? = null, headers: Map<Any?, *>
    ): Result<T>

   public suspend fun <T:Any> requestString(
        path: String,
        method: Method,
        body: Any? = null, headers: Map<Any?, *>
    ): Result<String>
}