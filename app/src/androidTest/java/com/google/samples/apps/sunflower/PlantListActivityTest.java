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

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.samples.apps.sunflower.TestUtils.testPlant;
import static com.google.samples.apps.sunflower.TestUtils.withCollapsingToolbarTitle;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PlantListActivityTest {

    @Rule
    public ActivityTestRule<PlantListActivity> activityTestRule =
            new ActivityTestRule<>(PlantListActivity.class);

    @Test
    public void plantListFrameIsDisplayed() {
        onView(withId(R.id.plant_list_frame)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnFirstListElement_NavigatesToPlantDetailsActivityWithPlantData() {
        onView(ViewMatchers.withId(R.id.plant_list_frame))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        String title = testPlant.getName();
        String description = testPlant.getDescription();
        onView(withId(R.id.toolbar_layout)).check(matches(withCollapsingToolbarTitle(title)));
        onView(withId(R.id.plant_detail)).check(matches(withText(description)));
    }

}
