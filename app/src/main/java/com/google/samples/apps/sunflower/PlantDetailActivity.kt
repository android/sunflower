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

package com.google.samples.apps.sunflower

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * An activity representing a single Plant detail screen.
 */
class PlantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_detail)

        if (savedInstanceState == null) {
            val plantId = intent.getStringExtra(PlantDetailFragment.ARG_ITEM_ID)
            val fragment = PlantDetailFragment.newInstance(plantId)
            supportFragmentManager.beginTransaction()
                    .add(R.id.plant_detail_container, fragment)
                    .commit()
        }
    }

    // According to the Principles of Navigation*, Up and Back should function identically when the
    // back button does not exit the app.
    // * https://developer.android.com/topic/libraries/architecture/navigation/navigation-principles
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
