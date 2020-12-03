/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.compose.utils

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.ContextAmbient

/**
 * Load a string with grammatically correct pluralization for the given quantity,
 * using the given arguments.
 *
 * TODO: Remove when https://issuetracker.google.com/issues/158065051 is fixed
 *
 * @param id the resource identifier
 * @param quantity The number used to get the correct string for the current language's
 *           plural rules.
 *
 * @return the string data associated with the resource
 */
@Composable
fun getQuantityString(@PluralsRes id: Int, quantity: Int): String {
    val context = AmbientContext.current
    return context.resources.getQuantityString(id, quantity, quantity)
}
