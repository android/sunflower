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

import android.text.method.LinkMovementMethod
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.transition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.globalPosition
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.accessibilityLabel
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.compose.ui.viewinterop.viewModel
import androidx.core.text.HtmlCompat
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.compose.Dimens
import com.google.samples.apps.sunflower.compose.utils.TextSnackbarContainer
import com.google.samples.apps.sunflower.compose.utils.getQuantityString
import com.google.samples.apps.sunflower.compose.visible
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.ItemPlantDescriptionBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import dev.chrisbanes.accompanist.insets.systemBarsPadding

/**
 * As these callbacks are passed in through multiple Composables, to avoid having to name
 * parameters to not mix them up, they're aggregated in this class.
 */
data class PlantDetailsCallbacks(
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onShareClick: () -> Unit
)

@Composable
fun PlantDetailsScreen(
    plantId: String,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit
) {
    // ViewModel and LiveDatas needed to populate the plant details info on the screen
    val plantDetailsViewModel: PlantDetailViewModel = viewModel(
        factory = InjectorUtils.providePlantDetailViewModelFactory(AmbientContext.current, plantId)
    )
    val plant = plantDetailsViewModel.plant.observeAsState().value
    val isPlanted = plantDetailsViewModel.isPlanted.observeAsState().value
    val showSnackbar = plantDetailsViewModel.showSnackbar.observeAsState().value

    if (plant != null && isPlanted != null && showSnackbar != null) {
        Surface {
            TextSnackbarContainer(
                snackbarText = stringResource(R.string.added_plant_to_garden),
                showSnackbar = showSnackbar,
                onDismissSnackbar = { plantDetailsViewModel.dismissSnackbar() }
            ) {
                val context = AmbientContext.current
                PlantDetails(
                    plant, isPlanted,
                    PlantDetailsCallbacks(
                        onBackClick = onBackClick,
                        onFabClick = {
                            plantDetailsViewModel.addPlantToGarden()
                        },
                        onShareClick = {
                            val shareText = context.resources.getString(R.string.share_text_plant, plant.name)
                            onShareClick(shareText)
                        }
                    )
                )
            }
        }
    }
}

@VisibleForTesting
@Composable
fun PlantDetails(
    plant: Plant,
    isPlanted: Boolean,
    callbacks: PlantDetailsCallbacks,
    modifier: Modifier = Modifier
) {
    // PlantDetails owns the scrollerPosition to simulate CollapsingToolbarLayout's behavior
    val scrollState = rememberScrollState()
    var plantScroller by remember {
        mutableStateOf(PlantDetailsScroller(scrollState, Float.MIN_VALUE))
    }
    val toolbarState = plantScroller.getToolbarState(AmbientDensity.current)

    // Transition that fades in/out the header with the image and the Toolbar
    val transition = transition(toolbarTransitionDefinition, toolbarState)
    Box(modifier) {
        PlantDetailsContent(
            scrollState = scrollState,
            toolbarState = toolbarState,
            onNamePosition = { newNamePosition ->
                // Comparing to Float.MIN_VALUE as we are just interested on the original
                // position of name on the screen
                if (plantScroller.namePosition == Float.MIN_VALUE) {
                    plantScroller = plantScroller.copy(namePosition = newNamePosition)
                }
            },
            plant = plant,
            isPlanted = isPlanted,
            callbacks = callbacks,
            transitionState = transition
        )
        PlantHeader(toolbarState, plant.name, callbacks, transition)
    }
}

@Composable
private fun PlantDetailsContent(
    scrollState: ScrollState,
    toolbarState: ToolbarState,
    plant: Plant,
    isPlanted: Boolean,
    onNamePosition: (Float) -> Unit,
    callbacks: PlantDetailsCallbacks,
    transitionState: TransitionState
) {
    ScrollableColumn(scrollState = scrollState) {
        PlantImageHeader(
            scrollState, plant.imageUrl, callbacks.onFabClick, isPlanted,
            Modifier.graphicsLayer(alpha = transitionState[contentAlphaKey])
        )
        PlantInformation(
            name = plant.name,
            wateringInterval = plant.wateringInterval,
            description = plant.description,
            onNamePosition = { onNamePosition(it) },
            toolbarState = toolbarState
        )
    }
}

@Composable
private fun PlantHeader(
    toolbarState: ToolbarState,
    plantName: String,
    callbacks: PlantDetailsCallbacks,
    transitionState: TransitionState
) {
    if (toolbarState.isShown) {
        PlantDetailsToolbar(
            plantName = plantName,
            onBackClick = callbacks.onBackClick,
            onShareClick = callbacks.onShareClick,
            modifier = Modifier.alpha(transitionState[toolbarAlphaKey])
        )
    } else {
        PlantHeaderActions(
            onBackClick = callbacks.onBackClick,
            onShareClick = callbacks.onShareClick,
            modifier = Modifier.alpha(transitionState[contentAlphaKey])
        )
    }
}

