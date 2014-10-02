/**
 * File: $URL: $
 *
 * Abstract: Manages the UI. This class is responsible to manage the pages.
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 29th Nov 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */

package com.forside.android;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.forside.android.Hades9780759519732HA.R;
import com.forside.android.view.USearchListView;
import com.forside.android.view.UWebView;
import com.forside.android.xml.NavPointData;
import com.forside.android.xml.NavPointMap;

/**
 * This class is responsible to display the pages and handle the events on them
 * 
 * @author ScrollMotion
 * 
 */
public class PageManager {

	/**
	 * Book's URL
	 */
	private static final String BOOK_URL = "file:///android_asset/OEBPS/";
	/**
	 * Mime type for the url.
	 */
	private static final String MIME_TYPE = "text/html";
	/**
	 * Default encoding.
	 */
	private static final String ENCODING = "UTF-8";
	/**
	 * Reference which points to the webview object which is displayed currently
	 */
	private UWebView mCurrentWebView;
	/**
	 * This reference variable is used as temporary variable.
	 */
	private UWebView mTempWebView;
	/**
	 * Flipper which contains two webviews
	 */
	private ViewFlipper mViewFlipper;
	/**
	 * Variable to track the current page index of the book, start from 1 not 0.
	 */
	private int pageNumber = 1;
	/**
	 * Variable to check whether to go next or previous.
	 */
	private boolean isNext;
	/**
	 * This variable is set to true if animation should take place while page
	 * change.
	 */
	private boolean mIsAnimationEnabled;
	/**
	 * This variable is set to true if page animation is running.
	 */
	private boolean isAnimating = false;

	/**
	 * Animation to slide in from left
	 */
	private Animation slideLeftIn;
	/**
	 * Animation to slide in from right
	 */
	private Animation slideRightIn;
	/**
	 * Animation to slide out from left
	 */
	private Animation slideLeftOut;
	/**
	 * Animation to slide out from right
	 */
	private Animation slideRightOut;

	private Context mContext;

	/**
	 * List containing all pages.
	 */
	private List<NavPointData> pages;
	/*
	 * Showing the message for sample when user navigate behind limit
	 */
	private Toast toastforSample;

	String navPointIds[] = { "c01", "c001", "c1", "c0001", "chap_1", "Chap_1",
			"chapter1", "chapter_1", "chapter_01", "chapter_001",
			"chapter_0001", "chapter-1", "chapter-01", "chapter-001",
			"chapter-0001", "chapter01", "chapter001", "chapter0001", "c00001",
			"ch01", "ch001", "ch0001", "ch1", "chap-1", "chap-01", "chap-001",
			"chap-0001", "chapter1-html", "chapter01-html", "TOCREF-1",
			"pt01ch01", "cvi", "cop", "cover" };
	NavPointMap navMap = NavPointMap.getInstance();
	
	int chapterOneLocation = -1;
	/*
	 * Cache pool for html page.
	 */
	PageCachePool mPagePool;
	/**
	 * Constructor
	 * 
	 * @param pages
	 * @param context
	 */
	PageManager(List<NavPointData> pages, Context context) {
		this.pages = pages;
		this.mContext = context;
		slideLeftIn = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_left_in);

