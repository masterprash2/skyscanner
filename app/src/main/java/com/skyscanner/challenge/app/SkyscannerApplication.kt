package com.skyscanner.challenge.app

import com.skyscanner.challenge.app.di.DaggerSkyscannerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

open class SkyscannerApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return androidInjector
    }

    lateinit var androidInjector: AndroidInjector<SkyscannerApplication>

    override fun onCreate() {
        androidInjector = DaggerSkyscannerAppComponent.factory().create(this)
        super.onCreate()
    }

}