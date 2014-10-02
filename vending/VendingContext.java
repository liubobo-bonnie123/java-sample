package com.forside.android.vending;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class VendingContext {
	private final Activity activity;
	private final Runnable allowHandler;
	private final Runnable disallowHandler;
	
	public static final int DIALOG_EORROR_ID = 0;
	public static final int DIALOG_LICENSE_DONOT_ALLOW=1;
	public static final String LICENSE_ERROR_MSG_ID = "0";
	
	public VendingContext(Activity activity,
			Runnable allowHandler, Runnable disallowHandler) {

		this.activity = activity;
		this.allowHandler = allowHandler;
		this.disallowHandler = disallowHandler;
	}

	public Context getContext() {
		return activity.getApplicationContext();
	}

	public void handleApplicationError(final String errorMsg)
	{
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Bundle args = new Bundle();
				args.putString(LICENSE_ERROR_MSG_ID, errorMsg);
				activity.showDialog(DIALOG_EORROR_ID,args);
			}
		});
	}
	
	public void allow() {
		if (activity.isFinishing()) {
			// Don't update UI if Activity is finishing.
			return;
		}
		allowHandler.run();
	}

	public void dontAllow() {
		if (activity.isFinishing()) {
			// Don't update UI if Activity is finishing.
			return;
		}
		disallowHandler.run();
	}
}
