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

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.launchInComposition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.samples.apps.sunflower.compose.systemBarsPadding
import kotlinx.coroutines.delay

/**
 * Simple API to display a Snackbar with text on the screen
 */
@Composable
fun TextSnackbarContainer(
    snackbarText: String,
    showSnackbar: Boolean,
    onDismissSnackbar: () -> Unit,
    modifier: Modifier = Modifier,
    dismissTimeoutMs: Long = 5000,
    content: @Composable () -> Unit
) {
    Box(modifier) {
        content()
        if (showSnackbar) {
            launchInComposition(showSnackbar) {
                delay(dismissTimeoutMs)
                onDismissSnackbar()
            }

            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .systemBarsPadding()
                    .padding(all = 8.dp),
                text = { Text(snackbarText) },
                shape = RoundedCornerShape(4.dp)
            )
        }
    }
}
