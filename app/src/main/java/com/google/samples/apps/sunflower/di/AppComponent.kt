package com.google.samples.apps.sunflower.di

import androidx.work.ListenableWorker
import com.google.samples.apps.sunflower.App
import com.google.samples.apps.sunflower.workers.ChildWorkerFactory
import com.google.samples.apps.sunflower.workers.SampleWorkerFactory
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Component
import dagger.MapKey
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * Created by Trevor Wang on 2/16/17.
 */
@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            InjectorBuilderModule::class,
            SampleAssistedInjectModule::class,
            WorkerBindingModule::class
        ]
)
interface AppComponent : AndroidInjector<App> {
    fun factory(): SampleWorkerFactory
}

@Module(includes = [AssistedInject_SampleAssistedInjectModule::class])
@AssistedModule
interface SampleAssistedInjectModule

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(SeedDatabaseWorker::class)
    fun bind(assistedFactory: SeedDatabaseWorker.Factory): ChildWorkerFactory

}
