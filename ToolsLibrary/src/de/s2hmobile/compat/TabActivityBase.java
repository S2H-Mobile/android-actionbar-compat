/* Modified by S2H Mobile, 2013.
 * 
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

package de.s2hmobile.compat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.s2hmobile.compat.tab.CompatTab;
import de.s2hmobile.compat.tab.TabHelper;

/**
 * A base activity that defers tab functionality to a {@link TabHelper}. It
 * obfuscates all the compatibility work for you.
 */
abstract class TabActivityBase extends ActionBarFragmentActivity {

	/**
	 * In this implementation, the argument for the
	 * {@link TabHelper#createInstance(android.support.v4.app.FragmentActivity)}
	 * is the same as in the sample library. Alternatively we could provide the
	 * parent of this class or one of its derived activities.
	 * <p>
	 * 
	 * <code>FragmentActivity parent = TabActivityBase.this.getParent();</code>
	 * 
	 * <code>private mTabHelper = TabHelper.createInstance(parent);</code>
	 */
	private final TabHelper mTabHelper = TabHelper
			.createInstance(getActivity());

	/**
	 * Convenience method to create a {@link CompatTab}.
	 * 
	 * @param tag
	 *            - name of the fragment
	 * @param position
	 *            - position of the fragment within the pager
	 * @return The {@link CompatTab} object.
	 */
	protected CompatTab createTab(String tag, int position) {
		return CompatTab.newTab(getActivity(), tag, position);
	}

	/**
	 * Get an instance of this activity. The factory methods of
	 * {@link TabHelper} and the {@link CompatTab} need to use the same instance
	 * of {@link FragmentActivity}.
	 * 
	 * @return This activity.
	 */
	protected abstract FragmentActivity getActivity();

	/** @return The {@link TabHelper} for this activity. */
	protected TabHelper getTabHelper() {
		mTabHelper.setUp();
		return mTabHelper;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mTabHelper.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mTabHelper.onSaveInstanceState(outState);
	}
}