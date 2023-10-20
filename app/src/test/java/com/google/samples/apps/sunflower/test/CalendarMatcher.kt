/*
 * Copyright 2023 Google LLC
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

package com.google.samples.apps.sunflower.test

import org.hamcrest.Description
import org.hamcrest.Factory
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

/**
 * Calendar matcher.
 * Only Year/Month/Day precision is needed for comparing GardenPlanting Calendar entries
 */
internal class CalendarMatcher(
    private val expected: Calendar
) : TypeSafeDiagnosingMatcher<Calendar>() {
    private val formatter = SimpleDateFormat("dd.MM.yyyy")

    override fun describeTo(description: Description?) {
        description?.appendText(formatter.format(expected.time))
    }

    override fun matchesSafely(actual: Calendar?, mismatchDescription: Description?): Boolean {
        if (actual == null) {
            mismatchDescription?.appendText("was null")
            return false
        }
        if (actual.get(YEAR) == expected.get(YEAR) &&
            actual.get(MONTH) == expected.get(MONTH) &&
            actual.get(DAY_OF_MONTH) == expected.get(DAY_OF_MONTH)
        )
            return true
        mismatchDescription?.appendText("was ")?.appendText(formatter.format(actual.time))
        return false
    }

    companion object {
        /**
         * Creates a matcher for [Calendar]s that only matches when year, month and day of
         * actual calendar are equal to year, month and day of expected calendar.
         *
         * For example:
         * <code>assertThat(someDate, hasSameDateWith(Calendar.getInstance()))</code>
         *
         * @param expected calendar that has expected year, month and day [Calendar]
         */
        @Factory
        fun equalTo(expected: Calendar): Matcher<Calendar> = CalendarMatcher(expected)
    }
}
