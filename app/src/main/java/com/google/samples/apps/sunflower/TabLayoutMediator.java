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

package com.google.samples.apps.sunflower;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE;
import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
/**
 * A mediator to link a TabLayout with a ViewPager2. The mediator will synchronize the ViewPager2's
 * position with the selected tab when a tab is selected, and the TabLayout's scroll position when
 * the user drags the ViewPager2.
 *
 * Establish the link by creating an instance of this class, make sure the ViewPager2 has an adapter
 * and then call {@link #attach()} on it. When creating an instance of this class, you must supply
 * an implementation of {@link OnConfigureTabCallback} in which you set the text of the tab, and/or
 * perform any styling of the tabs that you require.
 *
 * This file was taken from the ViewPager2 sample: https://github.com/googlesamples/android-viewpager2.
 */
@RestrictTo(LIBRARY_GROUP)
public final class TabLayoutMediator {
    private final @NonNull TabLayout mTabLayout;
    private final @NonNull ViewPager2 mViewPager;
    private final boolean mAutoRefresh;
    private final OnConfigureTabCallback mOnConfigureTabCallback;
    private RecyclerView.Adapter mAdapter;
    private boolean mAttached;

    private TabLayoutOnPageChangeCallback mOnPageChangeCallback;
    private TabLayout.OnTabSelectedListener mOnTabSelectedListener;
    private RecyclerView.AdapterDataObserver mPagerAdapterObserver;

    /**
     * A callback interface that must be implemented to set the text and styling of newly created
     * tabs.
     */
    public interface OnConfigureTabCallback {
        /**
         * Called to configure the tab for the page at the specified position. Typically calls
         * {@link TabLayout.Tab#setText(CharSequence)}, but any form of styling can be applied.
         *
         * @param tab The Tab which should be configured to represent the title of the item at the
         *        given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        void onConfigureTab(@NonNull TabLayout.Tab tab, int position);
    }

    /**
     * Creates a TabLayoutMediator to synchronize a TabLayout and a ViewPager2 together. It will
     * update the tabs automatically when the data set of the view pager's adapter changes. The link
     * will be established after {@link #attach()} is called.
     *
     * @param tabLayout The tab bar to link
     * @param viewPager The view pager to link
     */
    public TabLayoutMediator(@NonNull TabLayout tabLayout, @NonNull ViewPager2 viewPager,
                             @NonNull OnConfigureTabCallback onConfigureTabCallback) {
        this(tabLayout, viewPager, true, onConfigureTabCallback);
    }

    /**
     * Creates a TabLayoutMediator to synchronize a TabLayout and a ViewPager2 together. If {@code
     * autoRefresh} is true, it will update the tabs automatically when the data set of the view
     * pager's adapter changes. The link will be established after {@link #attach()} is called.
     *
     * @param tabLayout The tab bar to link
     * @param viewPager The view pager to link
     * @param autoRefresh If {@code true}, will recreate all tabs when the data set of the view
     *                   pager's adapter changes.
     */
    public TabLayoutMediator(@NonNull TabLayout tabLayout, @NonNull ViewPager2 viewPager,
                             boolean autoRefresh, @NonNull OnConfigureTabCallback onConfigureTabCallback) {
        mTabLayout = tabLayout;
        mViewPager = viewPager;
        mAutoRefresh = autoRefresh;
        mOnConfigureTabCallback = onConfigureTabCallback;
    }

    /**
     * Link the TabLayout and the ViewPager2 together.
     * @throws IllegalStateException If the mediator is already attached, or the ViewPager2 has no
     *         adapter.
     */
    public void attach() {
        if (mAttached) {
            throw new IllegalStateException("TabLayoutMediator is already attached");
        }
        mAdapter = mViewPager.getAdapter();
        if (mAdapter == null) {
            throw new IllegalStateException("TabLayoutMediator attached before ViewPager2 has an "
                    + "adapter");
        }
        mAttached = true;

        // Add our custom OnPageChangeCallback to the ViewPager
        mOnPageChangeCallback = new TabLayoutOnPageChangeCallback(mTabLayout);
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback);

        // Now we'll add a tab selected listener to set ViewPager's current item
        mOnTabSelectedListener = new ViewPagerOnTabSelectedListener(mViewPager);
        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListener);

        // Now we'll populate ourselves from the pager adapter, adding an observer if
        // autoRefresh is enabled
        if (mAutoRefresh) {
            // Register our observer on the new adapter
            mPagerAdapterObserver = new PagerAdapterObserver();
            mAdapter.registerAdapterDataObserver(mPagerAdapterObserver);
        }

        populateTabsFromPagerAdapter();

