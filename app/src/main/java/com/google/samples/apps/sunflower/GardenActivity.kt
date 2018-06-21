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

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.samples.apps.sunflower.databinding.ActivityGardenBinding

class GardenActivity : AppCompatActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityGardenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityGardenBinding>(this, R.layout.activity_garden)
            .apply {
                binding = this
                setupToolbar()
                setupNavigationDrawer()
            }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setupNavigationDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close
        ).also {
            binding.drawerLayout.addDrawerListener(it)
            it.isDrawerSlideAnimationEnabled = false
            binding.drawerLayout.addDrawerListener(it)
        }

        val navController = Navigation.findNavController(this, R.id.garden_nav_fragment)
        binding.navigationView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        // [ActionBarDrawerToggle] will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * If [ActionBarDrawerToggle] is used, it must be called in [onPostCreate] and
     * [onConfigurationChanged].
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after has occurred.
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        // Pass any configuration change to the drawer toggle.
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        with(binding.drawerLayout) {
            val isDrawerOpen = isDrawerOpen(GravityCompat.START)
            if (isDrawerOpen) closeDrawer(GravityCompat.START)
            else super.onBackPressed()
        }
    }
}