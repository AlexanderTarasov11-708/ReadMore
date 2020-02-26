package com.itis.readmore.di

import android.app.Application
import com.intmainreturn00.grapi.grapi
import com.itis.readmore.BuildConfig

class AppController : Application() {
    companion object {
        lateinit var instance: AppController
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        grapi.init(this, BuildConfig.goodreadsKey, BuildConfig.goodreadsSecret, BuildConfig.goodreadsCallback)
    }

}