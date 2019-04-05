package integration.screen

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.utilities.chooser
import com.google.samples.apps.sunflower.utilities.testPlant
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.startsWith

class PlantScreen : BaseScreen() {

    fun checkPlant(name: String, watering: Int, description: String): PlantScreen {
        onView(withId(R.id.plant_name)).check(matches(withText(name)))
        onView(withId(R.id.plant_watering)).check(matches(withText(
                currentActivity.getString(R.string.watering_needs_prefix) + " " +
                        currentActivity.resources.getQuantityString(R.plurals.watering_needs_suffix, watering, watering))))
        onView(withId(R.id.plant_description)).check(matches(withText(startsWith(description))))
        return this
    }

    fun addPlant(): PlantScreen {
        fabInteraction().perform(click())
        return this
    }

    fun checkNoAddPlant(): PlantScreen {
        fabInteraction().check(matches(not(isDisplayed())))
        return this
    }

    private fun fabInteraction() = onView(withId(R.id.fab))

    fun checkAddedMessageShown(): PlantScreen {
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.added_plant_to_garden)))

        return this
    }

    fun checkShare() {
        val shareText = currentActivity.getString(R.string.share_text_plant, testPlant.name)

        Intents.init()
        onView(withId(R.id.action_share)).perform(click())
        Intents.intended(chooser(CoreMatchers.allOf(
                IntentMatchers.hasAction(Intent.ACTION_SEND),
                IntentMatchers.hasType("text/plain"),
                IntentMatchers.hasExtra(Intent.EXTRA_TEXT, shareText)))
        )
        Intents.release()

        // dismiss the Share Dialog
        InstrumentationRegistry.getInstrumentation()
                .uiAutomation
                .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }
}