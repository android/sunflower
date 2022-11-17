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

package com.google.samples.apps.sunflower.compose

import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.samples.apps.sunflower.compose.home.Home
import com.google.samples.apps.sunflower.compose.plantdetail.PlantDetails

@Composable
fun SunflowerApp(fragmentManager: FragmentManager) {
  MdcTheme {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
      composable("home") {
        Home(fragmentManager) { plantId ->
          navController.navigate("plant/$plantId")
        }
      }
      composable(
        "plant/{plantId}",
        arguments = listOf(navArgument("plantId") {
          type = NavType.StringType
        })
      ) { backStackEntry ->
        val plantId = backStackEntry.arguments?.getString("plantId")
        PlantDetails(fragmentManager, plantId) {
          navController.navigateUp()
        }
      }
    }
  }
}
