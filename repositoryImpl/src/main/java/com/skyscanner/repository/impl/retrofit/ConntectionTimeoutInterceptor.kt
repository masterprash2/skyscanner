package com.skyscanner.repository.impl.retrofit

import io.reactivex.subjects.PublishSubject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.net.SocketTimeoutException

class ConntectionTimeoutInterceptor : Interceptor {

    val resetSingnal = PublishSubject.create<Boolean>()

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            return chain.proceed(chain.request())
        } catch (e: Exception) {
            if (e is SocketTimeoutException && e.message!!.contains("timeout")) {
                chain.call().cancel()
                resetOkhttpClient()
            }
            throw e
        }
    }

    private fun resetOkhttpClient() {
        resetSingnal.onNext(true)
    }

    fun attach(client: OkHttpClient) {
        val subscribe = resetSingnal.subscribe {
            try {
                client.dispatcher().cancelAll()
                client.connectionPool().evictAll()
            } catch (e: Exception) {
            }
        }
    }

}