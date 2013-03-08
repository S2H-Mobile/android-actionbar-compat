package de.s2hmobile.tools.tabcompat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * Implementation of {@link CompatTabListener} to handle tab change events. This implementation
 * instantiates the specified fragment class with no arguments when its tab is selected.
 */
public class SimpleTabListener implements CompatTabListener {

    private final ActionBarTabActivity mActivity;
    private final Class<? extends Fragment> mClass;

    /**
     * Constructor used each time a new tab is created.
     *
     * @param activity The host Activity, used to instantiate the fragment
     * @param cls      The class representing the fragment to instantiate
     */
    public SimpleTabListener(ActionBarTabActivity activity, Class<? extends Fragment> cls) {
        mActivity = activity;
        mClass = cls;
    }

    /* The following are each of the ActionBar.TabListener callbacks */
    @Override
    public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
        // Check if the fragment is already initialized
        Fragment fragment = tab.getFragment();
        if (fragment == null) {
            // If not, instantiate and add it to the activity
            fragment = Fragment.instantiate(mActivity, mClass.getName());
            tab.setFragment(fragment);
            ft.add(android.R.id.tabcontent, fragment, tab.getTag());
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
        // User selected the already selected tab. Do nothing.
    }
}