package ru.azharkova.nativeloaderlib

import kotlinx.cinterop.BetaInteropApi
import kotlinx.serialization.KSerializer
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataTaskWithRequest
import platform.Foundation.setAllHTTPHeaderFields
import platform.Foundation.setHTTPBody
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public actual class NetworkHttp actual constructor(){

val urlSession: NSURLSession by lazy {
    NSURLSession.sharedSession
}

    @OptIn(BetaInteropApi::class)
  public  actual suspend inline fun<reified T> request(path: String,
                                                 method: Method,
                                                 body: Any?, headers: Map<Any?, *>): Result<T> {
        val url = NSURL.URLWithString(path)
        val urlRequest = NSMutableURLRequest(url!!)
        urlRequest.setAllHTTPHeaderFields(headers)
        if (body != null) {
        }
        print(url)

        return suspendCoroutine<Result<T>> { coroutine ->
            urlSession.dataTaskWithRequest(urlRequest) { data, response, error ->

                try {
                    val content = NSString.create(data!!, encoding =  NSUTF8StringEncoding).toString()
                    print(content)
                    coroutine.resume(Result.success(JsonDecoder.instance.decode<T>(content)))
                } catch (e: Exception) {
                    print(e.message)
                    coroutine.resume(Result.failure(e))
                }
            }.resume()
        }
    }

    public actual suspend fun <T:Any> requestString(
        path: String,
        method: Method,
        body: Any?, headers: Map<Any?, *>,
    ): Result<String> {
        val url = NSURL.URLWithString(path)
        val urlRequest = NSMutableURLRequest(url!!)
        urlRequest.setAllHTTPHeaderFields(headers)
        if (body != null) {
        }
        print(url)

        return suspendCoroutine { coroutine ->
            urlSession.dataTaskWithRequest(urlRequest) { data, response, error ->

                try {
                    val content = NSString.create(data!!, encoding = NSUTF8StringEncoding).toString()
                    print(content)
                    coroutine.resume(Result.success(content))
                } catch (e: Exception) {
                    print(e.message)
                    coroutine.resume(Result.failure(e))
                }
            }.resume()
        }
    }

}