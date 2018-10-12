package com.google.samples.apps.sunflower.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.samples.apps.sunflower.data.Plant

class PlantItemViewModelFactory(private val plant: Plant) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlantItemViewModel(plant) as T
    }
}