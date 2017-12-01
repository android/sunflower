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

package com.google.samples.apps.sunflower;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;

import com.google.samples.apps.sunflower.data.Plant;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

class TestUtils {

    /**
     * {@link Plant} object used for tests.
     */
    static Plant testPlant = new Plant("1", "Apple", "A red fruit");

    /**
     * Matches the toolbar title with a specific string.
     *
     * @param string the string to match
     */
    static Matcher<View> withCollapsingToolbarTitle(final String string) {
        return new BoundedMatcher<View, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                description.appendText(string);
            }

            @Override
            protected boolean matchesSafely(CollapsingToolbarLayout toolbar) {
                return string.equals(toolbar.getTitle());
            }
        };
    }
}
