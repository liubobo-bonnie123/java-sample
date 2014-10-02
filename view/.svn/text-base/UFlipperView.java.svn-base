/**
 * File: $URL: $
 *
 * Abstract: Flipper view
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 26th Nov 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */
package com.forside.android.view;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ViewFlipper;

/**
 * Customized ViewFlipper
 * 
 * @author ScrollMotion
 * 
 */
public class UFlipperView extends ViewFlipper {

	private static final String tag = "UFlipperView";

	public UFlipperView(Context context) {
		super(context);
	}

	@Override
	protected void onDetachedFromWindow() {
//this FlipperView bug also exist in 2.2 and 2.3.3
		try {
			super.onDetachedFromWindow();
		} catch (IllegalArgumentException e) {
			Log.e(tag, "Prevent IllegalArgumentException error in UFlipperView."+e.getMessage());
			stopFlipping();
		}
	}
}
