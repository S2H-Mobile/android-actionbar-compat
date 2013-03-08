/*
 * Copyright (C) 2012 S2H Mobile
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

package de.s2hmobile.tools;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

public class IntentHelper {

	private IntentHelper() {}
	
	/**
	 * Builds an intent to launch either the Google Play app or the website.
	 * @param context the context
	 * @param packageName the package name of the app
	 * @return the intent
	 */
	public static Intent launchGPlay(Context context, String packageName) {	
		Intent result = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.parse("market://details?id=" + packageName);
		result.setData(data);
		if (!isIntentSafe(context, result, PackageManager.MATCH_DEFAULT_ONLY)) {
			data = Uri.parse("http://play.google.com/store/apps/details?id=" + packageName);
			result.setData(data);
		}
		return result;
	}
	
    /**
     * Uses the {@link PackageManager} to check if the intent can be handled. 
     * 
     * @param context for the package manager
     * @param intent the intent to evaluate
     * @param flag for querying, indicates the package category
     * @return true if there is at least one activity that can handle the intent
     */
	protected static boolean isIntentSafe(Context context, Intent intent, int flag) {
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(intent, flag);
		return activities.size() > 0;
	}
	  
    public static boolean isConnected(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    return info != null && info.isConnectedOrConnecting();
	}
}
