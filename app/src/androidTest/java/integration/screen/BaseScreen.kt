package integration.screen

import android.app.Activity
import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.utilities.getToolbarNavigationContentDescription

open class BaseScreen {

    protected val currentActivity: Activity
        get() {
            getInstrumentation().waitForIdleSync()
            val activity = arrayOfNulls<Activity>(1)
            getInstrumentation().runOnMainSync {
                val activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
                activity[0] = Iterables.getOnlyElement(activities)
            }
            return activity[0] ?: throw RuntimeException("Failed to get current activity")
        }

    fun clickOnHomeIconToOpenNavigationDrawer() {
        Espresso.onView(ViewMatchers.withContentDescription(getToolbarNavigationContentDescription(
                currentActivity, R.id.toolbar))).perform(ViewActions.click())
    }
}