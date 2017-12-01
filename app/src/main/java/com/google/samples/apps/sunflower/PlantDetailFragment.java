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

package com.google.samples.apps.sunflower;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.apps.sunflower.data.Plant;
import com.google.samples.apps.sunflower.databinding.FragmentPlantDetailBinding;
import com.google.samples.apps.sunflower.utilities.InjectorUtils;
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel;
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModelFactory;

/**
 * A fragment representing a single {@link Plant} detail screen.
 * This fragment is either contained in a {@link PlantListActivity}
 * in two-pane mode (on tablets) or a {@link PlantDetailActivity}
 * on handsets.
 */
public class PlantDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Data binding for this fragment
     */
    private FragmentPlantDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlantDetailFragment() {
    }

    /**
     * Create a new instance of PlantDetailFragment, initialized with a plant ID.
     */
    public static PlantDetailFragment newInstance(String plantId) {
        PlantDetailFragment fragment = new PlantDetailFragment();

        // Supply plant ID as an argument.
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_ID, plantId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String plantId = getArguments().getString(ARG_ITEM_ID);

        PlantDetailViewModelFactory factory = InjectorUtils.providePlantDetailViewModelFactory(
                getActivity().getApplication(), plantId);
        PlantDetailViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(PlantDetailViewModel.class);
        subscribeToModel(viewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_plant_detail, container,false);

        return binding.getRoot();
    }

    private void subscribeToModel(PlantDetailViewModel viewModel) {
        viewModel.getPlant().observe(this, this::updateUi);
    }

    private void updateUi(Plant plant) {
        if (plant == null) {
            return;
        }

        binding.plantDetail.setText(plant.getDescription());

        CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(plant.getName());
        }
    }
}