		slideRightIn = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_right_in);

		slideRightOut = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_right_out);
		slideLeftOut = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_left_out);

		Animation.AnimationListener animListener = new Animation.AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimating = false;
				mCurrentWebView.setVerticalScrollBarEnabled(true);
				mCurrentWebView.setHorizontalScrollBarEnabled(true);
			}

			@Override
			public void onAnimationStart(Animation animation) {
				isAnimating = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		};
		slideLeftIn.setAnimationListener(animListener);
		slideRightIn.setAnimationListener(animListener);

		mPagePool = new PageCachePool(pages,context);
	}

	/**
	 * Updates the web view's font size
	 */
	void updateWebViewFont() {
		int fontSize = (int) UIManager.getInstance().getDefaultFontSize();

		mCurrentWebView.getSettings().setDefaultFontSize(fontSize);
		mTempWebView.getSettings().setDefaultFontSize(fontSize);

		mCurrentWebView.getSettings().setMinimumFontSize(
				UIManager.getInstance().getMinimumFontSize());
		// updateWebview(); // Resolved an issue related to font size change
		// (Ticket # 62)
	}

	void ApplyCustomizeStyle(WebView view) {
		
		view.getSettings().setJavaScriptEnabled(true);
		/**
		 * For some pages contain large image, which will cause horizontal
		 * scrollbar appear, so here to set the max width for images in all
		 * page.
		 */
		String sciptString = "javascript:(function() { "
				+ " var img = document.getElementsByTagName('img');"
				+ "for(var i=0; i < img.length; i++) {"
				+ "img[i].style.maxWidth = '%spx';}" + "})()";
		int maxWdith = 0;
		int scale = UIManager.getInstance().getScale();
		//To ensure no horizontal scroll bar appear.
		// zoom out
		if (scale < 100) {
			maxWdith = (Uniberg.ORIGNAL_PAGE_WIDTH - Uniberg.LEFT_MARGIN - Uniberg.RIGHT_MARGIN)
					* scale / 100;
		}
		// zoom in
		else {
			maxWdith = (this.mContext.getResources().getDisplayMetrics().widthPixels
					- Uniberg.LEFT_MARGIN - Uniberg.RIGHT_MARGIN)
					* 100 / scale;
		}

		sciptString = String.format(sciptString, maxWdith);
		view.loadUrl(sciptString);
	}

	/**
	 * Sets the Listeners of Web View. These listeners include Swipe and Tap
	 * events listener.
	 */
	void setWebViewListeners() {
		UWebView.OnSwipeListener swipeListener = new UWebView.OnSwipeListener() {
			public void onSwipe(int event) {
				if (!isAnimating) {
					switch (event) {
					case UWebView.OnSwipeListener.LEFT_TO_RIGHT:
						mTempWebView.setVerticalScrollBarEnabled(false);
						mCurrentWebView.setVerticalScrollBarEnabled(false);
						mCurrentWebView.setHorizontalScrollBarEnabled(false);
						mTempWebView.setHorizontalScrollBarEnabled(false);
						if (pageNumber < pages.size()) {
							pageNumber++;
							if (!isAllowToNavigate()) {
								showToastToBuyBook();
								pageNumber--;
								return;
							}
							isNext = true;
							mIsAnimationEnabled = true;
							changePage();
						}
						break;
					case UWebView.OnSwipeListener.RIGHT_TO_LEFT:
						mTempWebView.setVerticalScrollBarEnabled(false);
						mCurrentWebView.setVerticalScrollBarEnabled(false);
						mCurrentWebView.setHorizontalScrollBarEnabled(false);
						mTempWebView.setHorizontalScrollBarEnabled(false);
						if (pageNumber > 1) {
							pageNumber--;
							if (!isAllowToNavigate()) {
								showToastToBuyBook();
								pageNumber++;
								return;
							}
							isNext = false;
							mIsAnimationEnabled = true;
							changePage();
						}
						break;
					default:
						break;
					}
				}
			}
		};

		mCurrentWebView.setSwipeListener(swipeListener);
		mTempWebView.setSwipeListener(swipeListener);

		WebViewClient webViewClient = new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (view.equals(mCurrentWebView)) {
					// apply customize style like limiting image size.
					//Fix ticket #75
					ApplyCustomizeStyle(view);

					if (mIsAnimationEnabled) {

						mIsAnimationEnabled = false;
						if (isNext) {
							mViewFlipper.setInAnimation(slideLeftIn);
							mViewFlipper.setOutAnimation(slideLeftOut);
							mViewFlipper.showNext();

						} else {
							mViewFlipper.setInAnimation(slideRightIn);
							mViewFlipper.setOutAnimation(slideRightOut);
							mViewFlipper.showPrevious();
						}
					}
				}
				if (USearchListView.searchText != null
						&& UIManager.sIsFromSearchList) {
					UIManager.sIsFromSearchList = false;
					mCurrentWebView.getSettings().setJavaScriptEnabled(true);
					mCurrentWebView
							.loadUrl("javascript:(function() { "
									+ "var startTag = \"<font style='color:white; background-color:#3399cc;'>\";"
									+ "var endTag = \"</font>\";"
									+ "var searchString = \""
									+ USearchListView.searchText
									+ "\";"
									+ "var searchStringLC = searchString.toLowerCase();"
									+ "var oldBody = document.body.innerHTML;"
									+ "var newBody = \"\";"
									+ "var oldBodyLC = oldBody.toLowerCase();"
									+ "var i = -1;"
									+ "while (oldBody.length > 0) {"
									+ "	i = oldBodyLC.indexOf(searchStringLC, i + 1);"
									+ "	if (i < 0) {"
									+ "newBody += oldBody;"
									+ "oldBody = \"\";"
									+ "} else {"
									+ "if (oldBody.lastIndexOf(\">\", i) >= oldBody.lastIndexOf(\"<\", i)) {"
									+ "if (oldBodyLC.lastIndexOf(\"/script>\", i) >= oldBodyLC.lastIndexOf(\"<script\", i)) {"
									+ "newBody += oldBody.substring(0, i) + startTag+ oldBody.substr(i, searchString.length)+ endTag;"
									+ "oldBody = oldBody.substr(i + searchString.length);"
									+ "oldBodyLC = oldBody.toLowerCase();"
									+ "i = -1;"
									+ "}"
									+ "}"
									+ "}"
									+ "}"
									+ "document.body.innerHTML = newBody;"
									+ "return true;" + "})()");
				}
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (errorCode == ERROR_FILE_NOT_FOUND) {

					view.stopLoading();
					/**
					 * show the blank screen when the resource not found error
					 * occurs
					 */

					// view.loadData("<html></html>", "text/html", "utf-8");
					if (navMap.getAppType().equalsIgnoreCase(
							NavPointMap.APP_SAMPLE)) {
						showToastToBuyBook();
					}
				}
			}

		};

		mCurrentWebView.setWebViewClient(webViewClient);
		mTempWebView.setWebViewClient(webViewClient);

		UWebView.OnTapListener tapListener = new UWebView.OnTapListener() {
			@Override
			public void onTap() {
				UIManager.getInstance().updateHud();
			}
		};

		mCurrentWebView.setTapListener(tapListener);
		mTempWebView.setTapListener(tapListener);
	}

	/**
	 * Displays a toast containg a message to buy the book.
	 */
	void showToastToBuyBook() {
		int message;
		if (navMap.getDocType().equalsIgnoreCase(NavPointMap.TYPE_AGENCY))
			message = R.string.buy_alert;
		else
			message = R.string.store_alert;

		if (toastforSample == null) {
			toastforSample = Toast.makeText(mContext, mContext.getResources()
					.getString(message), 2000);

			toastforSample.setGravity(Gravity.CENTER, 0, 0);
		}

		toastforSample.show();

	}

	/**
	 * This method check to Navigate to requested chapter based on app type
	 * 
	 * @return true if it is allowed to navigate
	 */
	private boolean isAllowToNavigate() {
		/*
		 * if (!UIManager.getInstance().isSampleApp()) { return true; }
		 */
		if (navMap.getAppType().equalsIgnoreCase(NavPointMap.APP_FULL)) {
			return true;
		}
		chapterOneLocation = navMap.getChapterOneLocation();
		if (chapterOneLocation != -1) {
			if (Integer.parseInt(((NavPointData) pages.get(pageNumber-1))
					.getNavPointPlayOrder()) > (chapterOneLocation)) {
				return false;
			} else
				return true;
		} else if (chapterOneLocation == -1) { // book does not have standard
			// chapter representation(like
			// c1,ch1, chapter-1 etc), so
			// getting % of allow to user
			if (Integer.parseInt(((NavPointData) pages.get(pageNumber-1))
					.getNavPointPlayOrder()) > (navMap.getTenPercentOfBook())) {
				return false;
			} else
				return true;
		}

		/*
		 * String navPointId = ((NavPointData) pages.get(location))
		 * .getNavPointId();
		 * 
		 * if (!navPointId.toLowerCase().startsWith("c") ||
		 * navPointId.toLowerCase().startsWith("co")) { return true; }
		 * 
		 * for (int i = 0; i < navPointIds.length; i++) { if
		 * (navPointId.equals(navPointIds[i])) { return true; } }
		 */
		return false;
	}

	/**
	 * Navigates the page to a section number
	 * 
	 * @param newPageNumber
	 *            section number to which page should navigate
	 */
	public void navigate(int newPageNumber) {
		int preLocation = pageNumber;

		pageNumber = newPageNumber;
		if (!isAllowToNavigate()) {
			showToastToBuyBook();
			pageNumber = preLocation;
			return;
		}
		updateWebview();
	}

	/**
	 * Changes the page to the new location.
	 */
	synchronized void changePage() {
		while (isAnimating) {
			Thread.yield();
		}

		isAnimating = true;
		mCurrentWebView.setVerticalScrollBarEnabled(false);
		mTempWebView.setVerticalScrollBarEnabled(false);
		mCurrentWebView.setHorizontalScrollBarEnabled(false);
		mTempWebView.setHorizontalScrollBarEnabled(false);
		UWebView temp = mCurrentWebView;
		mCurrentWebView = mTempWebView;
		mTempWebView = temp;
		updateWebview();
	}

	int lastLoadedPageIndex = -1;
	/**
	 * Updates the Web View as per the current page to be displayed.
	 */
	void updateWebview() {
		if (toastforSample != null) {
			toastforSample.setDuration(0);
			toastforSample.cancel();
		}
		/*
		 * int index = htmlFile.indexOf(":"); String temp =
		 * htmlFile.substring(index+1); location =Integer.parseInt(temp);
		 * htmlFile = htmlFile.substring(0, index); //htmlFile = ((NavPointData)
		 * pages.get(location)).getContentSrc();
		 */
		// /mine begining
		/*
		 * try { SecretKeySpec skeySpec = new SecretKeySpec(key, "AES"); Cipher
		 * cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		 * cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
		 * 
		 * android.util.Log.d("TEST", "Start decoding...." +
		 * String.valueOf(length));
		 * 
		 * byte[] decrypted = cipher.doFinal(content);
		 * 
		 * File file2 = new
		 * File(Environment.getExternalStorageDirectory().getPath() +
		 * "/test.mp3"); OutputStream os = new FileOutputStream(file2);
		 * os.write(decrypted); } catch (Exception ex) { ex.printStackTrace(); }
		 * ///mine end
		 */if (mCurrentWebView == null) {
			((Uniberg) mContext).createWebView();
		}
		int pageIndex =mPagePool. getSafePageIndex(pageNumber);
		if(lastLoadedPageIndex == pageIndex)
			return;
		lastLoadedPageIndex = pageIndex;
		String str =mPagePool.GetDecrptyPage(pageIndex);
		if(str != null){
		//Clear the old data before loading new content, otherwise when swiping, user may see old content.
		mCurrentWebView.clearView();
		try {
			mCurrentWebView.loadDataWithBaseURL(BOOK_URL, str, MIME_TYPE,
					ENCODING, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		}
	}


	
	
	/**
	 * Function to open the page, requested by user using "GoTo" functionality
	 * 
	 * @param pageNumberToGo
	 */
	public void gotoPage(String pageNumberToGo) {
		if (pageNumberToGo != null && pageNumberToGo.length() > 0) {

			int newPageNumber = Integer.parseInt(pageNumberToGo);

			if (newPageNumber > pages.size() || newPageNumber <= 0) {
				Toast.makeText(mContext, "Page not found.", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			int preLocation = pageNumber;
			pageNumber = newPageNumber;
			if (!isAllowToNavigate()) {
				showToastToBuyBook();
				pageNumber = preLocation;
				return;
			}

			if (pageNumber > preLocation) {
				isNext = true;
			} else {
				isNext = false;
			}
			mIsAnimationEnabled = true;

			UIManager.getInstance().hideContents();
			UIManager.getInstance().getContentsManager().hideVirtualKeypad();
			changePage();

		} else { // show message to the user to enter value for page number to
			// go to a specific page.
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.field_empty_alert), Toast.LENGTH_LONG)
					.show();
		}

	}

	/**
	 * Sets flipper view
	 * 
	 * @param viewFlipper
	 */
	public void setViewFlipper(ViewFlipper viewFlipper) {
		this.mViewFlipper = viewFlipper;
	}

	/**
	 * Sets the temporary webview, which would be used for page animation
	 * 
	 * @param tempWebView
	 */
	public void setTempWebView(UWebView tempWebView) {
		this.mTempWebView = tempWebView;
		this.mTempWebView.setHorizontalScrollBarEnabled(false);
		this.mTempWebView
				.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);

	}

	/**
	 * Sets webview which is to be displayed currently
	 * 
	 * @param webView
	 */
	public void setCurrentWebView(UWebView webView) {
		this.mCurrentWebView = webView;

		this.mCurrentWebView.setHorizontalScrollBarEnabled(false);
		this.mCurrentWebView
				.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);
	}

//	/**
//	 * Sets the page location
//	 * 
//	 * @param pageNumber
//	 */
//	public void setPageNumber(int pageNumber) {
//		PageManager.pageNumber = pageNumber;
//	}

	public UWebView getCurrentWebView() {
		return mCurrentWebView;

	}

}
