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

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

// Value obtained empirically so that the header buttons don't surpass the header container
private val HeaderTransitionOffset = 92.dp

/**
 * Class that contains derived state for when the toolbar should be shown
 */
data class PlantDetailsScroller(
    val scrollState: ScrollState,
    val namePosition: Float
) {
    val toolbarTransitionState = MutableTransitionState(ToolbarState.HIDDEN)

    fun getToolbarState(density: Density): ToolbarState {
        // When the namePosition is placed correctly on the screen (position > 1f) and it's
        // position is close to the header, then show the toolbar.
        return if (namePosition > 1f &&
            scrollState.value > (namePosition - getTransitionOffset(density))
        ) {
            toolbarTransitionState.targetState = ToolbarState.SHOWN
            ToolbarState.SHOWN
        } else {
            toolbarTransitionState.targetState = ToolbarState.HIDDEN
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


@Composable
fun rememberToolbarTransition(
    transitionState: MutableTransitionState<ToolbarState>
): Pair<State<Float>, State<Float>> {
    val transition = updateTransition(transitionState)
    val toolbarAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) {
        if (it == ToolbarState.HIDDEN) 0f else 1f
    }
    val contentAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) {
        if (it == ToolbarState.HIDDEN) 1f else 0f
    }
    return remember(transitionState) { Pair(toolbarAlpha, contentAlpha) }
}
