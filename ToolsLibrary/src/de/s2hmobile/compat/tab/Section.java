package de.s2hmobile.compat.tab;

import android.support.v4.app.Fragment;

/**
 * This pojo is a section object for the {@link SectionsPagerAdapter}.
 * 
 * @author Stephan Hoehne
 * 
 */
class Section {

	private final Class<? extends Fragment> mFragmentClass;
	private final String mTag;
	private final int mTitleId;

	Section(String tag, int titleId, Class<? extends Fragment> clz) {
		mTag = tag;
		mTitleId = titleId;
		mFragmentClass = clz;
	}

	String getTag() {
		return mTag;
	}

	Class<? extends Fragment> getFragmentClass() {
		return mFragmentClass;
	}

	int getTitleId() {
		return mTitleId;
	}

	// TODO implement factory

}
