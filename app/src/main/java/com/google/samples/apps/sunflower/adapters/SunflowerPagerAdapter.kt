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

package com.google.samples.apps.sunflower.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.samples.apps.sunflower.GardenFragment
import com.google.samples.apps.sunflower.PlantListFragment

const val MY_GARDEN_PAGE_INDEX = 0
const val PLANT_LIST_PAGE_INDEX = 1

class SunflowerPagerAdapter(
    private val tabTitles: List<String>,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    /**
     * There are only two fragments in this ViewPager: [GardenFragment] and [PlantListFragment]
     */
    override fun getCount() = 2

    override fun getItem(position: Int): Fragment {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> GardenFragment()
            PLANT_LIST_PAGE_INDEX -> PlantListFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> tabTitles[MY_GARDEN_PAGE_INDEX]
            PLANT_LIST_PAGE_INDEX -> tabTitles[PLANT_LIST_PAGE_INDEX]
            else -> null
        }
    }
}