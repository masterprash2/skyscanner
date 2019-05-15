package com.skyscanner.challenge.app

import com.skyscanner.challenge.app.di.DaggerSkyscannerTestAppComponent
import dagger.android.AndroidInjector


open class SkyscannerTestApplication : SkyscannerApplication() {

    override fun createComponent(): AndroidInjector<SkyscannerApplication> {
        return DaggerSkyscannerTestAppComponent.factory().create(this)
    }

}