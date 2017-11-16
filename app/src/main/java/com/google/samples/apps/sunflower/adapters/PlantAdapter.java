/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.samples.apps.sunflower.PlantDetailActivity;
import com.google.samples.apps.sunflower.PlantDetailFragment;
import com.google.samples.apps.sunflower.PlantListActivity;
import com.google.samples.apps.sunflower.R;
import com.google.samples.apps.sunflower.data.Plant;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private final PlantListActivity parentActivity;
    private final List<Plant> values;
    private final boolean isTwoPane;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Plant item = (Plant) view.getTag();
            if (isTwoPane) {
                PlantDetailFragment fragment = PlantDetailFragment.newInstance(item.getPlantId());
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.plant_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, PlantDetailActivity.class);
                intent.putExtra(PlantDetailFragment.ARG_ITEM_ID, item.getPlantId());

                context.startActivity(intent);
            }
        }
    };

    /**
     * Use this constructor to create a new Plant Adapter.
     *
     * @param parent         the activity that is using this adapter
     * @param items          a List of plants
     * @param isMasterDetail if a master/detail layout should be used
     */
    public PlantAdapter(@NonNull PlantListActivity parent,
                        @NonNull List<Plant> items,
                        boolean isMasterDetail) {
        values = items;
        parentActivity = parent;
        isTwoPane = isMasterDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_plant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idView.setText(values.get(position).getPlantId());
        holder.contentView.setText(values.get(position).getName());
        holder.itemView.setTag(values.get(position));
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView idView;
        final TextView contentView;

        /**
         * Use this constructor to create a new ViewHolder.
         *
         * @param view - view to store in the ViewHolder
         */
        ViewHolder(View view) {
            super(view);
            idView = view.findViewById(R.id.id_text);
            contentView = view.findViewById(R.id.content);
        }
    }
}
