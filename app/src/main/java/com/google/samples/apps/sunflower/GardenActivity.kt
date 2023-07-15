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
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.samples.apps.sunflower.compose.SunflowerApp
import com.google.samples.apps.sunflower.compose.home.SunflowerPage
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: update the superclass to ComponentActivity https://github.com/android/sunflower/issues/829
@AndroidEntryPoint
class GardenActivity : AppCompatActivity() {

    private val viewModel: PlantListViewModel by viewModels()

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.menu_plant_list, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.filter_zone -> {
                    viewModel.updateData()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Displaying edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(ComposeView(this).apply {
            // Provide a different ComposeView instead of using ComponentActivity's setContent
            // method so that the `consumeWindowInsets` property can be set to `false`. This is
            // needed so that insets can be properly applied to `HomeScreen` which uses View interop
            // This can likely be changed when `HomeScreen` is fully in Compose.
            consumeWindowInsets = false
            setContent {
                MdcTheme {
                    SunflowerApp(
                        onAttached = { toolbar ->
                            setSupportActionBar(toolbar)
                        },
                        onPageChange = { page ->
                            when (page) {
                                SunflowerPage.MY_GARDEN -> removeMenuProvider(menuProvider)
                                SunflowerPage.PLANT_LIST -> addMenuProvider(
                                    menuProvider,
                                    this@GardenActivity
                                )
                            }
                        },
                        plantListViewModel = viewModel,
                    )
                }
            }
        })
    }
}