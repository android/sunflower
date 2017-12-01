/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import static com.google.samples.apps.sunflower.utilities.AppExecutors.runOnIoThread;
import static com.google.samples.apps.sunflower.utilities.Constants.DATABASE_NAME;

/**
 * The Room database for this app
 */
@Database(entities = {Plant.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlantDao plantDao();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {

                    // Reset the database to have new data on every app launch
                    // TODO / STOPSHIP: This is only used for development; remove prior to shipping
                    context.deleteDatabase(DATABASE_NAME);

                    // Create and pre-populate the database. See this article for more details:
                    // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
                    instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    runOnIoThread(() -> seedDatabase(getInstance(context)));
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }

    private static void seedDatabase(AppDatabase database) {
        ArrayList<Plant> plants = new ArrayList<>(4);
        plants.add(new Plant("1", "Apple", "A red fruit"));
        plants.add(new Plant("2", "Beet", "A red root vegetable"));
        plants.add(new Plant("3", "Celery", "A green vegetable"));
        plants.add(new Plant("4", "Tomato", "A red vegetable"));

        database.plantDao().insertAll(plants);
    }

}
