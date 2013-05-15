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

package de.s2hmobile.tools.tabcompat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import de.s2hmobile.tools.R;
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

	/*
	 * The argument for createInstance() is a FragmentActivity. In this
	 * implementation, the argument is the same as in the sample library.
	 * Alternatively one could provide the parent or derived activities as an
	 * argument.
	 * Activity parent = ActionBarTabActivity.this.getParent();
	 * private mTabHelper = TabHelper.createInstance(parent);
	 */
	private TabHelper mTabHelper = TabHelper
			.createInstance(ActionBarTabActivity.this);

	// TODO test new implementation, remove old code
	// protected void setUpActionBarTabHelper(FragmentActivity activity,
	// boolean isHomeStateful) {
	// mTabHelper = TabHelper.createInstance(activity);
	// super.setUpActionBarHelper(activity, isHomeStateful);
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_compat);
	}

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

	public static class ActionBarTabListener<T extends Fragment> implements
			CompatTabListener {

		private final ActionBarTabActivity mActivity;
		private final Bundle mArgs;
		private final Class<T> mFragmentClass;
		private final int mContainerId;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 * @param args
		 *            Arguments for the fragment
		 */
		public ActionBarTabListener(ActionBarTabActivity activity,
				Class<T> clz, Bundle args) {
			mActivity = activity;
			mFragmentClass = clz;
			mArgs = args;
			mContainerId = android.R.id.tabcontent;
			// TODO android.R.id.tabcontent might be replaced with
			// Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
			// ? android.R.id.content // Honeycomb and above
			// : R.id.realtabcontent; // Froyo and Gingerbread
		}

		@Override
		public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			Fragment fragment = tab.getFragment();
			if (fragment == null) {
				// If not, instantiate and add it to the activity
				fragment = Fragment.instantiate(mActivity,
						mFragmentClass.getName(), mArgs);
				tab.setFragment(fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.add(mContainerId, fragment, tab.getTag());
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(fragment);
			}
		}

		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(fragment);
			}
		}

		@Override
		public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
		}
	}
}
