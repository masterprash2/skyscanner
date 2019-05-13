package com.skyscanner.challenge.app.di

import android.content.Context
import com.skyscanner.challenge.app.SkyscannerApplication
import com.skyscanner.repository.ConfigGateway
import com.skyscanner.repository.FlightRepository
import com.skyscanner.repository.PlatformUtils
import com.skyscanner.repository.impl.AndroidUtils
import com.skyscanner.repository.impl.ConfigGatewayImpl
import com.skyscanner.repository.impl.FlightRespositoryImpl
import com.skyscanner.repository.impl.retrofit.FlightApi
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SkyscannerAppModule {


    @AppScope
    @Provides
    fun context(skyscannerApplication: SkyscannerApplication): Context {
        return skyscannerApplication
    }


//    @AppScope
//    @Provides
//    fun networkGateway(assetBackedGateway: NetworkGatewayUsingAssetsDevImpl): NetworkGateway {
//        return assetBackedGateway
//    }


    @BackgroundThreadScheduler
    @AppScope
    @Provides
    fun backgroundThreadScheduler(): Scheduler {
        return Schedulers.computation()
    }

    @AppScope
    @Provides
    fun configGateway(configGatewayImpl: ConfigGatewayImpl): ConfigGateway {
        return configGatewayImpl
    }


    @AppScope
    @Provides
    fun platformUtils(androidUtils: AndroidUtils): PlatformUtils {
        return androidUtils
    }

    @AppScope
    @Provides
    fun flightRespository(instance: FlightRespositoryImpl): FlightRepository {
        return instance
    }

    @MainThreadScheduler
    @Provides
    fun mainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread();
    }




}