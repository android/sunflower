package integration.test

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.samples.apps.sunflower.GardenActivity
import integration.screen.GardenScreen
import com.google.samples.apps.sunflower.utilities.Apple
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as isA


class AddFlowTest {

    @Rule
    @JvmField
    var gardenActivityTestRule = ActivityTestRule(GardenActivity::class.java, false, false)

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun addAppleToGarden() {
        val gardenScreen = GardenScreen()
        gardenScreen.launch(gardenActivityTestRule)
                .goToPlants()
                .openPlant(Apple.NAME)
                .checkPlant(Apple.NAME, Apple.WATERING, Apple.DESCRIPTION)
                .addPlant()
                .checkAddedMessageShown()
    }
}