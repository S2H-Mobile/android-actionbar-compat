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
import de.s2hmobile.compat.tab.TabHelper;

/**
 * A base activity that defers tab functionality to a {@link TabHelper}.
 * 
 * When building an activity with tabs, extend this class in order to provide
 * compatibility with API level 5 and above. Using this class along with the
 * {@link TabHelper} and {@link com.example.android.tabcompat.lib.CompatTab}
 * classes, you can build a tab UI that's built using the
 * {@link android.app.ActionBar} on Honeycomb+ and the
 * {@link android.widget.TabWidget} on all older versions.
 * 
 * The {@link TabHelper} APIs obfuscate all the compatibility work for you.
 */
public abstract class TabActivityBase extends ActionBarFragmentActivity {

	/**
	 * In this implementation, the argument for the
	 * {@link TabHelper#createInstance(android.support.v4.app.FragmentActivity)}
	 * is the same as in the sample library. Alternatively we could provide the
	 * parent or derived activities.
	 * <p>
	 * 
	 * <code>FragmentActivity parent = TabActivityBase.this.getParent();</code>
	 * 
	 * <code>private mTabHelper = TabHelper.createInstance(parent);</code>
	 */
	private final TabHelper mTabHelper = TabHelper
			.createInstance(TabActivityBase.this);

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