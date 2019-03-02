/*
 * Copyright 2018 Google LLC
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

package com.google.samples.apps.sunflower.adapters

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.bold
import androidx.core.text.italic
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.samples.apps.sunflower.R

@BindingAdapter("imageFromUrl")
fun ImageView.bindImageFromUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
    }
}

@BindingAdapter("isGone")
fun FloatingActionButton.bindIsGone(isGone: Boolean?) {
    if (isGone == null || isGone) {
        hide()
    } else {
        show()
    }
}

@BindingAdapter("renderHtml")
fun TextView.bindRenderHtml(description: String?) {
    if (description != null) {
        text = HtmlCompat.fromHtml(description, FROM_HTML_MODE_COMPACT)
        movementMethod = LinkMovementMethod.getInstance()
    } else {
        text = ""
    }
}

@BindingAdapter("wateringText")
fun TextView.bindWateringText(wateringInterval: Int) {
    val resources = context.resources
    val quantityString = resources.getQuantityString(R.plurals.watering_needs_suffix,
        wateringInterval, wateringInterval)

    text = SpannableStringBuilder()
        .bold { append(resources.getString(R.string.watering_needs_prefix)) }
        .append(" ")
        .italic { append(quantityString) }
}