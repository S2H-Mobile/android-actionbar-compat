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

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * An abstract base class that handles some common action bar-related
 * functionality in the app. This class provides functionality useful for both
 * phones and tablets, and does not require any Android 3.0-specific features,
 * although it uses them if available.
 * 
 * Two implementations of this class are {@link ActionBarHelperFroyo} for a
 * pre-Honeycomb version of the action bar, and {@link ActionBarHelperHoneycomb}
 * , which uses the built-in ActionBar features in Android 3.0 and later.
 */
public abstract class ActionBarHelper {

	protected final Activity mActivity;

	/** Stores the status of the home icon. */
	protected final boolean mHomeActive;

	/** Stores the resource id of the home icon. */
	protected final int mHomeResId;

	protected ActionBarHelper(Activity activity, boolean isHomeActive,
			int homeResId) {
		mActivity = activity;
		mHomeActive = isHomeActive;
		mHomeResId = homeResId;
	}

	/**
	 * Returns a {@link MenuInflater} for use when inflating menus. The
	 * implementation of this method in {@link ActionBarHelperFroyo} returns a
	 * wrapped menu inflater that can read action bar metadata from a menu
	 * resource pre-Honeycomb.
	 */
	public MenuInflater getMenuInflater(MenuInflater superMenuInflater) {
		return superMenuInflater;
	}

	/** Provides access to {@link Activity#invalidateOptionsMenu()}. */
	public void invalidateOptionsMenu() {
	}

	/**
	 * Action bar helper code to be run in
	 * {@link Activity#onCreate(android.os.Bundle)}.
	 */
	public void onCreate(Bundle savedInstanceState) {
	}

	/**
	 * Action bar helper code to be run in
	 * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * Action bar helper code to be run in
	 * {@link Activity#onPostCreate(android.os.Bundle)}.
	 */
	public void onPostCreate(Bundle savedInstanceState) {
	}

	/**
	 * Action bar helper code to be run in
	 * {@link Activity#onTitleChanged(CharSequence, int)}.
	 */
	public void onTitleChanged(CharSequence title, int color) {
	}

	/**
	 * Sets the indeterminate loading state of the item identified by
	 * <code>R.id.menu_refresh</code>.
	 */
	public abstract void setRefreshActionItemState(boolean isRefreshing);

	/**
	 * Factory method for creating {@link ActionBarHelper} objects for a given
	 * activity. Depending on the type of activity and on the device the app is
	 * running on, a specific helper will be returned.
	 * 
	 * @param activity
	 *            the calling activity
	 * @param isHomeActive
	 *            flag to set the status of the home icon
	 * @return a version specific {@link ActionBarHelper}
	 */
	public static ActionBarHelper createInstance(Activity activity,
			final boolean isHomeActive, final int homeResId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return new ActionBarHelperICS(activity, isHomeActive, homeResId);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new ActionBarHelperHoneycomb(activity, isHomeActive,
					homeResId);
		} else {
			return new ActionBarHelperFroyo(activity, isHomeActive, homeResId);
		}
	}
}
