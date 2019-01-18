package com.google.samples.apps.sunflower.di

import com.google.samples.apps.sunflower.GardenFragment
import com.google.samples.apps.sunflower.PlantDetailFragment
import com.google.samples.apps.sunflower.PlantListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by wangta on 19/12/2017.
 */
@Module
abstract class InjectorBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeGardenFragment(): GardenFragment

    @ContributesAndroidInjector
    abstract fun contributePlantDetailFragment(): PlantDetailFragment

    @ContributesAndroidInjector
    abstract fun PlantListFragment(): PlantListFragment
}