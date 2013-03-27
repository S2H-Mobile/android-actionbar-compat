package de.s2hmobile.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkHelper {
	
	private NetworkHelper() {}
	
	public static org.apache.http.client.HttpClient getHttpClient() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			// TODO http://android-developers.blogspot.de/2011/09/androids-http-clients.html
			// return new java.net.HttpURLConnection
			return new org.apache.http.impl.client.DefaultHttpClient();
		} else {
			return new org.apache.http.impl.client.DefaultHttpClient();
		}
	}
	  
    public static boolean isConnected(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    return info != null && info.isConnectedOrConnecting();
	}
}
