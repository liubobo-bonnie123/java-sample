/**
 * File: $URL: $
 *
 * Abstract: Uniberg activity to display splash screen
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */

package com.forside.android;

import com.forside.android.Hades9780759519732HA.R;
import com.forside.android.vending.VendingContext;
import com.forside.android.vending.VendingProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

//import android.os.CountDownTimer;

/**
 * This activity displays the initial splash screen of the application, and upon
 * successful initialization of webview, shows webview to use the application.
 * 
 * @author ScrollMotion
 * 
 */
public class SplashScreen extends Activity {

	//save the time starting checking license.
	long mCheckingLicenseTime;
	//splash screen delay time, default is 2s.
	static final long SplashScreenDelayTime = 2 * 1000;
	
	final VendingContext vendingContext = new VendingContext(this,
			//Handler when license is valid
			new Runnable() {
				@Override
				public void run() {
					//if checking license too quick, we will keep the splash screen show 2s. 
					long waitTime = mCheckingLicenseTime + SplashScreenDelayTime - System.currentTimeMillis(); 
					if(waitTime >0)
					{
						try {
							Thread.sleep(waitTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// Start the activity.
							Intent intent = new Intent();
							intent.setClass(SplashScreen.this, Uniberg.class);

							startActivity(intent);
							finish();
						}
					});

				}
			},
			//Fail to valid license
			new Runnable() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Bundle args = new Bundle();
							args.putString(VendingContext.LICENSE_ERROR_MSG_ID,
									getString(R.string.dont_allow));
							showDialog(
									VendingContext.DIALOG_LICENSE_DONOT_ALLOW,
									args);

						}
					});
				}
			});

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            Bundler reference to the instance state if this activity was
	 *            started after it got killed, blank if this is the first
	 *            invocation.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		// Intent intent = new Intent();
		// intent.setClass(SplashScreen.this, Uniberg.class);
		//
		// startActivity(intent);
		// finish();
		// return;

		mCheckingLicenseTime = System.currentTimeMillis();
		new Thread(new Runnable() {

			@Override
			public void run() {
				VendingProvider.GetVendingService().GetLicenseChecker()
						.ValidateLicense(vendingContext);
			}
		}).start();

		// mDisplayTimer.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		VendingProvider.GetVendingService().GetLicenseChecker().Destory();
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SplashScreen.this.finish();
					}
				});
		Dialog dialog = null;
		switch (id) {
		case VendingContext.DIALOG_EORROR_ID:
			String errMsg = args.getString(VendingContext.LICENSE_ERROR_MSG_ID);
			if (errMsg == null) {
				errMsg = "Error happens when validating license, exit now.";
			}
			builder.setMessage(errMsg);
			dialog = builder.create();
			break;
		case VendingContext.DIALOG_LICENSE_DONOT_ALLOW:
			String dontAllowMsg = args
					.getString(VendingContext.LICENSE_ERROR_MSG_ID);
			if (dontAllowMsg == null) {
				errMsg = "The product is unlicensed, please purchase firstly.";
			}
			builder.setMessage(dontAllowMsg);
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
}