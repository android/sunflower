package com.google.samples.apps.sunflower.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText

class PlantsScreen : BaseScreen() {

    fun openPlant(name: String): PlantScreen {
        onView(withText(name)).perform(click())

        return PlantScreen()
    }
}