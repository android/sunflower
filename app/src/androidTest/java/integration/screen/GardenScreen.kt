package integration.screen

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.google.samples.apps.sunflower.GardenActivity
import com.google.samples.apps.sunflower.R
import java.text.SimpleDateFormat
import java.util.*

class GardenScreen : BaseScreen() {

    fun launch(gardenActivityTestRule: ActivityTestRule<GardenActivity>): GardenScreen {
        gardenActivityTestRule.launchActivity(Intent())
        return this
    }

    fun goToPlants(): PlantsScreen {
        clickOnHomeIconToOpenNavigationDrawer()
        onView(withText(R.string.plant_list_title)).perform(click())
        return PlantsScreen()
    }

    fun openPlan(name: String): PlantScreen {
        val text = currentActivity.getString(R.string.planted_date, name, DATE_FORMAT.format(Calendar.getInstance().time))
        onView(withText(text))
                .perform(click())

        return PlantScreen()
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("MMM d, yyyy", Locale.US)
    }
}