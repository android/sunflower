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

package com.google.samples.apps.sunflower.data

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents URLs available for a Unsplash photo.
 *
 * Although several photo sizes are available, this project uses only uses the `small` sized photo.
 * For more details, consult the API documentation
 * [here](https://unsplash.com/documentation#example-image-use).
 */
data class UnsplashPhotoUrls(
    @field:SerializedName("small") val small: String
)
