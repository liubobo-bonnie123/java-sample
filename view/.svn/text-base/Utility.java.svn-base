package com.forside.android.view;

import android.content.Context;

public final class Utility {
	
	/*
	 * Conver dp to pix
	 * refer to:http://developer.android.com/guide/practices/screens_support.html#screen-independence
	 * */
	public static int ConvertDPToPix(Context context, int dp)
	{
		// Convert the dps to pixels
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);

	}
}
