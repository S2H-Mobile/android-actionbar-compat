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

package de.s2hmobile.tools.tabcompat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.s2hmobile.tools.actionbarcompat.ActionBarFragmentActivity;

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
public abstract class ActionBarTabActivity extends ActionBarFragmentActivity {
	private TabHelper mTabHelper = null;

	protected void setUpTabHelper(FragmentActivity activity,
			boolean isHomeStateful) {
		// mTabHelper = TabHelper.createInstance(ActionBarTabActivity.this);
		mTabHelper = TabHelper.createInstance(activity);
		super.setUpActionBarHelper(activity, isHomeStateful);
	}

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	//
	// /* The argument for createInstance() is a FragmentActivity. In this
	// * implementation, the argument is the same as in the sample library.
	// * An alternative implementation is
	// * Activity parent = ActionBarTabActivity.this.getParent();
	// * mTabHelper = TabHelper.createInstance(parent);
	// */
	// mTabHelper = TabHelper.createInstance(ActionBarTabActivity.this);
	// }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mTabHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mTabHelper.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Returns the {@link TabHelper} for this activity.
	 */
	protected TabHelper getTabHelper() {
		mTabHelper.setUp();
		return mTabHelper;
	}
}
