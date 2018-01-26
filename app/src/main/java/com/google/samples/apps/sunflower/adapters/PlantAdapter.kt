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

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.samples.apps.sunflower.PlantDetailActivity
import com.google.samples.apps.sunflower.PlantDetailFragment
import com.google.samples.apps.sunflower.PlantListActivity
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant

/**
 * Adapter for the [RecyclerView] in [PlantListActivity].
 *
 * @param parentActivity the activity that is using this adapter
 * @param isTwoPane if a master/detail layout should be used
 */
class PlantAdapter(
    parentActivity: PlantListActivity,
    isTwoPane: Boolean
) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    var values: List<Plant> = ArrayList(0)
      set(items) {
          field = items
          notifyDataSetChanged()
      }

    private val onClickListener = View.OnClickListener { view ->
        val item = view.tag as Plant
        if (isTwoPane) {
            val fragment = PlantDetailFragment.newInstance(item.plantId)
            parentActivity.supportFragmentManager.beginTransaction()
                    .replace(R.id.plant_detail_container,fragment).commit()
        } else {
            val intent = Intent(view.context, PlantDetailActivity::class.java).apply {
                putExtra(PlantDetailFragment.ARG_ITEM_ID, item.plantId)
            }
            view.context.startActivity(intent)
        }

    }

    override fun getItemCount() = values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            idView.text = values[position].plantId
            contentView.text = values[position].name
            with(itemView) {
                tag = values[position]
                setOnClickListener(onClickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_plant, parent, false))
    }

    /**
     * Use this constructor to create a new ViewHolder.
     *
     * @param itemView - view to store in the ViewHolder
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idView: TextView = itemView.findViewById(R.id.id_text)
        val contentView: TextView = itemView.findViewById(R.id.content)
    }
}