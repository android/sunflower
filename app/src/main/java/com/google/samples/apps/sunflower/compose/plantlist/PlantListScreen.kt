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

package com.google.samples.apps.sunflower.compose.plantlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant

@Composable
fun PlantListScreen(
    onPlantClick: (Plant) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlantListViewModel = hiltViewModel(),
) {
    val plants by viewModel.plants.observeAsState(initial = emptyList())
    PlantListScreen(plants = plants, modifier, onPlantClick = onPlantClick)
}

@Composable
fun PlantListScreen(
    plants: List<Plant>,
    modifier: Modifier = Modifier,
    onPlantClick: (Plant) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.testTag("plant_list"),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.card_side_margin),
            vertical = dimensionResource(id = R.dimen.header_margin)
        )
    ) {
        items(
            items = plants,
            key = { it.plantId }
        ) { plant ->
            PlantListItem(plant = plant) {
                onPlantClick(plant)
            }
        }
    }
}

@Preview
@Composable
private fun PlantListScreenPreview(
    @PreviewParameter(PlantListPreviewParamProvider::class) plants: List<Plant>
) {
    PlantListScreen(plants = plants)
}

private class PlantListPreviewParamProvider : PreviewParameterProvider<List<Plant>> {
    override val values: Sequence<List<Plant>> =
        sequenceOf(
            emptyList(),
            listOf(
                Plant("1", "Apple", "Apple", growZoneNumber = 1),
                Plant("2", "Banana", "Banana", growZoneNumber = 2),
                Plant("3", "Carrot", "Carrot", growZoneNumber = 3),
                Plant("4", "Dill", "Dill", growZoneNumber = 3),
            )
        )
}