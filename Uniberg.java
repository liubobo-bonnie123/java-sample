/**
 * File: $URL: $
 *
 * Abstract: Uniberg activity to display webview
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

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forside.android.view.UFlipperView;
import com.forside.android.view.UListView;
import com.forside.android.view.USearchListView;
import com.forside.android.view.UWebView;
import com.forside.android.Hades9780759519732HA.R;

/**
 * This is the launcher activity of the application.
 * 
 * @author ScrollMotion
 * 
 */
public class Uniberg extends Activity implements OnClickListener {
	/**
	 * The original page width
	 */
	public final static int ORIGNAL_PAGE_WIDTH = 500;
	/**
	 * page left margin
	 */
	public final static int LEFT_MARGIN = 20;
	/**
	 * page right margin
	 */
	public final static int RIGHT_MARGIN = 20;
	/**
	 * Stores the instance of this Activity.
	 */
	private static Uniberg mInstance = null;

	private static final byte PADDING = 2;

	/**
	 * Uimanager to manage all UI related changes.
	 */
	private static UIManager mUIManager;
	/**
	 * Main layout of the page. This layout is the parent of all the views and
	 * layouts displayed on this page.
	 */
	private LinearLayout mainLayout;
	private static Button plus;

	private static Button minus;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		mInstance = this;
		UIManager.setContext(this);
		mUIManager = UIManager.getInstance();
		initScale();
		setContentView(R.layout.webview_screen);

		setTitle(mUIManager.getBookTitle(), mUIManager.getBookAuthor());
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		createWebView();

		View hudLayout = findViewById(R.id.menu_layout);
		View titleLayout = findViewById(R.id.title_layout);
		View gotoLayout = findViewById(R.id.goto_layout);
		Button contentsButton = (Button) findViewById(R.id.contents);
		Button settingsButton = (Button) findViewById(R.id.settings);
		Button storeButton = (Button) findViewById(R.id.store);
		Button searchButton = (Button) findViewById(R.id.search);
		Button searchTitleButton = (Button) findViewById(R.id.search_title);

		hudLayout.setOnClickListener(this);
		titleLayout.setOnClickListener(this);
		gotoLayout.setOnClickListener(this);

		mUIManager.setHudLayout(hudLayout);
		mUIManager.setTitleLayout(titleLayout);
		mUIManager.setButtons(contentsButton, searchButton, storeButton,
				searchTitleButton, settingsButton);

		mUIManager.setButtonListeners();

