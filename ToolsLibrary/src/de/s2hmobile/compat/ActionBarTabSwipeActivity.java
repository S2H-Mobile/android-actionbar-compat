package de.s2hmobile.compat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import de.s2hmobile.compat.tab.CompatTab;
import de.s2hmobile.compat.tab.CompatTabListener;
import de.s2hmobile.compat.tab.SectionsPagerAdapter;
import de.s2hmobile.compat.tab.TabHelper;

/**
 * Extend this class if you want the FixedTabs+Swipe pattern. This class
 * implements {@link CompatTabListener} to handle tab change events.
 * 
 * A base activity that defers tab functionality to a {@link TabHelper}.
 * 
 * When building an activity with tabs, extend this class in order to provide
 * compatibility with API level 5 and above. Using this class along with the
 * {@link TabHelper} and {@link com.example.android.tabcompat.lib.CompatTab}
 * classes
 * 
 * 
 * You can make use of a tab UI that's built using the
 * {@link android.app.ActionBar} on Honeycomb+ and the
 * {@link android.widget.TabWidget} on all older versions.
 * 
 * 
 * 
 * @author Stephan Hoehne
 * 
 */
// TODO improve documentation
public abstract class ActionBarTabSwipeActivity extends TabActivityBase
		implements CompatTabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter = null;

	private ViewPager mViewPager = null;

	/**
	 * If the user is currently looking at the first step, allow the system to
	 * handle the Back button. This calls finish() on this activity and pops the
	 * back stack. Otherwise, select the previous step.
	 */
	@Override
	public void onBackPressed() {

		// get the current position within the ViewPager
		int currentItem = mViewPager.getCurrentItem();
		if (currentItem == 0) {

			// we are at at the first page, with position = 0
			super.onBackPressed();
		} else {
			mViewPager.setCurrentItem(--currentItem);
		}
	}

	@Override
	public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
	}

	/**
	 * Implementation of {@link CompatTabListener} to handle tab change events.
	 * When the tab is selected, switch to the corresponding page in the
	 * {@link ViewPager}.
	 */
	@Override
	public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
		final Fragment fragment = tab.getFragment();
		if (fragment != null) {

			// detach the fragment, because another one is being attached
			ft.detach(fragment);
		}
	}

	@Override
	protected TabActivityBase getActivity() {
		return ActionBarTabSwipeActivity.this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_compat_pager);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		// create adapter that will return a fragment for each of the sections
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(), getResources());

		// add the sections to the adapter
		declareSections(mSectionsPagerAdapter);

		// set up the tabs
		final TabHelper tabHelper = super.getTabHelper();

		// add a tab to the action bar for each of the sections in the activity
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {

			// the page title, as defined by the adapter
			final CharSequence title = mSectionsPagerAdapter.getPageTitle(i);

			// give this tab the fragment name to hold for later use
			final String tag = mSectionsPagerAdapter.getTag(i);

			// create the tab
			final CompatTab compatTab = createTab(tag, i);

			// set the title
			compatTab.setText(title);

			// set the listener for when this tab is selected
			compatTab.setTabListener(ActionBarTabSwipeActivity.this);

			// add the tab to the action bar
			tabHelper.addTab(compatTab);
		}

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// listener for swipe events
		final OnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				// select the tab corresponding to the new page
				getTabHelper().setSelectedTab(position);
			}

		};
		mViewPager.setOnPageChangeListener(listener);
	}

	protected abstract void declareSections(SectionsPagerAdapter adapter);
}