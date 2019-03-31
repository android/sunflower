package com.google.samples.apps.sunflower

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.InstrumentationRegistry
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.test.AddFlowTest
import com.google.samples.apps.sunflower.test.CheckGardenTest
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        AddFlowTest::class,
        CheckGardenTest::class
)
object IntegrationSuite {

    @BeforeClass
    @JvmStatic
    fun setUp() {
        // Prepare some utils, inject test DI components
        val context = InstrumentationRegistry.getTargetContext()
        AppDatabase.setInstance(Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance().enqueue(request)
                    }
                })
                .build())
    }
}