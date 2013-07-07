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

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * Helper class to build tabs on Honeycomb. Call
 * {@link TabActivityBase#getTabHelper()} to get the generic instance for
 * compatibility with older versions.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TabHelperHoneycomb extends TabHelper {

	/**
	 * Key for saving the position of the currently selected tab in the instance
	 * state {@link Bundle}.
	 */
	private static final String KEY_CURRENT_TAB = "position_current_tab";

	private ActionBar mActionBar = null;

	protected TabHelperHoneycomb(FragmentActivity activity) {
		super(activity);
	}

	@Override
	public void addTab(CompatTab tab) {

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		final Fragment fragment = mActivity.getSupportFragmentManager()
				.findFragmentByTag(tab.getTag());
		tab.setFragment(fragment);

		if (fragment != null && !fragment.isDetached()) {
			// TODO remove log statement
			android.util.Log.i("TabHelperHoneycomb", "addTab() detaches fragment");
			final FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(fragment);
			ft.commit();
		}

		if (tab.getCallback() == null) {
			throw new IllegalStateException(
					"CompatTab must have a CompatTabListener.");
		}

		// We know tab is a CompatTabHoneycomb instance, so its
		// native tab object is an ActionBar.Tab.
		mActionBar.addTab((ActionBar.Tab) tab.getTab());
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		final int position = savedInstanceState.getInt(KEY_CURRENT_TAB);
		mActionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		final int position = mActionBar.getSelectedTab().getPosition();
		outState.putInt(KEY_CURRENT_TAB, position);
	}

	@Override
	public void setSelectedTab(int position) {
		mActionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void setUp() {
		if (mActionBar == null) {
			mActionBar = mActivity.getActionBar();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		}
	}
}