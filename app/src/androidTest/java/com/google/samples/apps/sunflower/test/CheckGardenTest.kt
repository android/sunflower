package com.google.samples.apps.sunflower.test

import androidx.test.rule.ActivityTestRule
import com.google.samples.apps.sunflower.GardenActivity
import com.google.samples.apps.sunflower.screen.GardenScreen
import com.google.samples.apps.sunflower.utilities.Apple
import org.junit.Rule
import org.junit.Test


class CheckGardenTest {

    @Rule
    @JvmField
    var gardenActivityTestRule = ActivityTestRule(GardenActivity::class.java)

    @Test
    fun checkApplePlanted() {
        GardenScreen().launch(gardenActivityTestRule)
                .openPlan(Apple.NAME)
                .checkPlant(Apple.NAME, Apple.WATERING, Apple.DESCRIPTION)
                .checkNoAddPlant()
    }

    @Test
    fun checkShareApple() {
        GardenScreen().launch(gardenActivityTestRule)
                .openPlan(Apple.NAME)
                .checkShare()
    }
}