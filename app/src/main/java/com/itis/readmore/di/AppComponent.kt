package com.itis.readmore.di

import com.itis.readmore.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [NetModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(appController: AppController)

    fun inject(activity: MainActivity)
}