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

package com.google.samples.apps.sunflower.utilities

import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.samples.apps.sunflower.R

fun AppCompatActivity.setUpAppbar(view: View, isHome: Boolean) {
    setSupportActionBar(view.findViewById(R.id.toolbar))
    if (isHome) {
        NavigationUI.setupActionBarWithNavController(
            this,
            Navigation.findNavController(this, R.id.nav_controller),
            findViewById(R.id.drawer_layout)
        )
    } else {
        NavigationUI.setupActionBarWithNavController(
            this,
            Navigation.findNavController(this, R.id.nav_controller)
        )
    }
}