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

package com.google.samples.apps.sunflower.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object PlantDetail : Screen(
        route = "plantDetail/{plantId}",
        navArguments = listOf(navArgument("plantId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(plantId: String) = "plantDetail/${plantId}"
    }

    data object Gallery : Screen(
        route = "gallery/{plantName}",
        navArguments = listOf(navArgument("plantName") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(plantName: String) = "gallery/${plantName}"

    }
}