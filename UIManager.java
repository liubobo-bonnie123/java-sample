/**
 * File: $URL: $

 *
 * Abstract: Manages the UI. This class handles all the events.
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 16th Nov 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */

package com.forside.android;

import java.util.List;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.forside.android.json.JSONParser;
import com.forside.android.view.UListView;
import com.forside.android.view.USearchListView;
import com.forside.android.view.UWebView;
import com.forside.android.xml.NavPointData;
import com.forside.android.xml.NavPointMap;
import com.forside.android.Hades9780759519732HA.R;
/**
 * This class is responsible to manage UI rendering as per the events.
 * 
 * @author ScrollMotion
 * 
 */
public class UIManager {
	/**
	 * Variable to check whether the page navigation is from search list or not.
	 */
	static boolean sIsFromSearchList = false;
	/**
	 * Singleton instance of UIManager.
	 */
	private static UIManager instance;

	/**
	 * A variable to check whether the HUD controls are active or not.
	 */
	private boolean mIsHUDMenu = false;

	private Button contentsButton;
	private Button searchButton;
	private Button settingsButton;
	private Button storeButton;
	private Button searchTitleButton;

	private Activity activty = null;

	public Activity getActivty() {
		return activty;
	}

	public void setActivty(Activity activty) {
		this.activty = activty;
	}

	/**
	 * To check whether Search list is displayed or not.
	 */
	private boolean mIsSearchDisplayed;

	private float defaultFontSize = 28.0f;
	/**
	 * Layout containing all Hud buttons
	 */
	private View hudLayout;

	private View titleLayout;

	private static Context mContext;

	/**
	 * This list contains the all pages information of the book.
	 */
	private List<NavPointData> pages;

	private ContentsManager mContentsManager;
	private PageManager mPageManager;
	private USearchListView uSearchListView;

	private boolean mIsSampleApp = false;

	private static int scale = -1;
	private int minFont = 20;
	private int maxFont = 50;

	/**
	 * Made as private to achieve singleton design pattern.
	 */
	private UIManager() {
		updatePages();
		mContentsManager = new ContentsManager(pages, mContext);
		mPageManager = new PageManager(pages, mContext);
	}

	/**
	 * Returns the instance of UIManager
	 * 
	 * @return instance of UIManager
	 */
	public static UIManager getInstance() {
		if (instance == null) {
			instance = new UIManager();
		}
		return instance;

	}

	/**
	 * Sets the context.
	 * 
	 * @param context
	 */
	static void setContext(Context context) {
		mContext = context;
	}

	/**
	 * Updates the font size of the web view
	 */
	void updateWebViewFont() {
		mPageManager.updateWebViewFont();
	}

	/**
	 * Sets the Listeners of Web View. These listeners include Swipe and Tap
	 * events listener.
	 */
	void setWebViewListeners() {
		mPageManager.setWebViewListeners();
	}

	/**
	 * Sets the onclick event listeners for all the buttons in the Hud controls.
	 */
	void setButtonListeners() {
		contentsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mContentsManager.isContentsDisplayed()) {
					hideContents();
				} else {
					hideSearch();
					showContents();
				}
			}
		});

		settingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSearch();
				updateContentsButtonBackground(false);
				Intent intent = new Intent(mContext, UnibergSettings.class);

				mContext.startActivity(intent);
			}
		});

		storeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideContents();
				hideSearch();
				/*
				 * Toast toast = Toast.makeText(mContext,
				 * "Store functionality will be implemented in TIER-2.", 2000);
				 * toast.show();
				 */
				NavPointMap navMap = NavPointMap.getInstance();

				// if application type is not full, then show "Buy" button to
				// user
				if (!navMap.getAppType().equalsIgnoreCase(NavPointMap.APP_FULL)
						&& navMap.getDocType().equalsIgnoreCase(
								NavPointMap.TYPE_AGENCY)) {
					Intent intent = new Intent(mContext, UnibergBuy.class);
					mContext.startActivity(intent);
				} else {
					Intent intent = new Intent(mContext, UnibergStore.class);
					mContext.startActivity(intent);
				}
			}
		});
		searchTitleButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hideContents();
				hideSearch();
				Intent intent = new Intent(mContext, UnibergSearchTitle.class);
				mContext.startActivity(intent);

			}
		});
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * hideContents(); Toast toast = Toast.makeText(mContext,
				 * "Search functionality will be implemented in TIER-2.", 2000);
				 * toast.show();
				 */
				showSearch();

			}
		});
	}

	/**
	 * Updates the title and Hud layouts.
	 */
	void updateLayout() {
		switch (hudLayout.getVisibility()) {
		case View.VISIBLE:
		case View.INVISIBLE:
			hudLayout.setVisibility(View.GONE);
			titleLayout.setVisibility(View.GONE);
			hideContents();
			mIsHUDMenu = false;
			break;
		case View.GONE:
			hudLayout.setVisibility(View.VISIBLE);
			titleLayout.setVisibility(View.VISIBLE);
			if (mContentsManager.isContentsDisplayed()) {
				showContents();
			} else {
				hideContents();
			}
			mIsHUDMenu = true;
			break;
		}
	}

	/**
	 * Updates the Web View as per the current page to be displayed.
	 */

	void updatePage() {
		mPageManager.updateWebview();
	}

