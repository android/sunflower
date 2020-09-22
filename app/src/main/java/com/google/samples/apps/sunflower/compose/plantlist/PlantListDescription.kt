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

package com.google.samples.apps.sunflower.compose.plantlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.samples.apps.sunflower.compose.Dimens

//Equivalent to MaskedCardView
@Composable
private fun CardView(onClick: () -> Unit,
                     modifier: Modifier = Modifier,
                     content: @Composable () -> Unit) {
    Card(
            modifier = modifier.padding(
                    start = Dimens.CardSideMargin,
                    end = Dimens.CardSideMargin,
                    bottom = Dimens.CardBottomMargin)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = onClick),
            shape = RoundedCornerShape(
                    topLeft = Dimens.CornerRadiusFlat,
                    topRight = Dimens.CornerRadius,
                    bottomLeft = Dimens.CornerRadius,
                    bottomRight = Dimens.CornerRadiusFlat),
            elevation = Dimens.CardElevation,
            content = content,
            backgroundColor = MaterialTheme.colors.surface
    )
}
