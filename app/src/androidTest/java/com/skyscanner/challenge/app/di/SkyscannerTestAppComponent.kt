package com.skyscanner.challenge.app.di

import com.skyscanner.challenge.app.FlightApiTestHelper
import com.skyscanner.challenge.app.SkyscannerApplication
import com.skyscanner.repository.impl.retrofit.di.RetrofitScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@RetrofitScope
@AppScope
@dagger.Component(
    modules = [AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ActivityBuilderModule::class,
        SkyscannerTestAppModule::class,
        MockRetrofitModule::class
    ]
)
interface SkyscannerTestAppComponent : SkyscannerAppComponent {

    fun flightApiHelper() : FlightApiTestHelper

    @Component.Factory
    interface Builder : SkyscannerAppComponent.Builder {

        override fun create(@BindsInstance instance: SkyscannerApplication): SkyscannerTestAppComponent

    }
}
