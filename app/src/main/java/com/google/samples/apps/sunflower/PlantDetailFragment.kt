/*
 * Copyright 2018 Google LLC
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

package com.google.samples.apps.sunflower

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.samples.apps.sunflower.databinding.FragmentPlantDetailBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel
import androidx.navigation.findNavController
import androidx.core.widget.NestedScrollView

/**
 * A fragment representing a single Plant detail screen.
 */
class PlantDetailFragment : Fragment() {

    private val args: PlantDetailFragmentArgs by navArgs()
    private lateinit var shareText: String

    private val plantDetailViewModel: PlantDetailViewModel by viewModels {
        InjectorUtils.providePlantDetailViewModelFactory(requireActivity(), args.plantId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPlantDetailBinding>(
                inflater, R.layout.fragment_plant_detail, container, false).apply {
            viewModel = plantDetailViewModel
            lifecycleOwner = this@PlantDetailFragment
            fab.setOnClickListener { view ->
                hideAppBarFab(view as FloatingActionButton)
                plantDetailViewModel.addPlantToGarden()
                Snackbar.make(view, R.string.added_plant_to_garden, Snackbar.LENGTH_LONG).show()
            }

            toolbarLayout.setCollapsedTitleTextColor(R.color.colorPrimaryDark)

            // scroll change listener begins at Y = 0 when image is fully collapsed
            plantDetailScrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                // if scroll past image to height of toolbar, plant name in body is off screen
                if (scrollY > toolbar.height) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        // convert dp value from dimens to pixel value
                        // set regular shadow to toolbar
                        appbar.elevation = resources.displayMetrics.density *
                                resources.getDimension(R.dimen.toolbar_elevation)
                    }

                    // set toolbar title to plant name
                    toolbarLayout.title = viewModel?.plant?.value?.name
                } else { // if plant name in body is still visible

                    // remove title (since plant name in body is used as title)
                    toolbarLayout.title = " "
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        // remove shadow from toolbar
                        appbar.elevation = 0.0f
                    }
                }
            })

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        createShareIntent()
                        true
                    }
                    else -> false
                }
            }
        }

        plantDetailViewModel.plant.observe(this) { plant ->
            shareText = if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_plant, plant.name)
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
        val shareIntent = ShareCompat.IntentBuilder.from(activity)
                .setText(shareText)
                .setType("text/plain")
                .createChooserIntent()
                .apply {
                    // https://android-developers.googleblog.com/2012/02/share-with-intents.html
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // If we're on Lollipop, we can open the intent as a document
                        addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    } else {
                        // Else, we will use the old CLEAR_WHEN_TASK_RESET flag
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                    }
                }
        startActivity(shareIntent)
    }

    // FloatingActionButtons anchored to AppBarLayouts have their visibility controlled by the scroll position.
    // We want to turn this behavior off to hide the FAB when it is clicked.
    //
    // This is adapted from Chris Banes' Stack Overflow answer: https://stackoverflow.com/a/41442923
    private fun hideAppBarFab(fab: FloatingActionButton) {
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior
        behavior.isAutoHideEnabled = false
        fab.hide()
    }
}
