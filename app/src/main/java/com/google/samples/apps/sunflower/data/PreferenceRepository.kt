/*
 * Copyright 2022 Google LLC
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

package com.google.samples.apps.sunflower.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.samples.apps.sunflower.utilities.PreferenceKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor (private val dataStore: DataStore<Preferences>) {

    val nightMode: Flow<Int?> = dataStore.data.map { prefs ->
        prefs[PreferenceKeys.KEY_NIGHT_MODE_PREF]
    }

    suspend fun saveNightMode(mode: Int) {
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.KEY_NIGHT_MODE_PREF] = mode
        }
    }

}