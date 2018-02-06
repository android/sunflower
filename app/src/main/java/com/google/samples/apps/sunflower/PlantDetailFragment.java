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

package com.google.samples.apps.sunflower;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        String plantId = getArguments().getString(ARG_ITEM_ID);

        PlantDetailViewModelFactory factory = InjectorUtils.providePlantDetailViewModelFactory(
                getActivity().getApplication(), plantId);
        PlantDetailViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(PlantDetailViewModel.class);

        FragmentPlantDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_plant_detail, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

}
