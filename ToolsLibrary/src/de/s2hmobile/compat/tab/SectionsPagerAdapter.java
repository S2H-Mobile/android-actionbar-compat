/*
 * Copyright (C) 2012 - 2013, S2H Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.s2hmobile.compat.tab;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import de.s2hmobile.compat.ActionBarTabSwipeActivity;

/**
 * <p>
 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments
 * for each of the sections. You can choose between two adapters for the view
 * pager, {@link FragmentPagerAdapter} and {@link FragmentStatePagerAdapter}.
 * </p>
 * 
 * <p>
 * FragmentPagerAdapter keeps the current view and its neighbours in memory.
 * When you are on page 5 - memory will be allocated for 4,5,6 Fragments. When
 * you move to page 6, 4 is removed and 7 is added. When a fragment is removed,
 * it destroys the view. When you get back to the fragment
 * <code>onCreateView()</code> is started.
 * </p>
 * 
 * <p>
 * FragmentStatePagerAdapter is more memory friendly with a disadvantage of
 * worse performance. It destroys the whole fragment. When you get back to it,
 * the view pager will invoke onCreate and onCreateView.
 * </p>
 * 
 * <p>
 * We use a FragmentPagerAdapter derivative. If this becomes too memory
 * intensive, it may be best to switch to a FragmentStatePagerAdapter.
 * </p>
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private final SparseArrayCompat<Section> mSections = new SparseArrayCompat<Section>();

	private final Resources mResources;

	public SectionsPagerAdapter(FragmentManager fm, Resources resources) {
		super(fm);
		mResources = resources;
	}

	/**
	 * The number of elements in the hasmap determines the number of pages in
	 * {@link ActionBarTabSwipeActivity#mViewPager}.
	 */
	@Override
	public int getCount() {
		return mSections.size();
	}

	/**
	 * Returns the {@link Fragment} instance corresponding to the section at the
	 * given position.
	 */
	@Override
	public Fragment getItem(int position) {
		final Class<? extends Fragment> clz = mSections.get(position)
				.getFragmentClass();

		// TODO remove log statement
		android.util.Log.i("SectionsPagerAdapter", "getItem(" + position
				+ ") called, creating newInstance()");

		Fragment fragment = new Fragment();
		try {
			fragment = clz.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (NullPointerException e) {
		}
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		final Section section = mSections.get(position);
		return mResources.getString(section.getTitleId());
	}

	/**
	 * Get the tag of the fragment at the given position in the
	 * {@link ViewPager}.
	 * 
	 * @param position
	 *            - the position in the {@link ViewPager}
	 * @return The tag of the fragment at this position.
	 */
	public String getTag(int position) {
		return mSections.get(position).getTag();
	}

	public void putSection(int position, String tag, int titleId,
			Class<? extends Fragment> clz) {
		mSections.put(position, new Section(tag, titleId, clz));
	}
}