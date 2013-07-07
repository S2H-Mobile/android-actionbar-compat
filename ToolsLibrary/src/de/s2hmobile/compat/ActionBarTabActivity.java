package de.s2hmobile.compat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import de.s2hmobile.compat.tab.CompatTab;
import de.s2hmobile.compat.tab.CompatTabListener;

public abstract class ActionBarTabActivity extends TabActivityBase {

	/**
	 * Implementation of {@link CompatTabListener} to handle tab change events.
	 * This implementation instantiates the specified fragment class with no
	 * arguments when its tab is selected.
	 */
	public static class ActionBarTabListener implements CompatTabListener {

		private final ActionBarTabActivity mActivity;
		private final Class<? extends Fragment> mFragmentClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            - the host Activity, used to instantiate the fragment
		 * @param clz
		 *            - the fragment class, used to instantiate the fragment
		 */
		public ActionBarTabListener(ActionBarTabActivity activity,
				Class<? extends Fragment> clz) {
			mActivity = activity;
			mFragmentClass = clz;
		}

		@Override
		public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
		}

		/** Instantiates the tab fragment when necessary. */
		@Override
		public void onTabSelected(CompatTab tab, FragmentTransaction ft) {

			// check if fragment is already initialized
			Fragment fragment = tab.getFragment();
			if (fragment == null) {

				// instantiate and add the fragment to the activity
				fragment = Fragment.instantiate(mActivity,
						mFragmentClass.getName());

				// TODO remove log statement
				android.util.Log.i("ActionBarTabActivity", "instantiating "
						+ mFragmentClass.getName());

				tab.setFragment(fragment);

				// set animation
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

				/*
				 * The training class proposed to select the id of the view
				 * according to platform version.
				 * 
				 * Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
				 * android.R.id.content : R.id.tabcontent;
				 */
				ft.add(android.R.id.tabcontent, fragment, tab.getTag());
			} else {

				// if fragment exists, attach it in order to show it
				ft.attach(fragment);
			}
		}

		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			final Fragment fragment = tab.getFragment();
			if (fragment != null) {

				// detach the fragment, because another one is being attached
				ft.detach(fragment);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_compat);
	}
}