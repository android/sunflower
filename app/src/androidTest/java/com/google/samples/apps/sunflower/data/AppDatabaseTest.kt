package com.google.samples.apps.sunflower.data

import android.support.test.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class AppDatabaseTest {

    @Test fun readJson() {
        assertNotEquals("", AppDatabase.readJson(InstrumentationRegistry.getTargetContext()))
    }

    @Test fun readJson_invalidFile_returnsEmptyString() {
        assertEquals("", AppDatabase.readJson(InstrumentationRegistry.getTargetContext(), "foo"))
    }

}