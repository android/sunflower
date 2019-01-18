package com.google.samples.apps.sunflower

import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.samples.apps.sunflower.di.AppModule
import com.google.samples.apps.sunflower.di.DaggerAppComponent
import com.google.samples.apps.sunflower.workers.SampleWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class App : DaggerApplication() {
    @Inject
    lateinit var factory: SampleWorkerFactory

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this, Configuration.Builder().setWorkerFactory(factory).build())
    }
}