/* File modified by S2H Mobile.
 * 
 * Copyright 2011 The Android Open Source Project
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

package de.s2hmobile.compat.actionbar;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.s2hmobile.compat.R;

/** Implements the action bar pattern for pre-Honeycomb devices. */
public class ActionBarHelperFroyo extends ActionBarHelper {
	/**
	 * A {@link android.view.MenuInflater} that reads action bar metadata.
	 */
	private class WrappedMenuInflater extends MenuInflater {
		MenuInflater mInflater;

		public WrappedMenuInflater(final Context context,
				final MenuInflater inflater) {
			super(context);
			mInflater = inflater;
		}

		@Override
		public void inflate(final int menuRes, final Menu menu) {
			loadActionBarMetadata(menuRes);
			mInflater.inflate(menuRes, menu);
		}

		/**
		 * Loads action bar metadata from a menu resource, storing a list of
		 * menu item IDs that should be shown on-screen (i.e. those with
		 * showAsAction set to always or ifRoom).
		 * 
		 * @param menuResId
		 */
		private void loadActionBarMetadata(final int menuResId) {
			XmlResourceParser parser = null;
			try {
				parser = mActivity.getResources().getXml(menuResId);

				int eventType = parser.getEventType();
				int itemId = 0;
				int showAsAction = 0;

				boolean eof = false;
				while (!eof) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if (!parser.getName().equals("item")) {
							break;
						}

						itemId = parser.getAttributeResourceValue(NAMESPACE,
								MENU_ID, 0);
						if (itemId == 0) {
							break;
						}

						showAsAction = parser.getAttributeIntValue(NAMESPACE,
								MENU_ATTRIBUTE, -1);
						if (showAsAction == MenuItem.SHOW_AS_ACTION_ALWAYS
								|| showAsAction == MenuItem.SHOW_AS_ACTION_IF_ROOM) {
							mActionItemIds.add(itemId);
						}
						break;

					case XmlPullParser.END_DOCUMENT:
						eof = true;
						break;
					}

					eventType = parser.next();
				}
			} catch (final XmlPullParserException e) {
				throw new InflateException("Error inflating menu XML", e);
			} catch (final IOException e) {
				throw new InflateException("Error inflating menu XML", e);
			} finally {
				if (parser != null) {
					parser.close();
				}
			}
		}
	}

	private static final String MENU_ATTRIBUTE = "showAsAction";
	private static final String MENU_ID = "id";

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";

	protected Set<Integer> mActionItemIds = new HashSet<Integer>();

	protected ActionBarHelperFroyo(final Activity activity,
			final boolean isHomeStateful) {
		super(activity, isHomeStateful);
	}

	/**
	 * Returns a {@link android.view.MenuInflater} that can read action bar
	 * metadata on pre-Honeycomb devices.
	 */
	@Override
	public MenuInflater getMenuInflater(final MenuInflater superMenuInflater) {
		return new WrappedMenuInflater(mActivity, superMenuInflater);
	}

	/** {@inheritDoc} */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	}

	/**
	 * ActionBar helper code to be run in
	 * {@link Activity#onCreateOptionsMenu(android.view.Menu)}. Hides on-screen
	 * action items from the options menu by marking them as invisible.
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		for (final Integer id : mActionItemIds) {
			menu.findItem(id).setVisible(false);
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void onPostCreate(final Bundle savedInstanceState) {

		// request custom window title bar
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.actionbar_compat);

		// set title and home icon
		setupActionBar();

		final SimpleMenu menu = new SimpleMenu(mActivity);
		mActivity.onCreatePanelMenu(Window.FEATURE_OPTIONS_PANEL, menu);
		mActivity.onPrepareOptionsMenu(menu);

		// add the menu icons from XML
		for (int i = 0; i < menu.size(); i++) {
			final MenuItem item = menu.getItem(i);
			if (mActionItemIds.contains(item.getItemId())) {
				addActionItemCompatFromMenuItem(item);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onTitleChanged(final CharSequence title, final int color) {
		final TextView titleView = (TextView) mActivity
				.findViewById(R.id.actionbar_compat_title);
		if (titleView != null) {
			titleView.setText(title);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void setRefreshActionItemState(final boolean refreshing) {
		final View refreshButton = mActivity
				.findViewById(R.id.actionbar_compat_item_refresh);
		final View refreshIndicator = mActivity
				.findViewById(R.id.actionbar_compat_item_refresh_progress);

		if (refreshButton != null) {
			refreshButton.setVisibility(refreshing ? View.GONE : View.VISIBLE);
		}
		if (refreshIndicator != null) {
			refreshIndicator.setVisibility(refreshing ? View.VISIBLE
					: View.GONE);
		}
	}

	/**
	 * Adds an action button to the compatibility action bar, using menu
	 * information from a {@link android.view.MenuItem}. If the menu item ID is
	 * <code>menu_refresh</code>, the menu item's state can be changed to show a
	 * loading spinner using
	 * {@link ActionBarHelperFroyo#setRefreshActionItemState(boolean)}.
	 */
	private View addActionItemCompatFromMenuItem(final MenuItem item) {
		final ViewGroup actionBar = getActionBarCompat();
		if (actionBar == null) {
			return null;
		}

		final int itemId = item.getItemId();

		// choose style depending on type of menu item
		final int buttonStyle = itemId == android.R.id.home ? mHomeActive ? R.attr.actionbarCompatItemStatefulHomeStyle
				: R.attr.actionbarCompatItemHomeStyle
				: R.attr.actionbarCompatItemStyle;

		// create action item
		final ImageButton actionButton = new ImageButton(mActivity, null,
				buttonStyle);

		// configure action item
		final Resources res = mActivity.getResources();
		final int dimenId = itemId == android.R.id.home ? R.dimen.actionbar_compat_button_home_width
				: R.dimen.actionbar_compat_button_width;
		final int width = (int) res.getDimension(dimenId);
		final int height = ViewGroup.LayoutParams.MATCH_PARENT;

		actionButton.setLayoutParams(new ViewGroup.LayoutParams(width, height));
		if (itemId == R.id.menu_refresh) {
			actionButton.setId(R.id.actionbar_compat_item_refresh);
		}

		actionButton.setImageDrawable(item.getIcon());
		actionButton.setScaleType(ImageView.ScaleType.CENTER);
		actionButton.setContentDescription(item.getTitle());
		actionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				mActivity
						.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, item);
			}
		});

		actionBar.addView(actionButton);

		/*
		 * Refresh buttons should be stateful, and allow for indeterminate
		 * progress indicators, so add those.
		 */
		if (item.getItemId() == R.id.menu_refresh) {
			final ProgressBar indicator = new ProgressBar(mActivity, null,
					R.attr.actionbarCompatProgressIndicatorStyle);

			final int buttonWidth = res
					.getDimensionPixelSize(R.dimen.actionbar_compat_button_width);
			final int buttonHeight = res
					.getDimensionPixelSize(R.dimen.actionbar_compat_height);
			final int indicatorSize = buttonWidth / 2;

			final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					indicatorSize, indicatorSize);

			params.setMargins((buttonWidth - indicatorSize) / 2,
					(buttonHeight - indicatorSize) / 2,
					(buttonWidth - indicatorSize) / 2, 0);

			indicator.setLayoutParams(params);
			indicator.setVisibility(View.GONE);
			indicator.setId(R.id.actionbar_compat_item_refresh_progress);
			actionBar.addView(indicator);
		}
		return actionButton;
	}

	/**
	 * Returns the {@link android.view.ViewGroup} for the action bar on phones
	 * (compatibility action bar). Can return null, and will return null on
	 * Honeycomb.
	 */
	private ViewGroup getActionBarCompat() {
		return (ViewGroup) mActivity.findViewById(R.id.actionbar_compat);
	}

	/**
	 * Sets up the compatibility action bar with title and home icon. The value
	 * of <code>android.R.id.home</code> is inlined at compile time.
	 */
	@SuppressLint("InlinedApi")
	private void setupActionBar() {
		final ViewGroup actionBarCompat = getActionBarCompat();
		if (actionBarCompat == null) {
			return;
		}

		// add home icon using a temporary SimpleMenu
		final SimpleMenu tempMenu = new SimpleMenu(mActivity);
		final SimpleMenuItem homeItem = new SimpleMenuItem(tempMenu,
				android.R.id.home, 0, mActivity.getString(R.string.menu_home));
		// homeItem.setIcon(R.drawable.ic_home);
		addActionItemCompatFromMenuItem(homeItem);

		// add title text
		final TextView titleText = new TextView(mActivity, null,
				R.attr.actionbarCompatTitleStyle);

		final LinearLayout.LayoutParams springLayoutParams = new LinearLayout.LayoutParams(
				0, ViewGroup.LayoutParams.MATCH_PARENT);
		springLayoutParams.weight = 1;

		titleText.setLayoutParams(springLayoutParams);
		titleText.setText(mActivity.getTitle());
		actionBarCompat.addView(titleText);
	}
}