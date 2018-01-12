/*
 * Copyright (C) 2018 The Android Open Source Project
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

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.samples.apps.sunflower.utilities.TestUtils.testPlant;
import static com.google.samples.apps.sunflower.utilities.TestUtils.withCollapsingToolbarTitle;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PlantDetailActivityTest {

    @Rule
    public ActivityTestRule<PlantDetailActivity> activityTestRule =
            new ActivityTestRule<PlantDetailActivity>(PlantDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, PlantDetailActivity.class);
                    result.putExtra(PlantDetailFragment.ARG_ITEM_ID, "1");
                    return result;
                }
            };

    @Test
    public void viewTextPersistAfterOrientationChange() {
        onView(withId(R.id.toolbar_layout))
                .check(matches(withCollapsingToolbarTitle(testPlant.getName())));
        onView(withId(R.id.plant_detail)).check(matches(withText(testPlant.getDescription())));

        activityTestRule.getActivity().setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.toolbar_layout))
                .check(matches(withCollapsingToolbarTitle(testPlant.getName())));
        onView(withId(R.id.plant_detail)).check(matches(withText(testPlant.getDescription())));
    }

}