		mUIManager.setActivty(this);
		mInstance = this;
		mUIManager.updatePage();
		if (savedInstanceState != null)
			if (savedInstanceState.isEmpty() == false) {
				if (savedInstanceState.getBoolean("IsContentsDisplayed") == true)
					mUIManager.setContentsDisplayed(true);
				if (savedInstanceState.getBoolean("IsHUDMenu") == true)
					onPrepareOptionsMenu(null);
			}
	}

	/**
	 * Function to get reference of plus button
	 * 
	 * @return
	 */
	public static Button getPlus() {
		return plus;
	}

	/**
	 * Function to get reference of minus button
	 * 
	 * @return
	 */
	public static Button getMinus() {
		return minus;
	}

	/**
	 * Method called by Android framework to save the instance in bundle.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("IsHUDMenu", mUIManager.isHudMenuActive());
		outState.putBoolean("IsContentsDisplayed",
				mUIManager.isContentDisplayed());
	}

	/**
	 * This function returns the instance of this class
	 * 
	 * @return: mInstance -instance of the this class
	 */
	static public Uniberg getInstance() {
		return mInstance;
	}

	/**
	 * Sets the title bar information.
	 * 
	 * @param title
	 *            string to set as title
	 * @param author
	 *            string to set as author name
	 */
	public void setTitle(String title, String author) {
		((TextView) findViewById(R.id.title)).setText(title);
		((TextView) findViewById(R.id.author)).setText(author);
	}

	/**
	 * This method gets called when the menu key is pressed.
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {

		mUIManager.updateLayout();
		return true;
	}

	/**
	 * This method handles the onclick events of the view.
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_layout:
		case R.id.goto_layout:
		case R.id.menu_layout:
		case R.id.title_layout: {
			mUIManager.updateLayout();
		}
			break;
		}
	}

	

	/**
	 * This method creates an android web view object.
	 */
	void createWebView() {
		UFlipperView flipper = new UFlipperView(this);
		mUIManager.setViewFlipper(flipper);
		final UWebView mWebView = new UWebView(this);

		final UWebView mWebView1 = new UWebView(this);
		
		
		
		mWebView.setClickable(true);
		mWebView.setLongClickable(true);
		mWebView.setBackgroundResource(R.drawable.bg_page_texture);
		mWebView.setPadding(PADDING, PADDING, PADDING, PADDING);
		mUIManager.setCurrentWebView(mWebView);

		/*
		 * // Plus Button for Zoom Out plus = new Button(this);
		 * plus.setBackgroundResource(android.R.drawable.btn_plus);
		 * plus.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { mWebView.zoomIn(); mWebView1.zoomIn();
		 * } }); plus.setId(15); plus.setVisibility(View.VISIBLE);
		 * 
		 * // Minus Button for Zoom In minus = new Button(this);
		 * minus.setBackgroundResource(android.R.drawable.btn_minus);
		 * minus.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { mWebView.zoomOut();
		 * mWebView1.zoomOut(); } }); minus.setId(16);
		 * minus.setVisibility(View.VISIBLE);
		 */

		flipper.addView(mWebView);

		mWebView1.setClickable(true);
		mWebView1.setLongClickable(true);
		mWebView1.setBackgroundResource(R.drawable.bg_page_texture);
		mWebView1.setPadding(PADDING, PADDING, PADDING, PADDING);
		mUIManager.setTempWebView(mWebView1);

		int size = (int) mUIManager.getDefaultFontSize();

		mWebView.getSettings().setDefaultFontSize(size);
		mWebView1.getSettings().setDefaultFontSize(size);
		mUIManager.setWebViewListeners();
		mWebView.setInitialScale(mUIManager.getScale());
		mWebView1.setInitialScale(mUIManager.getScale());
		
		flipper.addView(mWebView1);

		RelativeLayout relLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);

		flipper.setId(1);
		relLayout.addView(flipper, relParams);

		/*
		 * LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * params1.setMargins(0, 0, 20, 10);
		 * params1.addRule(RelativeLayout.ALIGN_RIGHT, 1);
		 * relLayout.addView(plus, params1);
		 * 
		 * LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * 
		 * params2.addRule(RelativeLayout.ALIGN_LEFT, 1); params2.setMargins(10,
		 * 0, 5, 10); relLayout.addView(minus, params2);
		 */

		UListView uListView = new UListView(this);
		USearchListView uSearchListView = new USearchListView(this);
		mUIManager.setListView(uListView);

		mUIManager.setSearchListView(uSearchListView);
		mUIManager.ensureLoadPages();

		RelativeLayout.LayoutParams paramsUU = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsUU.addRule(RelativeLayout.ALIGN_BOTTOM, 1);
		RelativeLayout.LayoutParams paramsUUS = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		relLayout.addView(uListView, paramsUU);
		relLayout.addView(uSearchListView, paramsUUS);
		mUIManager.setSearchListListeners();
		uListView.setVisibility(View.GONE);
		uSearchListView.setVisibility(View.GONE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		mainLayout.addView(relLayout, 1, params);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (mUIManager.handleBackKeyEvent()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Initializes the scale value which would be used as an initial scale for
	 * the webview.
	 */
	private void initScale() {
		if (mUIManager.getScale() != -1) {
			return;
		}
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		Double val = new Double((width < height) ? width : height)
				/ new Double(ORIGNAL_PAGE_WIDTH);
		val = val * 100d;
		mUIManager.setScale(val.intValue());
	}

	@Override
	protected void onDestroy() {
		mUIManager.clear();
		super.onDestroy();
	}
}
