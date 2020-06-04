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

package com.google.samples.apps.sunflower

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.samples.apps.sunflower.adapters.MY_GARDEN_PAGE_INDEX
import com.google.samples.apps.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.google.samples.apps.sunflower.adapters.SunflowerPagerAdapter
import com.google.samples.apps.sunflower.databinding.FragmentViewPagerBinding


class HomeViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = SunflowerPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let { tabSelected(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let { tabUnselected(it) }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { tabSelected(it) }
            }
        })

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun tabSelected(tab: TabLayout.Tab){
        tab.icon = when(tab.position){
            MY_GARDEN_PAGE_INDEX -> getDrawable(R.drawable.vad_garden_active_anim)
            PLANT_LIST_PAGE_INDEX -> getDrawable(R.drawable.vad_plan_list_active)
            else -> throw IndexOutOfBoundsException()
        }

        animateVectorDrawable(tab.icon!!)
    }

    private fun tabUnselected(tab: TabLayout.Tab){
        tab.icon = when(tab.position){
            MY_GARDEN_PAGE_INDEX -> getDrawable(R.drawable.vd_garden_inactive)
            PLANT_LIST_PAGE_INDEX -> getDrawable(R.drawable.ic_plant_list_inactive)
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getDrawable(@DrawableRes id: Int): Drawable?{
        return ContextCompat.getDrawable(requireActivity(), id)
    }

    private fun animateVectorDrawable(icon: Drawable){

        when(icon){
            is AnimatedVectorDrawable -> {
                icon.start()
            }

            is AnimatedVectorDrawableCompat -> {
                icon.start()
            }
        }
    }

    private fun getTabIcon(position: Int): Drawable? {
        val id = when (position) {
            MY_GARDEN_PAGE_INDEX -> R.drawable.vad_garden_active_anim
            PLANT_LIST_PAGE_INDEX -> R.drawable.ic_plant_list_inactive
            else -> throw IndexOutOfBoundsException()
        }

        return ContextCompat.getDrawable(requireActivity(), id)
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            MY_GARDEN_PAGE_INDEX -> getString(R.string.my_garden_title)
            PLANT_LIST_PAGE_INDEX -> getString(R.string.plant_list_title)
            else -> null
        }
    }
}