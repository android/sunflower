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

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit

@Composable
fun <T : Fragment> AndroidFragment(
  factory: (context: Context) -> T,
  modifier: Modifier = Modifier,
  update: (T) -> Unit = {}
) {
  val context = LocalContext.current
  val fragmentManager = remember(context) {
    // Must be used within a FragmentActivity
    (context as FragmentActivity).supportFragmentManager
  }

  var fragmentState: Fragment.SavedState? by rememberSaveable { mutableStateOf(null) }
  var fragment = remember<T?> { null }

  AndroidView(factory = { c ->
    val fragmentContainerView = FragmentContainerView(c).apply {
      id = View.generateViewId()
    }
    fragmentManager.commit {
      val f = factory(c).apply { setInitialSavedState(fragmentState) }
      add(fragmentContainerView.id, f)
      fragment = f
    }
    fragmentContainerView
  }, modifier) {
    val currentFragment = fragment
    if (currentFragment != null) {
      update(currentFragment)
    }
  }

  // Taken from AndroidViewBinding
  DisposableEffect(context) {
    onDispose {
      val f = fragment
      if (f != null) {
        // Remember Fragment.SavedState
        fragmentState = fragmentManager.saveFragmentInstanceState(f)
        if (!fragmentManager.isStateSaved) {
          fragmentManager.commit {
            remove(f)
          }
        }
      }
    }
  }
}
