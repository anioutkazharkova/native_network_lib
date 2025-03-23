package ru.azharkova.nativeloaderlib

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

class JsonDecoder{
    companion object {
        val instance = JsonDecoder()
        val jsonDecoder = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
    }

    inline fun<reified T> decode(json:String): T {
        return jsonDecoder.decodeFromString(json)
    }

    inline fun<reified T> encode(data:T): String {
        return jsonDecoder.encodeToString(data)
    }

    fun<T:Any> decodeString(deserializationStrategy: DeserializationStrategy<T>, json:String): T? {
        return jsonDecoder.decodeFromString(deserializationStrategy, json)
    }

    fun<T:Any> encodeString(serializationStrategy: SerializationStrategy<T>, data: T): String? {
        return jsonDecoder.encodeToString(serializationStrategy, data)
    }
}