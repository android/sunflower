package com.google.samples.apps.sunflower.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.samples.apps.sunflower.PlantListFragmentDirections
import com.google.samples.apps.sunflower.data.Plant

class PlantItemViewModel(
    val plant: Plant
) : ViewModel(), View.OnClickListener {

    override fun onClick(view: View) {
        val direction = PlantListFragmentDirections.ActionPlantListFragmentToPlantDetailFragment(plant.plantId)
        view.findNavController().navigate(direction)
    }
}