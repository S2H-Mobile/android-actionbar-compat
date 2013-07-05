package de.s2hmobile.compat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import de.s2hmobile.compat.tab.CompatTab;
import de.s2hmobile.compat.tab.CompatTabListener;

public abstract class ActionBarTabSwipeActivity extends TabActivityBase {

	public class ActionBarTabSwipeListener<T extends Fragment> implements
			CompatTabListener {

		private final TabActivityBase mActivity;
		private final Bundle mArgs;
		private final int mContainerId;
		private final Class<T> mFragmentClass;

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
		public ActionBarTabSwipeListener(TabActivityBase activity,
				Class<T> clz, Bundle args) {
			mActivity = activity;
			mFragmentClass = clz;
			mArgs = args;
			mContainerId = android.R.id.tabcontent;
			// Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
			// ? android.R.id.content // Honeycomb and above
			// : R.id.realtabcontent; // Froyo and Gingerbread
		}

		@Override
		public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
		}

		/**
		 * Instantiates the tab fragment when necessary.
		 */
		@Override
		public void onTabSelected(CompatTab tab, FragmentTransaction ft) {

			// When the tab is selected, switch to the
			// corresponding page in the ViewPager.
			mViewPager.setCurrentItem(tab.getPosition());

			Fragment fragment = tab.getFragment();
			if (fragment == null) {
				fragment = Fragment.instantiate(mActivity,
						mFragmentClass.getName(), mArgs);
				tab.setFragment(fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.add(mContainerId, fragment, tab.getTag());
			} else {

				// if fragment exists, attach it in order to show it
				ft.attach(fragment);
			}
		}

		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment != null) {

				// detach the fragment, because another one is being attached
				ft.detach(fragment);
			}
		}
	}

	protected ViewPager mViewPager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_compat_pager);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between pages, select the
						// corresponding tab.
						// getActionBar().setSelectedNavigationItem(position);
						getTabHelper().setSelectedTab(position);
					}
				});

	}
}