//	void setPageNumber(int pageNumber) {
//		mPageManager.setPagseNumber(pageNumber);
//	}

	/**
	 * Hides the contents list.
	 */
	void updateContentsButtonBackground(boolean isPressed) {
		if (isPressed) {
			contentsButton.setBackgroundResource(R.drawable.navbtn_contents_on);
		} else {

			contentsButton.setBackgroundResource(R.drawable.navbtn_contents);
		}
	}

	/**
	 * Displays the contents list.
	 */
	void showContents() {
		mContentsManager.show();
		updateContentsButtonBackground(true);
	}

	void hideContents() {
		mContentsManager.hide();
		updateContentsButtonBackground(false);
	}

	/**
	 * Initializes the hud menu buttons.
	 * 
	 * @param contentsButton
	 * @param searchButton
	 * @param storeButton
	 * @param settingsButton
	 */
	void setButtons(Button contentsButton, Button searchButton,
			Button storeButton, Button searchTitleButton, Button settingsButton) {
		this.contentsButton = contentsButton;
		this.searchButton = searchButton;
		this.storeButton = storeButton;
		this.searchTitleButton = searchTitleButton;
		this.settingsButton = settingsButton;

		NavPointMap navMap = NavPointMap.getInstance();

		// if application type is not full, then show "Buy" button to user
		if (!navMap.getAppType().equalsIgnoreCase(NavPointMap.APP_FULL)
				&& navMap.getDocType()
						.equalsIgnoreCase(NavPointMap.TYPE_AGENCY)) {
			this.storeButton.setBackgroundResource(R.drawable.buy_button);
			mIsSampleApp = true;
		}
	}

	/**
	 * Updates the book's pages. This data is updated from the xml file. Updates
	 * the complete books information in a list.
	 */
	void updatePages() {
		// XML Parsing
		// UnibergXMLParser xmlParser = new UnibergXMLParser();
		// xmlParser.context = mContext;
		// xmlParser.parse();

		// JSON Parsing
		JSONParser parser = JSONParser.getInstance();
		parser.parse();
		NavPointMap navMap = NavPointMap.getInstance();

		pages = navMap.getList();
	}

	/**
	 * Returns the book's title
	 * 
	 * @return the book's title
	 */
	String getBookTitle() {
		return NavPointMap.getInstance().getDocTitle();

	}

	/**
	 * Returns author's name of the book
	 * 
	 * @return author's name of the book
	 */
	String getBookAuthor() {
		return NavPointMap.getInstance().getDocAuthor();
	}

	/**
	 * Sets layout having hud menu controls
	 * 
	 * @param hudLayout
	 */
	void setHudLayout(View hudLayout) {
		this.hudLayout = hudLayout;
	}

	/**
	 * Sets the title layout
	 * 
	 * @param titleLayout
	 *            title layout to set
	 */
	void setTitleLayout(View titleLayout) {
		this.titleLayout = titleLayout;
	}

	/**
	 * Sets the list view
	 * 
	 * @param listView
	 *            list view to set
	 */
	void setListView(UListView listView) {
		mContentsManager.setListView(listView);
		mContentsManager.updateListItems();
		mContentsManager.setListListeners();
	}

	/**
	 * Sets the current web view
	 * 
	 * @param webView
	 *            web view to set
	 */
	void setCurrentWebView(UWebView webView) {
		mPageManager.setCurrentWebView(webView);
	}

	/**
	 * Returns whether Hud menu is active or not
	 * 
	 * @return true if Hud menu is active.
	 */
	public boolean isHudMenuActive() {
		return mIsHUDMenu;
	}

	public void setViewFlipper(ViewFlipper viewFlipper) {
		mPageManager.setViewFlipper(viewFlipper);
	}

	public void setTempWebView(UWebView tempWebView) {
		mPageManager.setTempWebView(tempWebView);
	}

	/**
	 * Handles the backkey event.
	 * 
	 * @return
	 */
	boolean handleBackKeyEvent() {
		if (mIsHUDMenu) {
			if (mContentsManager.isContentsDisplayed()) {
				hideContents();
			} else if (mIsSearchDisplayed) {
				hideSearch();
			} else {
				updateLayout();
			}
			return true;
		}
		return false;
	}

	/**
	 * Clears the searchfield content
	 * 
	 * @return
	 */
	void clearSearch() {
		uSearchListView.clearSearch();
	}

	public float getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(float defaultFontSize) {

		this.defaultFontSize = defaultFontSize;
		updateWebViewFont();
	}

	/**
	 * Function to open the page, requested by user using "GoTo" functionality
	 * 
	 * @param page
	 */
	public void gotoPage(String page) {
		mPageManager.gotoPage(page);

	}

	/**
	 * Updates Hud Controls.
	 */
	void updateHud() {
		hideContents();
		hideSearch();
		if (!mIsHUDMenu) {
			hudLayout.setVisibility(View.VISIBLE);
			titleLayout.setVisibility(View.VISIBLE);
			mIsHUDMenu = true;
		} else {
			hudLayout.setVisibility(View.GONE);
			titleLayout.setVisibility(View.GONE);
			mIsHUDMenu = false;
			mContentsManager.hideVirtualKeypad();
		}
	}

	public ContentsManager getContentsManager() {
		return mContentsManager;
	}

	public boolean isContentDisplayed() {
		return mContentsManager.isContentsDisplayed();
	}

	public void setContentsDisplayed(boolean isContentsDisplayed) {
		mContentsManager.setContentsDisplayed(isContentsDisplayed);
	}

	/**
	 * Sets the Search List view
	 * 
	 * @param searchlistView
	 *            search list view to set
	 */
	void setSearchListView(USearchListView searchlistView) {
		uSearchListView = searchlistView;
	}

	/**
	 * Loads all the html pages in buffer.
	 */
	void ensureLoadPages() {
		uSearchListView.addPages(pages);
	}

	/**
	 * Sets the listeners for handling list events. These events include Title
	 * bar click and item click events.
	 */
	void setSearchListListeners() {
		uSearchListView
				.setListItemClickListener(new USearchListView.OnListItemClickListener() {
					@Override
					public void onListItemClicked(int pageNumber) {
						hideContents();
						hideSearch();
						sIsFromSearchList = true;
						UIManager.getInstance().navigate(pageNumber);
					}
				});

		uSearchListView
				.setTitleBarClickListener(new USearchListView.OnTitleBarClickListener() {
					@Override
					public void onTitleBarClicked() {
						hideContents();
						hideSearch();
					}
				});
	}

	/**
	 * Display the search results on the list
	 */
	private void showSearch() {
		if (!mIsSearchDisplayed) {
			hideContents();
			uSearchListView.showVirtualKeyboard();
			uSearchListView.setVisibility(View.VISIBLE);
			// mWebView.setSelected(false);
			// uListView.setSelected(false);
			searchButton.setBackgroundResource(R.drawable.navbtn_search_on);
			contentsButton.setBackgroundResource(R.drawable.navbtn_contents);
			mIsSearchDisplayed = true;
		} else {
			hideSearch();
		}

	}

	/**
	 * Hides the contents list.
	 */
	private void hideSearch() {

		searchButton.setBackgroundResource(R.drawable.navbtn_search);

		if (uSearchListView != null) {
			uSearchListView.setVisibility(View.GONE);
		}
		uSearchListView.hideVirtualKeyboard();
		mIsSearchDisplayed = false;
	}

	/**
	 * Returns the current web view
	 * 
	 * @return
	 */
	public WebView getCurrentWebView() {
		return mPageManager.getCurrentWebView();
	}

	/**
	 * Returns whether the application is sample application or not
	 * 
	 * @return true if application is Sample Application
	 */
	public boolean isSampleApp() {
		return mIsSampleApp;
	}

	/**
	 * Navigates the page to a section number
	 * 
	 * @param newPageNumber
	 *            section number to which page should navigate
	 */
	public void navigate(int newPageNumber) {
		mPageManager.navigate(newPageNumber);
	}

	/**
	 * Sets the scale. This scale would be used as initial scale for webview
	 * 
	 * @param scale
	 */
	public void setScale(int scale) {
		UIManager.scale = scale;
	}

	/**
	 * Returns the scale value. This scale would be used as initial scale for
	 * webview
	 * 
	 * @return scale value
	 */
	public int getScale() {
		return UIManager.scale;
	}

	public void clear() {
		uSearchListView.clear();
	}

	public void setMinimumFontSize(int minFont) {
		this.minFont = minFont;	
	}
	
	public int getMinimumFontSize(){
		return this.minFont;
	}

	public void setMaximumFontSize(int maxFont) {
		this.maxFont = maxFont;
	}
	
	public int getMaximumFontSize() {
		return this.maxFont;
	}
}
