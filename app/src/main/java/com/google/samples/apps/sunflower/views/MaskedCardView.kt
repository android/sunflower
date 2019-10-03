/*
 * Copyright 2019 Google LLC
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

package com.google.samples.apps.sunflower.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapeAppearancePathProvider
import android.annotation.SuppressLint
import android.graphics.Path
import android.graphics.RectF

/**
 * A Card view that clips the content of any shape, this should be done upstream in card,
 * working around it for now.
 */
class MaskedCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyle) {
    @SuppressLint("RestrictedApi")
    private val pathProvider = ShapeAppearancePathProvider()
    private val path: Path = Path()
    private val shapeAppearance: ShapeAppearanceModel = ShapeAppearanceModel(
        context,
        attrs,
        defStyle,
        R.style.Widget_MaterialComponents_CardView
    )
    private val rectF = RectF(0f, 0f, 0f, 0f)

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    @SuppressLint("RestrictedApi")
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rectF.right = w.toFloat()
        rectF.bottom = h.toFloat()
        pathProvider.calculatePath(shapeAppearance, 1f, rectF, path)
        super.onSizeChanged(w, h, oldw, oldh)
    }
}