package com.skyscanner.challenge.test

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.skyscanner.challenge.app.SkyscannerTestApplication

class TestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, SkyscannerTestApplication::class.java.name, context)
    }


}