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

package com.google.samples.apps.sunflower.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.samples.apps.sunflower.R

/**
 * Class that captures dimens used in Compose code. The dimens that need to be consistent with the
 * View system use [dimensionResource] and are marked as composable.
 *
 * Disclaimer:
 * This approach doesn't consider multiple configurations. For that, an Ambient should be created.
 */
object Dimens {

    @Composable
    val PaddingSmall: Dp
        get() = dimensionResource(R.dimen.margin_small)

    @Composable
    val PaddingNormal: Dp
        get() = dimensionResource(R.dimen.margin_normal)

    val PaddingLarge: Dp = 24.dp

    @Composable
    val PlantDetailAppBarHeight: Dp
        get() = dimensionResource(R.dimen.plant_detail_app_bar_height)

    val ToolbarIconPadding = 12.dp

    val ToolbarIconSize = 32.dp

    //Card dimens
    @Composable
    val CornerRadius: Dp
        get() = dimensionResource(R.dimen.card_corner_radius)

    val CornerRadiusFlat = 0.dp

    @Composable
    val CardSideMargin: Dp
        get() = dimensionResource(R.dimen.card_side_margin)

    @Composable
    val CardBottomMargin: Dp
        get() = dimensionResource(R.dimen.card_bottom_margin)

    @Composable
    val CardElevation: Dp
        get() = dimensionResource(R.dimen.card_elevation)

    //Plant item dimens
    @Composable
    val PlantItemHeight: Dp
        get() = dimensionResource(R.dimen.plant_item_image_height)

    @Composable
    val MarginNormal: Dp
        get() = dimensionResource(R.dimen.margin_normal)

    @Composable
    val HeaderMargin: Dp
        get() = dimensionResource(R.dimen.header_margin)
}
