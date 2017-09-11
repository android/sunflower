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

    private final PlantListActivity mParentActivity;
    private final List<Plant> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Plant item = (Plant) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(PlantDetailFragment.ARG_ITEM_ID, item.id);
                PlantDetailFragment fragment = new PlantDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.plant_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, PlantDetailActivity.class);
                intent.putExtra(PlantDetailFragment.ARG_ITEM_ID, item.id);

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
        mValues = items;
        mParentActivity = parent;
        mTwoPane = isMasterDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_plant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).name);
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mIdView;
        final TextView mContentView;

        /**
         * Use this constructor to create a new ViewHolder.
         *
         * @param view - view to store in the ViewHolder
         */
        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }
    }
}
