package com.forside.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.forside.android.xml.NavPointData;

public class PageCachePool {

	private final List<NavPointData> pages;

	private final ArrayList<Integer> cacheOffsetToCurrentPage ;
	final String Tag = "PageCachePool";
	DecryptionWorker mDecryptorDecryptionWorker;
	/*
	 * Cache decrypted pages. when loading one page, we will try to decrypt the
	 * next page and previous page as well. key: pageIndex Value: plain text
	 */
	HashMap<Integer, String> mDecryptedPages = new HashMap<Integer, String>();

	public PageCachePool(List<NavPointData> pages, Context context) {
		this.pages = pages;
		mDecryptorDecryptionWorker = new DecryptionWorker(context);
		cacheOffsetToCurrentPage = new ArrayList<Integer>();

		cacheOffsetToCurrentPage.add(-1);//previous page
		cacheOffsetToCurrentPage.add(0);//current page
		cacheOffsetToCurrentPage.add(1);//next page
		cacheOffsetToCurrentPage.add(2);//next page
		cacheOffsetToCurrentPage.add(3);//next page
		
	}

	public String GetDecrptyPage(int pageIndex) {
		synchronized (mDecryptedPages) {

			String resultString = mDecryptedPages.get(pageIndex);
			if (resultString == null) {
				resultString = this.mDecryptorDecryptionWorker
						.DecrptyPage(((NavPointData) pages.get(pageIndex))
								.getContentSrc());
				if (resultString == null) {
					// Error happened.
					return resultString;
				}
				// cache the current page.
				mDecryptedPages.put(pageIndex, resultString);
			}
			else {
				Log.i(Tag, String.format("Page index %d was hit in cache",pageIndex ));
			}
			OnPageAccessed(pageIndex);
			return resultString;
		}

	}

	private void OnPageAccessed(final int pageIndex) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				synchronized (mDecryptedPages) {
					// try cache next or previous pages, and delete other cache
					// entries.
					Object[] keys = mDecryptedPages.keySet().toArray();
					for (int i = 0; i < keys.length; i++) {
						int key = Integer.parseInt(keys[i].toString());
						//the offset to current page
						int pageOffset =  key - pageIndex;
						
						if (cacheOffsetToCurrentPage.contains(pageOffset))
							continue;
						// remove other pages.
						mDecryptedPages.remove(keys[i]);
						Log.d(Tag, String.format("Page index %d was removed from cache",key ));
					}
					loadCache(pageIndex);
					
				}
			}

			private void loadCache(final int pageIndex) {
				for (int pageOffset : cacheOffsetToCurrentPage) {
					
					int pageIndexToLoad = pageIndex+pageOffset;
					if (pageIndexToLoad < 0) {
						pageIndexToLoad = 0;
					}
					if (pageIndexToLoad > pages.size() - 1) {
						pageIndexToLoad = pages.size() - 1;
					}
					//load cache page
					if (!mDecryptedPages.containsKey(pageIndexToLoad)) {
						
						PreLoadPage(pageIndexToLoad);
					}	
				}
			}
		}).start();
	}

	private void PreLoadPage(final int pageIndex) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.d(Tag, String.format("Starting loading page index %d to cache",pageIndex ));
				String resultString = mDecryptorDecryptionWorker
						.DecrptyPage(((NavPointData) pages.get(pageIndex))
								.getContentSrc());
				if (resultString == null) {
					// Error happened.
					return;
				}
				// cache the current page.
				synchronized (mDecryptedPages) {
					if (!mDecryptedPages.containsKey(pageIndex)) {
						mDecryptedPages.put(pageIndex, resultString);
						Log.d(Tag, String.format("Loaded page index %d to cache",pageIndex ));
					}
				}

			}
		}).start();
	}

	public int getSafePageIndex(int pageNumber) {
		// ensure the page index is not out of range.
		int pageIndex = pageNumber - 1;
		if (pageIndex > pages.size() - 1) {
			pageIndex = pages.size() - 1;
		} else if (pageIndex < 0) {
			pageIndex = 0;
		}
		return pageIndex;
	}

}
