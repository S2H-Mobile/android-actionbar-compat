/**
 * 
 */
package de.s2hmobile.tools.multithreading;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Callback that listens to bitmap rendered events.
 * 
 * @author S2H Mobile
 * 
 */
public interface OnBitmapRenderedListener {
	public void onBitmapRendered(ImageView view, Bitmap bitmap);
}
