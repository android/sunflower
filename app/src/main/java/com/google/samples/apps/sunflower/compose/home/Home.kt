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

package com.google.samples.apps.sunflower.compose.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.FragmentManager
import com.google.samples.apps.sunflower.HomeViewPagerFragment
import com.google.samples.apps.sunflower.compose.AndroidFragment

@Composable
fun Home(
  supportFragmentManager: FragmentManager,
  onPlantClicked: (String) -> Unit
) {
  // Set listener
  val currentOnPlantClicked by rememberUpdatedState(newValue = onPlantClicked)
  supportFragmentManager.setFragmentResultListener(
    "plantDetailRequestKey",
    LocalLifecycleOwner.current
  ) { _, bundle ->
    bundle.getString("plantId")?.let {
      currentOnPlantClicked(it)
    }
  }

  AndroidFragment(factory = {
    HomeViewPagerFragment()
  }, Modifier.fillMaxSize())

//  var fragmentState: Fragment.SavedState? by rememberSaveable {
//    mutableStateOf(null)
//  }
//
//  AndroidViewBinding(factory = { inflater, parent, attachToParent ->
//    val binding =
//      HomeViewPagerFragmentBinding.inflate(inflater, parent, attachToParent)
//
//    val fragment = HomeViewPagerFragment()
//    fragment.setInitialSavedState(fragmentState)
//    supportFragmentManager.commit {
//      replace(
//        R.id.home_view_pager_fragment_container,
//        fragment
//      )
//    }
//
//    binding
//  })
//
//  DisposableEffect(supportFragmentManager) {
//    onDispose {
//      val fragment = supportFragmentManager.findFragmentById(R.id.home_view_pager_fragment_container)
//      if (fragment != null) {
//        fragmentState = supportFragmentManager.saveFragmentInstanceState(
//          fragment
//        )
//      }
//    }
//  }
}

