package com.skyscanner.challenge.app.di

import android.content.Context
import com.skyscanner.challenge.app.FlightApiTestHelper
import com.skyscanner.repository.impl.retrofit.FlightApi
import dagger.Module
import dagger.Provides

@Module
class MockRetrofitModule {

//    @Provides
//    @RetrofitScope
//    fun okhttp(): OkHttpClient {
//        return OkHttpClient.Builder().build()
//    }
//
//    @Provides
//    @RetrofitScope
//    fun retrofit(configGateway: ConfigGateway, okHttpClient: OkHttpClient): Retrofit {
//        val moshi = Moshi.Builder().build()
//        return Retrofit.Builder()
//            .baseUrl(configGateway.getCreateSessionUrl())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .client(okHttpClient)
//            .build()
//    }
//
//    @AppScope
//    @Provides
//    fun flightApi(retrofit: Retrofit): FlightApi {
//        return FlightApiTestHelper(retrofit)
//    }

    @AppScope
    @Provides
    fun flightApi(flightApiTestHelper: FlightApiTestHelper): FlightApi {
        return flightApiTestHelper
    }

    @AppScope
    @Provides
    fun flightApiHelper(context: Context): FlightApiTestHelper {
        return FlightApiTestHelper(context)
    }

}