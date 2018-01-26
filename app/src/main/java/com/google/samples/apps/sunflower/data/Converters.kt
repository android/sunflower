/*
 * Copyright 2018 Google LLC
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

import android.arch.persistence.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    companion object {
        private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)

        @TypeConverter fun calendarToDatestamp(calendar: Calendar): String =
                dateFormat.format(calendar.time)

        @TypeConverter fun datestampToCalendar(value: String): Calendar =
                Calendar.getInstance().apply { time = dateFormat.parse(value) }
    }
}