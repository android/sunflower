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

package com.google.samples.apps.sunflower.compose.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.compose.garden.GardenScreen
import com.google.samples.apps.sunflower.compose.plantlist.PlantListScreen
import com.google.samples.apps.sunflower.compose.utils.SunflowerTopBar
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.PlantAndGardenPlantings
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModel
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel
import kotlinx.coroutines.launch

enum class SunflowerPage(
        @StringRes val titleResId: Int,
        @DrawableRes val drawableResId: Int
) {
    MY_GARDEN(R.string.my_garden_title, R.drawable.ic_my_garden_active),
    PLANT_LIST(R.string.plant_list_title, R.drawable.ic_plant_list_active)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
        modifier: Modifier = Modifier,
        onPlantClick: (Plant) -> Unit = {},
        onPageChange: (SunflowerPage) -> Unit = {},
        plantListViewModel: PlantListViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()

    Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = { SunflowerTopBar(pagerState){
            } },
            backgroundColor = MaterialTheme.colorScheme.secondary
    ) { paddingValues ->
        HomePagerScreen(
                pagerState =pagerState,
                onPlantClick = onPlantClick,
                onPageChange = onPageChange,
                plantListViewModel = plantListViewModel,
                modifier = Modifier
                        .padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
        pagerState: PagerState,
        onPlantClick: (Plant) -> Unit,
        onPageChange: (SunflowerPage) -> Unit,
        modifier: Modifier = Modifier,
        pages: Array<SunflowerPage> = SunflowerPage.values(),
        gardenPlantingListViewModel: GardenPlantingListViewModel = hiltViewModel(),
        plantListViewModel: PlantListViewModel = hiltViewModel(),
) {
    val gardenPlants by gardenPlantingListViewModel.plantAndGardenPlantings.collectAsState(initial = emptyList())
    val plants by plantListViewModel.plants.observeAsState(initial = emptyList())
    HomePagerScreen(
            pagerState = pagerState,
            onPlantClick = onPlantClick,
            onPageChange = onPageChange,
            modifier = modifier,
            pages = pages,
            gardenPlants = gardenPlants,
            plants = plants
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
        pagerState: PagerState,
        onPlantClick: (Plant) -> Unit,
        onPageChange: (SunflowerPage) -> Unit,
        modifier: Modifier = Modifier,
        pages: Array<SunflowerPage> = SunflowerPage.values(),
        gardenPlants: List<PlantAndGardenPlantings>,
        plants: List<Plant>,
) {

    LaunchedEffect(pagerState.currentPage) {
        onPageChange(pages[pagerState.currentPage])
    }

    // Use Modifier.nestedScroll + rememberNestedScrollInteropConnection() here so that this
    // composable participates in the nested scroll hierarchy so that HomeScreen can be used in
    // use cases like a collapsing toolbar
    Column(modifier
            .nestedScroll(rememberNestedScrollInteropConnection())) {
        val coroutineScope = rememberCoroutineScope()

        // Tab Row
        TabRow(selectedTabIndex = pagerState.currentPage,
                backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary) {
            pages.forEachIndexed { index, page ->
                val title = stringResource(id = page.titleResId)
                Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(text = title) },
                        icon = {
                            Icon(
                                    painter = painterResource(id = page.drawableResId),
                                    contentDescription = title
                            )
                        },
                        unselectedContentColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                        selectedContentColor = androidx.compose.material3.MaterialTheme.colorScheme.tertiary,
                )
            }
        }

        // Pages
        HorizontalPager(
                pageCount = pages.size,
                state = pagerState,
                verticalAlignment = Alignment.Top
        ) { index ->
            when (pages[index]) {
                SunflowerPage.MY_GARDEN -> {
                    GardenScreen(
                            gardenPlants = gardenPlants,
                            Modifier.fillMaxSize(),
                            onAddPlantClick = {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(SunflowerPage.PLANT_LIST.ordinal)
                                }
                            },
                            onPlantClick = {
                                onPlantClick(it.plant)
                            })
                }

                SunflowerPage.PLANT_LIST -> {
                    PlantListScreen(
                            plants = plants,
                            onPlantClick = onPlantClick,
                            modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun HomeScreenPreview(
        @PreviewParameter(HomeScreenPreviewParamProvider::class) param: HomePreviewParam
) {
    MdcTheme {
        HomePagerScreen(
                pagerState = rememberPagerState(),
                onPlantClick = {},
                onPageChange = {},
                gardenPlants = param.gardenPlants,
                plants = param.plants
        )
    }
}

private data class HomePreviewParam(
        val gardenPlants: List<PlantAndGardenPlantings>,
        val plants: List<Plant>,
)

private class HomeScreenPreviewParamProvider : PreviewParameterProvider<HomePreviewParam> {
    override val values: Sequence<HomePreviewParam> =
            sequenceOf(
                    HomePreviewParam(
                            gardenPlants = emptyList(),
                            plants = emptyList()
                    ),
                    HomePreviewParam(
                            gardenPlants = emptyList(),
                            plants = listOf(
                                    Plant("1", "Apple", "Apple", growZoneNumber = 1),
                                    Plant("2", "Banana", "Banana", growZoneNumber = 2),
                                    Plant("3", "Carrot", "Carrot", growZoneNumber = 3),
                                    Plant("4", "Dill", "Dill", growZoneNumber = 3),
                            )
                    )
            )
}