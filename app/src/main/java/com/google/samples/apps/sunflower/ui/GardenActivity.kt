/*
 * Copyright 2021 Google LLC
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

package com.google.samples.apps.sunflower.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.databinding.ActivityGardenBinding
import dagger.hilt.android.AndroidEntryPoint

// 声明Activity注入入口点
// Activity、Fragment、View、Service、BroadcastReceiver的入口点
// 声明都是AndroidEntryPoint注解
@AndroidEntryPoint
class GardenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityGardenBinding>(this, R.layout.activity_garden)
        Log.i(TAG, "启动GardenActivity")
    }

    companion object {
        private const val TAG = "GardenActivity"
    }
}
