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

/**
 * An extension of {@link ActionBarHelper} that provides Android 4.0-specific
 * functionality for IceCreamSandwich devices. It thus requires API level 14.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActionBarHelperICS extends ActionBarHelperHoneycomb {

	protected ActionBarHelperICS(final Activity activity,
			final boolean isHomeStateful, final int homeResId) {
		super(activity, isHomeStateful, homeResId);
	}

	/**
	 * IceCreamSandwich specific implementation of
	 * {@link Activity#onPostCreate(android.os.Bundle)}. Sets the state of the
	 * home item as defined in the {@link ActionBarConfigurator}.
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		final ActionBar actionBar = mActivity.getActionBar();
		if (actionBar != null) {

			// enable home icon to behave as action item
			actionBar.setDisplayHomeAsUpEnabled(mHomeActive);
			actionBar.setHomeButtonEnabled(mHomeActive);
		}
	}

	/**
	 * Returns a themed {@link Context} suitable for inflating layouts for the
	 * action bar.
	 */
	@Override
	protected Context getActionBarThemedContext() {
		return mActivity.getActionBar().getThemedContext();
	}
}