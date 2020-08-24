/*
 * Copyright 2020 Google LLC
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

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val HeaderTransitionOffset = 32.dp
private const val ParallaxFactor = 2f

/**
 * Class that contains derived state for when the toolbar should be shown
 */
data class PlantDetailsScroller(
    val scrollState: ScrollState,
    val namePosition: Float
) {
    fun getToolbarState(density: Density): ToolbarState {
        return if (namePosition != 0f &&
            scrollState.value > (namePosition + getTransitionOffset(density))
        ) {
            ToolbarState.SHOWN
        } else {
            ToolbarState.HIDDEN
        }
    }

    private fun getTransitionOffset(density: Density): Float = with(density) {
        HeaderTransitionOffset.toPx()
    }
}

// Toolbar state related classes and functions to achieve the CollapsingToolbarLayout animation
enum class ToolbarState { HIDDEN, SHOWN }

val ToolbarState.isShown
    get() = this == ToolbarState.SHOWN

val toolbarAlphaKey = FloatPropKey()
val contentAlphaKey = FloatPropKey()

val toolbarTransitionDefinition = transitionDefinition<ToolbarState> {
    state(ToolbarState.HIDDEN) {
        this[toolbarAlphaKey] = 0f
        this[contentAlphaKey] = 1f
    }
    state(ToolbarState.SHOWN) {
        this[toolbarAlphaKey] = 1f
        this[contentAlphaKey] = 0f
    }
    transition {
        toolbarAlphaKey using spring(
            stiffness = Spring.StiffnessLow
        )
        contentAlphaKey using spring(
            stiffness = Spring.StiffnessLow
        )
    }
}

@Composable
fun scrollerParallaxOffset(density: Density, scrollState: ScrollState): Dp =
    with(density) {
        (scrollState.value / ParallaxFactor).toDp()
    }
