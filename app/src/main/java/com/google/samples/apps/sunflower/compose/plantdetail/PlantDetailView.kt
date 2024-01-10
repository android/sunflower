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

import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.compose.Dimens
import com.google.samples.apps.sunflower.compose.utils.TextSnackbarContainer
import com.google.samples.apps.sunflower.compose.visible
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.ItemPlantDescriptionBinding
import com.google.samples.apps.sunflower.ui.SunflowerTheme
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

/**
 * As these callbacks are passed in through multiple Composables, to avoid having to name
 * parameters to not mix them up, they're aggregated in this class.
 */
data class PlantDetailsCallbacks(
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onShareClick: (String) -> Unit,
    val onGalleryClick: (Plant) -> Unit
)

@Composable
fun PlantDetailsScreen(
    plantDetailsViewModel: PlantDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit,
    onGalleryClick: (Plant) -> Unit,
) {
    val plant = plantDetailsViewModel.plant.observeAsState().value
    val isPlanted = plantDetailsViewModel.isPlanted.collectAsState(initial = false).value
    val showSnackbar = plantDetailsViewModel.showSnackbar.observeAsState().value

    if (plant != null && showSnackbar != null) {
        Surface {
            TextSnackbarContainer(
                snackbarText = stringResource(R.string.added_plant_to_garden),
                showSnackbar = showSnackbar,
                onDismissSnackbar = { plantDetailsViewModel.dismissSnackbar() }
            ) {
                PlantDetails(
                    plant,
                    isPlanted,
                    plantDetailsViewModel.hasValidUnsplashKey(),
                    PlantDetailsCallbacks(
                        onBackClick = onBackClick,
                        onFabClick = {
                            plantDetailsViewModel.addPlantToGarden()
                        },
                        onShareClick = onShareClick,
                        onGalleryClick = onGalleryClick,
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
    hasValidUnsplashKey: Boolean,
    callbacks: PlantDetailsCallbacks,
    modifier: Modifier = Modifier
) {
    // PlantDetails owns the scrollerPosition to simulate CollapsingToolbarLayout's behavior
    val scrollState = rememberScrollState()
    var plantScroller by remember {
        mutableStateOf(PlantDetailsScroller(scrollState, Float.MIN_VALUE))
    }
    val transitionState =
        remember(plantScroller) { plantScroller.toolbarTransitionState }
    val toolbarState = plantScroller.getToolbarState(LocalDensity.current)

    // Transition that fades in/out the header with the image and the Toolbar
    val transition = updateTransition(transitionState, label = "")
    val toolbarAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 0f else 1f
    }
    val contentAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 1f else 0f
    }

    Box(modifier.fillMaxSize()) {
        PlantDetailsContent(
            scrollState = scrollState,
            toolbarState = toolbarState,
            onNamePosition = { newNamePosition ->
                // Comparing to Float.MIN_VALUE as we are just interested on the original
                // position of name on the screen
                if (plantScroller.namePosition == Float.MIN_VALUE) {
                    plantScroller =
                        plantScroller.copy(namePosition = newNamePosition)
                }
            },
            plant = plant,
            isPlanted = isPlanted,
            hasValidUnsplashKey = hasValidUnsplashKey,
            imageHeight = with(LocalDensity.current) {
                val candidateHeight =
                    Dimens.PlantDetailAppBarHeight
                // FIXME: Remove this workaround when https://github.com/bumptech/glide/issues/4952
                // is released
                maxOf(candidateHeight, 1.dp)
            },
            onFabClick = callbacks.onFabClick,
            onGalleryClick = { callbacks.onGalleryClick(plant) },
            contentAlpha = { contentAlpha.value }
        )
        PlantToolbar(
            toolbarState, plant.name, callbacks,
            toolbarAlpha = { toolbarAlpha.value },
            contentAlpha = { contentAlpha.value }
        )
    }
}

@Composable
private fun PlantDetailsContent(
    scrollState: ScrollState,
    toolbarState: ToolbarState,
    plant: Plant,
    isPlanted: Boolean,
    hasValidUnsplashKey: Boolean,
    imageHeight: Dp,
    onNamePosition: (Float) -> Unit,
    onFabClick: () -> Unit,
    onGalleryClick: () -> Unit,
    contentAlpha: () -> Float,
) {
    Column(Modifier.verticalScroll(scrollState)) {
        ConstraintLayout {
            val (image, fab, info) = createRefs()

            PlantImage(
                imageUrl = plant.imageUrl,
                imageHeight = imageHeight,
                modifier = Modifier
                    .constrainAs(image) { top.linkTo(parent.top) }
                    .alpha(contentAlpha())
            )

            if (!isPlanted) {
                val fabEndMargin = Dimens.PaddingSmall
                PlantFab(
                    onFabClick = onFabClick,
                    modifier = Modifier
                        .constrainAs(fab) {
                            centerAround(image.bottom)
                            absoluteRight.linkTo(
                                parent.absoluteRight,
                                margin = fabEndMargin
                            )
                        }
                        .alpha(contentAlpha())
                )
            }

            PlantInformation(
                name = plant.name,
                wateringInterval = plant.wateringInterval,
                description = plant.description,
                hasValidUnsplashKey = hasValidUnsplashKey,
                onNamePosition = { onNamePosition(it) },
                toolbarState = toolbarState,
                onGalleryClick = onGalleryClick,
                modifier = Modifier.constrainAs(info) {
                    top.linkTo(image.bottom)
                }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PlantImage(
    imageUrl: String,
    imageHeight: Dp,
    modifier: Modifier = Modifier,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(0.2f)
) {
    var isLoading by remember { mutableStateOf(true) }
    Box(
        modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        if (isLoading) {
            // TODO: Update this implementation once Glide releases a version
            // that contains this feature: https://github.com/bumptech/glide/pull/4934
            Box(
                Modifier
                    .fillMaxSize()
                    .background(placeholderColor)
            )
        }
        GlideImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        ) {
            it.addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    isLoading = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    isLoading = false
                    return false
                }
            })
        }
    }
}

@Composable
private fun PlantFab(
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val addPlantContentDescription = stringResource(R.string.add_plant)
    FloatingActionButton(
        onClick = onFabClick,
        shape = MaterialTheme.shapes.small,
        // Semantics in parent due to https://issuetracker.google.com/184825850
        modifier = modifier.semantics {
            contentDescription = addPlantContentDescription
        }
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun PlantToolbar(
    toolbarState: ToolbarState,
    plantName: String,
    callbacks: PlantDetailsCallbacks,
    toolbarAlpha: () -> Float,
    contentAlpha: () -> Float
) {
    val onShareClick = {
        callbacks.onShareClick(plantName)
    }
    if (toolbarState.isShown) {
        PlantDetailsToolbar(
            plantName = plantName,
            onBackClick = callbacks.onBackClick,
            onShareClick = onShareClick,
            modifier = Modifier.alpha(toolbarAlpha())
        )
    } else {
        PlantHeaderActions(
            onBackClick = callbacks.onBackClick,
            onShareClick = onShareClick,
            modifier = Modifier.alpha(contentAlpha())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlantDetailsToolbar(
    plantName: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        TopAppBar(
            modifier = modifier
                .statusBarsPadding()
                .background(color = MaterialTheme.colorScheme.surface),
            title = {
                Row {
                    IconButton(
                        onBackClick,
                        Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.a11y_back)
                        )
                    }
                    Text(
                        text = plantName,
                        style = MaterialTheme.typography.titleLarge,
                        // As title in TopAppBar has extra inset on the left, need to do this: b/158829169
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                    val shareContentDescription =
                        stringResource(R.string.menu_item_share_plant)
                    IconButton(
                        onShareClick,
                        Modifier
                            .align(Alignment.CenterVertically)
                            // Semantics in parent due to https://issuetracker.google.com/184825850
                            .semantics { contentDescription = shareContentDescription }
                    ) {
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
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
            .sizeIn(
                maxWidth = Dimens.ToolbarIconSize,
                maxHeight = Dimens.ToolbarIconSize
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = Dimens.ToolbarIconPadding)
                .then(iconModifier)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.a11y_back)
            )
        }
        val shareContentDescription =
            stringResource(R.string.menu_item_share_plant)
        IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .padding(end = Dimens.ToolbarIconPadding)
                .then(iconModifier)
                // Semantics in parent due to https://issuetracker.google.com/184825850
                .semantics {
                    contentDescription = shareContentDescription
                }
        ) {
            Icon(
                Icons.Filled.Share,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun PlantInformation(
    name: String,
    wateringInterval: Int,
    description: String,
    hasValidUnsplashKey: Boolean,
    onNamePosition: (Float) -> Unit,
    toolbarState: ToolbarState,
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(Dimens.PaddingLarge)) {
        Text(
            text = name,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
                .align(Alignment.CenterHorizontally)
                .onGloballyPositioned { onNamePosition(it.positionInWindow().y) }
                .visible { toolbarState == ToolbarState.HIDDEN }
        )
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
        ) {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.watering_needs_prefix),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = Dimens.PaddingSmall)
                        .align(Alignment.CenterHorizontally)
                )

                val wateringIntervalText = pluralStringResource(
                    R.plurals.watering_needs_suffix, wateringInterval, wateringInterval
                )

                Text(
                    text = wateringIntervalText,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
            if (hasValidUnsplashKey) {
                Image(
                    painter = painterResource(id = R.drawable.ic_photo_library),
                    contentDescription = "Gallery Icon",
                    Modifier
                        .clickable { onGalleryClick() }
                        .align(Alignment.CenterEnd)
                )
            }
        }
        PlantDescription(description)
    }
}

@Composable
private fun PlantDescription(description: String) {
    // This remains using AndroidViewBinding because this feature is not in Compose yet
    AndroidViewBinding(ItemPlantDescriptionBinding::inflate) {
        plantDescription.text = HtmlCompat.fromHtml(
            description,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        plantDescription.movementMethod = LinkMovementMethod.getInstance()
        plantDescription.linksClickable = true
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    SunflowerTheme {
        Surface {
            PlantDetails(
                Plant("plantId", "Tomato", "HTML<br>description", 6),
                true,
                true,
                PlantDetailsCallbacks({ }, { }, { }, { })
            )
        }
    }
}
