package com.google.samples.apps.sunflower.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlantTest {

    private Plant plant;

    @Before
    public void setUp() throws Exception {
        plant = new Plant("1", "Tomato", "A red vegetable");
    }

    @Test
    public void test_toString() throws Exception {
        assertEquals("Tomato", plant.toString());
    }

}