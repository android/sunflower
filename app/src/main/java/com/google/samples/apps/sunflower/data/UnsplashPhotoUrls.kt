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

// TODO: comment out the ones that aren't needed
data class UnsplashPhotoUrls (
    @field:SerializedName("raw") val raw: String,
    @field:SerializedName("full") val full: String,
    @field:SerializedName("regular") val regular: String,
    @field:SerializedName("small") val small: String,
    @field:SerializedName("thumb") val thumb: String
)
