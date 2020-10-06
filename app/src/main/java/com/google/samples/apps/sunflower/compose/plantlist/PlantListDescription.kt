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

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.accessibilityLabel
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.viewModel
import com.google.samples.apps.sunflower.PlantListFragment
import com.google.samples.apps.sunflower.compose.Dimens
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel

/**
 * Stateful [PlantListScreen] that is responsible for managing state of composables
 */
@Composable
fun PlantListScreen(
        fragment: PlantListFragment,
        onClick: (String) -> Unit,
) {
    val viewModel = viewModel<PlantListViewModel>(
            factory = InjectorUtils.providePlantListViewModelFactory(fragment)
    )
    val plants by viewModel.plants.observeAsState(listOf())
    PlantListScreen(plants = plants, onClick = onClick)
}

/**
 * Stateless [PlantListScreen] that is responsible for just displaying data
 */
@Composable
fun PlantListScreen(
        plants: List<Plant>,
        onClick: (String) -> Unit,
        modifier: Modifier = Modifier
) {
    VerticalGridLayout(modifier = modifier.fillMaxWidth().padding(top = Dimens.HeaderMargin)) {
        plants.forEach {
            PlantListItem(it, onClick = onClick)
        }
    }
}

/**
 * displays the data in more than one column by default "2".
 *
 * Equivalent to Using StaggeredGridLayoutManager + app:spanCount="2" in xml/View system
 * @param [columnsNum] control the number of columns
 *
 * This is highly inspired by Owl sample app's grid layout,
 * @link https://github.com/android/compose-samples/blob/master/Owl/app/src/main/java/com/example/owl/ui/courses/FeaturedCourses.kt#L161
 */
@Composable
private fun VerticalGridLayout(
        columnsNum: Int = 2,
        modifier: Modifier = Modifier,
        item: @Composable () -> Unit
) {
    Layout(children = item, modifier = modifier) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            error("layouts with unBounded width Unsupported yet")
        }
        val columnWidth = constraints.maxWidth / columnsNum
        val itemConstraints = constraints.copy(maxWidth = columnWidth, minWidth = columnWidth)
        val columnsHeights = IntArray(columnsNum) { 0 } //track each column height For sake of measuring
        //Beginning of measuring stage
        val placeables = measurables.mapIndexed { itemIndex, measurable ->
            val columnIndex = itemIndex % columnsNum
            val placeable = measurable.measure(itemConstraints)
            columnsHeights[columnIndex] += placeable.height
            placeable
        }
        val layoutHeight = columnsHeights
                .maxOrNull()
                ?.coerceIn(constraints.minHeight, constraints.maxHeight) ?: constraints.minHeight
        //Beginning of layouting stage
        layout(
                width = constraints.maxWidth,
                height = layoutHeight
        ) {
            val itemY = IntArray(columnsNum) { 0 }
            val itemX = IntArray(columnsNum) { it } //Initialize item x value based on index
            placeables.forEachIndexed { index, placeable ->
                val column = index % columnsNum
                placeable.placeRelative(
                        x = itemX[column] * columnWidth,
                        y = itemY[column]
                )
                itemY[column] += placeable.height
            }
        }
    }
}

@Composable
fun PlantListItem(
        plant: Plant,
        onClick: (String) -> Unit,
        modifier: Modifier = Modifier
) {
    CardView(modifier.clickable(onClick = { onClick(plant.plantId) })) {
        //Also, we can use constraint layout instead of column here
        Column(modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()) {
            val plantImgDesc = stringResource(R.string.a11y_plant_item_image)
            CoilImageWithCrossfade(
                    data = plant.imageUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(Dimens.PlantItemHeight)
                            .fillMaxWidth()
                            .semantics {
                                accessibilityLabel = plantImgDesc
                            }
            )
            Text(
                    text = plant.name,
                    //Mimics ?attr/textAppearanceListItem textAppearance
                    style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onSurface),
                    modifier = Modifier.padding(vertical = Dimens.MarginNormal)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    textAlign = TextAlign.Center
            )
        }
    }
}

//Equivalent to MaskedCardView
@Composable
private fun CardView(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
            modifier = modifier.padding(
                    start = Dimens.CardSideMargin,
                    end = Dimens.CardSideMargin,
                    bottom = Dimens.CardBottomMargin)
                    .fillMaxWidth()
                    .wrapContentHeight(),
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
