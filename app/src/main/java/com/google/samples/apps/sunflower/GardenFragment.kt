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

package com.google.samples.apps.sunflower

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.samples.apps.sunflower.adapters.GardenPlantingAdapter
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModel

class GardenFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_garden, container, false)
        val adapter = GardenPlantingAdapter(view.context)
        view.findViewById<RecyclerView>(R.id.garden_list).adapter = adapter
        subscribeUi(adapter)
        return view
    }

    private fun subscribeUi(adapter: GardenPlantingAdapter) {
        val factory = InjectorUtils.provideGardenPlantingListViewModelFactory(requireContext())
        val viewModel = ViewModelProviders.of(this, factory)
                .get(GardenPlantingListViewModel::class.java)

        viewModel.getGardenPlantings().observe(this, Observer { plantings ->
            if (plantings != null && plantings.isNotEmpty()) {
                activity?.run {
                    findViewById<RecyclerView>(R.id.garden_list).run { visibility = View.VISIBLE }
                    findViewById<TextView>(R.id.empty_garden).run { visibility = View.GONE }
                }
            } else {
                activity?.run {
                    findViewById<RecyclerView>(R.id.garden_list).run { visibility = View.GONE }
                    findViewById<TextView>(R.id.empty_garden).run { visibility = View.VISIBLE }
                }
            }
        })

        viewModel.getPlantAndGardenPlantings().observe(this, Observer { result ->
            if (result != null && result.isNotEmpty())
                adapter.values = result
        })
    }
}
