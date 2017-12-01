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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.samples.apps.sunflower.utilities.Constants.DATABASE_NAME;

/**
 * This class creates and prepopulates the database used for the app.
 */
public class DatabaseCreator {

    private static AppDatabase appDatabase;
    private final AtomicBoolean dbInitializing = new AtomicBoolean(true);
    private static final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile DatabaseCreator instance;

    public synchronized static DatabaseCreator getInstance() {
        DatabaseCreator result = instance;
        if (result == null) {
            synchronized (LOCK) {
                result = instance;
                if (result == null) {
                    instance = result = new DatabaseCreator();
                }
            }
        }
        return result;
    }

    // Used to observe when the database initialization is done
    LiveData<Boolean> isDatabaseCreated() {
        return isDatabaseCreated;
    }

    @Nullable
    AppDatabase getDatabase() {
        return appDatabase;
    }

    /**
     * Creates database.
     *
     * Although this uses an AsyncTask which currently uses a serial executor, it's thread-safe.
     */
    public void createDb(Context context) {

        // Ensure that this method is only run once.
        if (!dbInitializing.compareAndSet(true, false)) {
            return;
        }

        isDatabaseCreated.setValue(false);
        new CreateDbTask().execute(context.getApplicationContext());
    }

    private static class CreateDbTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... contexts) {
            Context context = contexts[0].getApplicationContext();

            // Reset the database to have new data on every app launch
            context.deleteDatabase(DATABASE_NAME);

            // Build database
            AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME).build();

            seedDatabase(db);
            appDatabase = db;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Now on the main thread, notify observers that the db is created and ready.
            isDatabaseCreated.setValue(true);
        }
    }

    private static void seedDatabase(AppDatabase database) {
        ArrayList<Plant> plants = new ArrayList<>(4);
        plants.add(new Plant("1", "Apple", "A red fruit"));
        plants.add(new Plant("2", "Beet", "A red root vegetable"));
        plants.add(new Plant("3", "Cucumber", "A green vegetable"));
        plants.add(new Plant("4", "Tomato", "A red vegetable"));

        database.beginTransaction();
        try {
            database.plantDao().insertAll(plants);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

}
