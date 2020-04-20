package com.google.samples.apps.sunflower.utilities

import kotlin.math.abs

fun getValami(param: Int) = when (param) {
    in 0..7 -> 13
    else -> 1 // Remaining latitudes are assigned to zone 1.
}