@Composable
private fun PlantDetailsToolbar(
    plantName: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        TopAppBar(
            modifier = modifier.statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            IconButton(onBackClick, Modifier.align(Alignment.CenterVertically)) {
                Icon(Icons.Filled.ArrowBack)
            }
            Text(
                text = plantName,
                style = MaterialTheme.typography.h6,
                // As title in TopAppBar has extra inset on the left, need to do this: b/158829169
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            val shareAccessibilityLabel = stringResource(R.string.menu_item_share_plant)
            IconButton(
                onShareClick,
                Modifier.align(Alignment.CenterVertically).semantics {
                    accessibilityLabel = shareAccessibilityLabel
                }
            ) {
                Icon(Icons.Filled.Share)
            }
        }
    }
}

@Composable
private fun PlantImageHeader(
    scrollState: ScrollState,
    imageUrl: String,
    onFabClick: () -> Unit,
    isPlanted: Boolean,
    modifier: Modifier = Modifier
) {
    var imageHeight by remember { mutableStateOf(0) }

    Box(Modifier.fillMaxWidth()) {
        PlantImage(
            scrollState, imageUrl,
            modifier.onSizeChanged { imageHeight = it.height }
        )
        if (!isPlanted) {
            val fabModifier = if (imageHeight != 0) {
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = Dimens.PaddingSmall)
                    .offset(y = getFabOffset(imageHeight, scrollState))
                    .then(modifier)
            } else {
                Modifier.visible { false }
            }
            val fabAccessibilityLabel = stringResource(R.string.add_plant)
            FloatingActionButton(
                onClick = onFabClick,
                shape = MaterialTheme.shapes.small,
                modifier = fabModifier.semantics {
                    accessibilityLabel = fabAccessibilityLabel
                }
            ) {
                Icon(Icons.Filled.Add)
            }
        }
    }
}

@Composable
private fun PlantImage(
    scrollState: ScrollState,
    imageUrl: String,
    modifier: Modifier = Modifier,
    placeholderColor: Color = MaterialTheme.colors.onSurface.copy(0.2f)
) {
    val parallaxOffset = with(AmbientDensity.current) {
        scrollerParallaxOffset(this, scrollState)
    }
    CoilImage(
        data = imageUrl,
        contentScale = ContentScale.Crop,
        fadeIn = true,
        loading = {
            Box(modifier = Modifier.fillMaxSize().background(placeholderColor))
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = parallaxOffset)
            .preferredHeight(Dimens.PlantDetailAppBarHeight)
    )
}

@Composable
private fun PlantHeaderActions(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(top = Dimens.ToolbarIconPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val iconModifier = Modifier
            .sizeIn(maxWidth = Dimens.ToolbarIconSize, maxHeight = Dimens.ToolbarIconSize)
            .background(color = MaterialTheme.colors.surface, shape = CircleShape)

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(start = Dimens.ToolbarIconPadding).then(iconModifier)
        ) {
            Icon(Icons.Filled.ArrowBack)
        }
        val shareAccessibilityLabel = stringResource(R.string.menu_item_share_plant)
        IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .padding(end = Dimens.ToolbarIconPadding)
                .then(iconModifier)
                .semantics {
                    accessibilityLabel = shareAccessibilityLabel
                }
        ) {
            Icon(Icons.Filled.Share)
        }
    }
}

@Composable
private fun PlantInformation(
    name: String,
    wateringInterval: Int,
    description: String,
    onNamePosition: (Float) -> Unit,
    toolbarState: ToolbarState
) {
    Column(modifier = Modifier.padding(Dimens.PaddingLarge)) {
        Text(
            text = name,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
                .align(Alignment.CenterHorizontally)
                .onGloballyPositioned { onNamePosition(it.globalPosition.y) }
                .visible { toolbarState == ToolbarState.HIDDEN }
        )
        Text(
            text = stringResource(id = R.string.watering_needs_prefix),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = Dimens.PaddingSmall)
                .align(Alignment.CenterHorizontally)
        )
        Providers(AmbientContentAlpha provides ContentAlpha.medium) {
            Text(
                text = getQuantityString(R.plurals.watering_needs_suffix, wateringInterval),
                modifier = Modifier
                    .padding(
                        start = Dimens.PaddingSmall,
                        end = Dimens.PaddingSmall,
                        bottom = Dimens.PaddingNormal
                    )
                    .align(Alignment.CenterHorizontally)
            )
        }
        PlantDescription(description)
    }
}

@Composable
private fun PlantDescription(description: String) {
    AndroidViewBinding(ItemPlantDescriptionBinding::inflate) {
        plantDescription.text = HtmlCompat.fromHtml(
            description,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        plantDescription.movementMethod = LinkMovementMethod.getInstance()
        plantDescription.linksClickable = true
    }
}

/**
 * Calculates offset FAB needs to keep aligned in the middle of the bottom of the picture.
 *
 * As the [Modifier.onGloballyPositioned] in the image is invoked after scrollPosition has changed,
 * there's a frame delay.
 */
@Composable
private fun getFabOffset(imageHeight: Int, scrollState: ScrollState): Dp {
    return with(AmbientDensity.current) {
        imageHeight.toDp() + scrollerParallaxOffset(this, scrollState) - (56 / 2).dp
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    ProvideWindowInsets {
        MdcTheme {
            Surface {
                PlantDetails(
                    Plant("plantId", "Tomato", "HTML<br>description", 6),
                    true,
                    PlantDetailsCallbacks({ }, { }, { })
                )
            }
        }
    }
}
