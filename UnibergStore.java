/**
 * File: $URL: $
 *
 * Abstract: Activity to display Store UI
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 1st Dec 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */
package com.forside.android;

import android.app.Activity;
import com.forside.android.Hades9780759519732HA.R;
import com.forside.android.vending.VendingProvider;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * This activity is to handle Uniberg Store.
 * 
 * @author ScrollMotion
 * 
 */
public class UnibergStore extends Activity {

	WebView web;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		web = new WebView(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		// to handle to start loading and finished loading event
		web.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			/**
			 * Function called when the page starts loading
			 */
			public void onLoadResource(WebView view, String url) {
				showProgressBar();
			}

			/**
			 * Function called when the page has been been completed loaded
			 */
			public void onPageFinished(WebView view, String url) {
				hideProgressBar();
			}
		});

		// the URL which has to be loaded
		web.loadUrl("http://www.mobi-book.com/");
		//		web.loadUrl(VendingProvider.GetVendingService().GetStoreUrl());
		setContentView(R.layout.store_screen);

		// to add web view below the header
		RelativeLayout webviewLayout = (RelativeLayout) findViewById(R.id.webviewLayout);
		webviewLayout.addView(web, params);

		Button done_button = (Button) findViewById(R.id.done);

		// to handle "Done" button event
		done_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				done();
			}
		});
	}

	/**
	 * This method gets called when the Done button is pressed.
	 */
	private void done() {
		UnibergStore.this.finish();
	}

	/**
	 * Function to show the progress bar when the page is being loaded
	 */
	public void showProgressBar() {
		RelativeLayout proBarLayout = (RelativeLayout) findViewById(R.id.proBarLayout);
		proBarLayout.setVisibility(RelativeLayout.VISIBLE);
		proBarLayout.setBackgroundColor("#336C70".hashCode());
		proBarLayout.setClickable(true);
	}

	/**
	 * Function to hide the progress bar when the page has been completely
	 * loaded
	 */
	public void hideProgressBar() {
		RelativeLayout proBarLayout = (RelativeLayout) findViewById(R.id.proBarLayout);
		proBarLayout.setVisibility(RelativeLayout.INVISIBLE);
		proBarLayout.setClickable(false);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * This method gets called when the orientation of device changes. Normally
	 * when the orientation changes, on Create method is called. As this method
	 * has been overridden, onCreate is not called and thus web view is not
	 * created again. This helps preventing reload of the URL.
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
