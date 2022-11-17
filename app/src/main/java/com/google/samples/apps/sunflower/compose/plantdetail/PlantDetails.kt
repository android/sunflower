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

package com.google.samples.apps.sunflower.compose.plantdetail

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.samples.apps.sunflower.PlantDetailFragment
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.databinding.PlantDetailFragmentBinding

@Composable
fun PlantDetails(
  supportFragmentManager: FragmentManager,
  plantId: String?,
  onBackClicked: () -> Unit
) {
  val lifecycle = LocalLifecycleOwner.current
  AndroidViewBinding(factory = { inflater, parent, attachToParent ->
    supportFragmentManager.setFragmentResultListener(
      "plantDetailBackRequestKey",
      lifecycle
    ) { _, _ ->
      onBackClicked()
    }
    PlantDetailFragmentBinding.inflate(inflater, parent, attachToParent)
  }) {
    supportFragmentManager.commit {
      val bundle = bundleOf("plantId" to plantId)
      add<PlantDetailFragment>(
        R.id.plant_detail_fragment_container,
        args = bundle
      )
    }
  }
}
