/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.di

import android.content.Context
import com.google.samples.apps.sunflower.data.db.AppDatabase
import com.google.samples.apps.sunflower.data.dao.GardenPlantingDao
import com.google.samples.apps.sunflower.data.dao.PlantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 表示提供依赖注入实例的模块
@Module
// 把模块安装到的组件作用域：SingletonComponent全局可用、
// ActivityComponent在Activity、Fragment、View可用、ViewComponent在View可用等
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    // 实现方法提供实例的注解@Provides；抽象方法自动提供实例的注解@Binds
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): PlantDao {
        return appDatabase.getPlantDao()
    }

    @Provides
    fun provideGardenPlantingDao(appDatabase: AppDatabase): GardenPlantingDao {
        return appDatabase.getGardenPlantingDao()
    }
}