        // Now update the scroll position to match the ViewPager's current item
        mTabLayout.setScrollPosition(mViewPager.getCurrentItem(), 0f, true);
    }

    /**
     * Unlink the TabLayout and the ViewPager
     */
    public void detach() {
        mAdapter.unregisterAdapterDataObserver(mPagerAdapterObserver);
        mTabLayout.removeOnTabSelectedListener(mOnTabSelectedListener);
        mViewPager.unregisterOnPageChangeCallback(mOnPageChangeCallback);
        mPagerAdapterObserver = null;
        mOnTabSelectedListener = null;
        mOnPageChangeCallback = null;
        mAttached = false;
    }

    @SuppressWarnings("WeakerAccess")
    void populateTabsFromPagerAdapter() {
        mTabLayout.removeAllTabs();

        if (mAdapter != null) {
            int adapterCount = mAdapter.getItemCount();
            for (int i = 0; i < adapterCount; i++) {
                TabLayout.Tab tab = mTabLayout.newTab();
                mOnConfigureTabCallback.onConfigureTab(tab, i);
                mTabLayout.addTab(tab, false);
            }

            // Make sure we reflect the currently set ViewPager item
            if (adapterCount > 0) {
                int currItem = mViewPager.getCurrentItem();
                if (currItem != mTabLayout.getSelectedTabPosition()) {
                    mTabLayout.getTabAt(currItem).select();
                }
            }
        }
    }

    /**
     * A {@link ViewPager2.OnPageChangeCallback} class which contains the necessary calls back to
     * the provided {@link TabLayout} so that the tab position is kept in sync.
     *
     * <p>This class stores the provided TabLayout weakly, meaning that you can use {@link
     * ViewPager2#registerOnPageChangeCallback(ViewPager2.OnPageChangeCallback)} without removing
     * the callback and not cause a leak.
     */
    private static class TabLayoutOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private final WeakReference<TabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;

        TabLayoutOnPageChangeCallback(TabLayout tabLayout) {
            mTabLayoutRef = new WeakReference<>(tabLayout);
            reset();
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                boolean updateText = mScrollState != SCROLL_STATE_SETTLING
                        || mPreviousScrollState == SCROLL_STATE_DRAGGING;
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                boolean updateIndicator = !(mScrollState == SCROLL_STATE_SETTLING
                        && mPreviousScrollState == SCROLL_STATE_IDLE);
                setScrollPosition(tabLayout, position, positionOffset, updateText, updateIndicator);
            }
        }

        @Override
        public void onPageSelected(final int position) {
            TabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null
                    && tabLayout.getSelectedTabPosition() != position
                    && position < tabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                boolean updateIndicator = mScrollState == SCROLL_STATE_IDLE
                        || (mScrollState == SCROLL_STATE_SETTLING
                        && mPreviousScrollState == SCROLL_STATE_IDLE);
                selectTab(tabLayout, tabLayout.getTabAt(position), updateIndicator);
            }
        }

        void reset() {
            mPreviousScrollState = mScrollState = SCROLL_STATE_IDLE;
        }
    }

    // region Reflective calls

    // Temporarily call methods TabLayout.setScrollPosition(int, float, boolean, boolean) and
    // TabLayout.selectTab(TabLayout.Tab, boolean) through reflection, until they have been made
    // public in the Material Design Components library.

    private static Method sSetScrollPosition;
    private static Method sSelectTab;
    private static final String SET_SCROLL_POSITION_NAME = "TabLayout.setScrollPosition(int, float,"
            + " boolean, boolean)";
    private static final String SELECT_TAB_NAME = "TabLayout.selectTab(TabLayout.Tab, boolean)";

    static {
        try {
            sSetScrollPosition = TabLayout.class.getDeclaredMethod("setScrollPosition", int.class,
                    float.class, boolean.class, boolean.class);
            sSetScrollPosition.setAccessible(true);

            sSelectTab = TabLayout.class.getDeclaredMethod("selectTab", TabLayout.Tab.class,
                    boolean.class);
            sSelectTab.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Can't reflect into method TabLayout"
                    + ".setScrollPosition(int, float, boolean, boolean)");
        }
    }

    @SuppressWarnings("WeakerAccess")
    static void setScrollPosition(TabLayout tabLayout, int position, float positionOffset,
                                  boolean updateSelectedText, boolean updateIndicatorPosition) {
        try {
            if (sSetScrollPosition != null) {
                sSetScrollPosition.invoke(tabLayout, position, positionOffset, updateSelectedText,
                        updateIndicatorPosition);
            } else {
                throwMethodNotFound(SET_SCROLL_POSITION_NAME);
            }
        } catch (Exception e) {
            throwInvokeFailed(SET_SCROLL_POSITION_NAME);
        }
    }

    @SuppressWarnings("WeakerAccess")
    static void selectTab(TabLayout tabLayout, TabLayout.Tab tab, boolean updateIndicator) {
        try {
            if (sSelectTab != null) {
                sSelectTab.invoke(tabLayout, tab, updateIndicator);
            } else {
                throwMethodNotFound(SELECT_TAB_NAME);
            }
        } catch (Exception e) {
            throwInvokeFailed(SELECT_TAB_NAME);
        }
    }

    private static void throwMethodNotFound(String method) {
        throw new IllegalStateException("Method " + method + " not found");
    }

    private static void throwInvokeFailed(String method) {
        throw new IllegalStateException("Couldn't invoke method " + method);
    }

    // endregion

    /**
     * A {@link TabLayout.OnTabSelectedListener} class which contains the necessary calls back to
     * the provided {@link ViewPager2} so that the tab position is kept in sync.
     */
    private static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        private final ViewPager2 mViewPager;

        ViewPagerOnTabSelectedListener(ViewPager2 viewPager) {
            this.mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // No-op
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // No-op
        }
    }

    private class PagerAdapterObserver extends RecyclerView.AdapterDataObserver {
        PagerAdapterObserver() {}

        @Override
        public void onChanged() {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            populateTabsFromPagerAdapter();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            populateTabsFromPagerAdapter();
        }
    }
}