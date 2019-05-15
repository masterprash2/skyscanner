package com.skyscanner.repository.impl.retrofit.di

import com.skyscanner.repository.ConfigGateway
import com.skyscanner.repository.impl.retrofit.FlightApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
class RetrofitModule {

    @Provides
    @RetrofitScope
    fun okhttp(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @RetrofitScope
    fun retrofit(configGateway: ConfigGateway, okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder().build()
        return Retrofit.Builder()
            .baseUrl(configGateway.getCreateSessionUrl())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @RetrofitScope
    fun flightApi(retrofit: Retrofit): FlightApi {
        return retrofit.create(FlightApi::class.java)
    }


}