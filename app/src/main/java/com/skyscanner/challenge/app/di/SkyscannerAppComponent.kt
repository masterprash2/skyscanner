package com.skyscanner.challenge.app.di

import com.skyscanner.challenge.app.SkyscannerApplication
import com.skyscanner.repository.impl.retrofit.di.RetrofitModule
import com.skyscanner.repository.impl.retrofit.di.RetrofitScope
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
        SkyscannerAppModule::class,
        RetrofitModule::class]
)
interface SkyscannerAppComponent : AndroidInjector<SkyscannerApplication> {

    @Component.Factory
    interface Builder : AndroidInjector.Factory<SkyscannerApplication> {
    }
}
