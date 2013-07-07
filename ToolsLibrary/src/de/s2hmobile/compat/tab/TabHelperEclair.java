/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.s2hmobile.compat.tab;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

/**
 * This is a helper class to build tabs on pre-Honeycomb. Call
 * {@link TabActivityBase#getTabHelper()} to get the generic instance for
 * compatibility with other versions.
 * 
 * It implements a generic mechanism for associating fragments with the tabs in
 * a tab host. It relies on a trick: Normally a tab host has a simple API for
 * supplying a View or Intent that each tab will show. This is not sufficient
 * for switching between fragments. So instead we make the content part of the
 * tab host 0dp high (it is not shown) and this supplies its own dummy view to
 * show as the tab content. It listens to changes in tabs, then passes the event
 * back to the tab's callback interface so the activity can take care of
 * switching to the correct fragment.
 */
public class TabHelperEclair extends TabHelper implements
		TabHost.OnTabChangeListener {

	/**
	 * Creates empty views as dummy content for the {@link TabSpec} part of the
	 * {@link TabHost}.
	 */
	static class DummyTabFactory implements TabHost.TabContentFactory {

		private final Context mContext;

		DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			final View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	/**
	 * Key for saving the tag of the currently selected tab in the instance
	 * state {@link Bundle}.
	 */
	private static final String KEY_CURRENT_TAB = "tag_current_tab";

	// CompatTabListener mCallback;
	private CompatTab mLastTab = null;

	// TODO almost all methods rely on setUp() been called first
	private TabHost mTabHost = null;

	private final HashMap<String, CompatTab> mTabs = new HashMap<String, CompatTab>();

	protected TabHelperEclair(FragmentActivity activity) {
		super(activity);
	}

	/**
	 * Converts the data from a {@link CompatTab} to a {@link TabSpec} object.
	 */
	@Override
	public void addTab(CompatTab tab) {

		// get the data
		final String tag = tab.getTag();
		final Drawable icon = tab.getIcon();
		final CharSequence label = tab.getText();

		// specify the properties of the tab
		final TabSpec spec = mTabHost.newTabSpec(tag);

		// set icon and label
		if (icon != null) {
			spec.setIndicator(label, icon);
		} else {
			spec.setIndicator(label);
		}

		// we have to provide tab content
		spec.setContent(new DummyTabFactory(mActivity));

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.

		final Fragment fragment = mActivity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		tab.setFragment(fragment);

		if (fragment != null && !fragment.isDetached()) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
//			TODO remove log statement
			android.util.Log.i("TabHelperEclair", "detaching fragment "
					+ fragment.getTag());

			ft.detach(fragment);
			ft.commit();
		}

		// put the new entry to the tab list
		mTabs.put(tag, tab);
		
		// add the tab to the host
		mTabHost.addTab(spec);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState
					.getString(KEY_CURRENT_TAB));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_CURRENT_TAB, mTabHost.getCurrentTabTag());
	}

	/**
	 * Converts the basic "tab changed" event for TabWidget into the three
	 * possible events for CompatTabListener: selected, unselected, reselected.
	 */
	@Override
	public void onTabChanged(String tabId) {
		
//		TODO
		CompatTab newTab = mTabs.get(tabId);
		FragmentTransaction ft = mActivity.getSupportFragmentManager()
				.beginTransaction();

		if (mLastTab != newTab) {
			if (mLastTab != null) {
				if (mLastTab.getFragment() != null) {
					// Pass the unselected event back to the tab's
					// CompatTabListener
					mLastTab.getCallback().onTabUnselected(mLastTab, ft);
				}
			}
			if (newTab != null) {
				// Pass the selected event back to the tab's CompatTabListener
				newTab.getCallback().onTabSelected(newTab, ft);
			}

			mLastTab = newTab;
		} else {
			// Pass the re-selected event back to the tab's CompatTabListener
			newTab.getCallback().onTabReselected(newTab, ft);
		}

		ft.commit();
		mActivity.getSupportFragmentManager().executePendingTransactions();
	}

	/**
	 * ViewPager swipe has changed the fragment, activate the appropriate new
	 * tab at new position
	 */
	@Override
	public void setSelectedTab(int position) {
//		TODO taken from FragmentTabsPager in support v4 demos
		
		// Unfortunately when TabHost changes the current tab, it kindly
        // also takes care of putting focus on it when not in touch mode.
        // The jerk.
        // This hack tries to prevent this from pulling focus out of our
        // ViewPager.
        final TabWidget widget = mTabHost.getTabWidget();
        final int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        
        
        mTabHost.setCurrentTab(position);
        
        widget.setDescendantFocusability(oldFocusability);
		
		
	}

	@Override
	public void setUp() {
		if (mTabHost == null) {
			mTabHost = (TabHost) mActivity.findViewById(android.R.id.tabhost);
			mTabHost.setup();
			mTabHost.setOnTabChangedListener(TabHelperEclair.this);
		}
	}
}
