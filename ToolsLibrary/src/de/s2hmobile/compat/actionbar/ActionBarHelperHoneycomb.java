/* File modified by S2H Mobile.
 * 
 * Copyright 2011 The Android Open Source Project
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

package de.s2hmobile.compat.actionbar;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.s2hmobile.compat.R;

/**
 * An extension of {@link ActionBarHelper} that provides Android 3.0-specific
 * functionality for Honeycomb tablets. It thus requires API level 11.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ActionBarHelperHoneycomb extends ActionBarHelper {

	/** Stores the refresh progress view. */
	private View mIndeterminateProgressView = null;

	private Menu mOptionsMenu = null;

	protected ActionBarHelperHoneycomb(Activity activity, boolean isHomeStateful) {
		super(activity, isHomeStateful);
	}

	@Override
	public void invalidateOptionsMenu() {
		mActivity.invalidateOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mOptionsMenu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Honeycomb specific implementation of
	 * {@link Activity#onPostCreate(android.os.Bundle)}. Sets the state of the
	 * home item as defined in the {@link ActionBarConfigurator}.
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		final ActionBar actionBar = mActivity.getActionBar();
		if (actionBar != null) {

			// enable home icon to behave as action item
			actionBar.setDisplayHomeAsUpEnabled(mHomeActive);
		}
	}

	/**
	 * Sets the state of the refresh button by giving it a custom action view.
	 */
	@Override
	public void setRefreshActionItemState(final boolean refreshing) {
		if (mOptionsMenu != null) {
			final MenuItem refreshItem = mOptionsMenu
					.findItem(R.id.menu_refresh);
			if (refreshItem != null) {
				refreshItem.setActionView(getRefreshProgressView(refreshing));
			}
		}
	}

	/**
	 * Returns a {@link Context} suitable for inflating layouts for the action
	 * bar.
	 */
	protected Context getActionBarThemedContext() {
		return mActivity;
	}

	private View getRefreshProgressView(final boolean isRefreshing) {
		if (isRefreshing) {
			if (mIndeterminateProgressView == null) {

				// inflate the progress bar from XML file in layout-v11
				final Context themedContext = getActionBarThemedContext();
				final LayoutInflater inflater = (LayoutInflater) themedContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mIndeterminateProgressView = inflater.inflate(
						R.layout.actionbar_indeterminate_progress, null);
			}
			return mIndeterminateProgressView;
		} else {
			return null;
		}
	}
}