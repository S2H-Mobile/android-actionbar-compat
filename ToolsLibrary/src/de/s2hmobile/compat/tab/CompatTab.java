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

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Represents a single tab. The {@link TabHelper} initializes one of the
 * subclasses of this based on the current platform version, upon call to
 * {@link TabHelper#newTab(String)}()
 */
public abstract class CompatTab {
	final FragmentActivity mActivity;
	final String mTag;
	final int mPosition;

	protected CompatTab(FragmentActivity activity, String tag, int position) {
		mActivity = activity;
		mTag = tag;
		mPosition = position;
	}

	/**
	 * Create a new tab.
	 * 
	 * @param tag
	 *            A unique tag to associate with the tab and associated fragment
	 * @return CompatTab for the appropriate android version
	 */
	public static CompatTab newTab(FragmentActivity activity, String tag,
			int position) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return new CompatTabHoneycomb(activity, tag, position);
		} else {
			return new CompatTabEclair(activity, tag, position);
		}
	}

	public abstract CompatTab setText(int resId);

	public abstract CompatTab setText(CharSequence title);

	public abstract CompatTab setIcon(int resId);

	public abstract CompatTab setTabListener(CompatTabListener callback);

	public abstract CompatTab setFragment(Fragment fragment);

	public abstract CharSequence getText();

	public abstract Drawable getIcon();

	public abstract CompatTabListener getCallback();

	public abstract Fragment getFragment();

	public abstract Object getTab();

	public String getTag() {
		return mTag;
	}

	public int getPosition() {
		return mPosition;
	}
}
