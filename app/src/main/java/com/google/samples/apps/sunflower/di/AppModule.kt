package com.google.samples.apps.sunflower.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.samples.apps.sunflower.App
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.GardenPlantingDao
import com.google.samples.apps.sunflower.data.PlantDao
import com.google.samples.apps.sunflower.utilities.DATABASE_NAME
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Trevor Wang on 2/16/17.
 */
@Module
class AppModule(val app: App) {

    @Provides
    @Singleton
    fun app(): App {
        return app
    }

    @Provides
    @Singleton
    fun context(): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun providePlantDao(db: AppDatabase): PlantDao {
        return db.plantDao()
    }

    @Provides
    @Singleton
    fun providePlantingDao(db: AppDatabase): GardenPlantingDao {
        return db.gardenPlantingDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase {
        return Room
                .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance().enqueue(request)
                    }
                })
                .build()
    }
}
