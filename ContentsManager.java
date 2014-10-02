/**
 * File: $URL: $

 *
 * Abstract: Manages the UI. This class is responsible to display and handle Contents List.
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
import android.text.Html;
import android.view.View;

import com.forside.android.view.UListView;
import com.forside.android.xml.NavPointData;

/**
 * This class is responsible to manage Contents List.
 * 
 * @author ScrollMotion
 * 
 */
public class ContentsManager {

	/**
	 * A list to display book's contents
	 */
	private UListView uListView;
	/**
	 * This list contains the all pages information of the book.
	 */
	private List<NavPointData> pages;

	/**
	 * To check whether Contents list is displayed or not.
	 */
	private boolean mIsContentsDisplayed;

	/**
	 * Constructor
	 * 
	 * @param pages
	 * @param context
	 */
	ContentsManager(List<NavPointData> pages, Context context) {
		this.pages = pages;
	}

	/**
	 * Sets the listeners for handling list events. These events include Title
	 * bar click and item click events.
	 */
	void setListListeners() {
		uListView
				.setListItemClickListener(new UListView.OnListItemClickListener() {
					@Override
					public void onListItemClicked(int pageNumber) {
						UIManager.getInstance().updateContentsButtonBackground(
								false);
						hide();
						UIManager.getInstance().navigate(pageNumber);
					}
				});

		uListView
				.setTitleBarClickListener(new UListView.OnTitleBarClickListener() {
					@Override
					public void onTitleBarClicked() {
						UIManager.getInstance().updateContentsButtonBackground(
								false);
						hide();
					}
				});

		uListView.setOnGoEventListener(new UListView.OnGoEventListener() {
			@Override
			public void onEvent(String text) {
				UIManager.getInstance().gotoPage(text);
			}
		});
	}

	/**
	 * Hides the contents list.
	 */
	void hide() {
		if (uListView != null) {
			uListView.setVisibility(View.GONE);
			uListView.hideVirtualKeypad();
		}
		mIsContentsDisplayed = false;
	}

	/**
	 * Displays the contents list.
	 */
	void show() {
		UIManager.getInstance().updateContentsButtonBackground(true);
		uListView.setVisibility(View.VISIBLE);
		mIsContentsDisplayed = true;
	}

	/**
	 * Updates the contents list items.
	 */
	void updateListItems() {
		for (int i = 0; i < pages.size(); i++) {
			if(! pages.get(i).isInToC())
				continue;
			String chapterName = ((NavPointData) pages.get(i))
					.getNavLabelText();
			chapterName = Html.fromHtml(chapterName).toString();
			
			
		
			/*
			 * String pageNumber = "Sec #" + ((NavPointData)
			 * pages.get(i)).getNavPointPlayOrder();
			 */
			/*String pageNumber = (Integer.parseInt(((NavPointData) pages.get(i))
					.getNavPointPlayOrder()) + 1)
					+ "";*/
			String pageNumber = (Integer.parseInt(((NavPointData) pages.get(i))
					.getNavPointPlayOrder()) )
					+ "";
			uListView.add(chapterName, pageNumber);

		}
	}

	/**
	 * Sets the list view
	 * 
	 * @param listView
	 *            list view to set
	 */
	void setListView(UListView listView) {
		uListView = listView;
	}

	/**
	 * Returns whether the contents list is displayed or not.
	 * 
	 * @return true if contents list is displayed.
	 */
	boolean isContentsDisplayed() {
		return mIsContentsDisplayed;
	}

	/**
	 * Sets whether to display contents or not
	 * 
	 * @param isContentsDisplayed
	 *            true to display the contents list
	 */
	public void setContentsDisplayed(boolean isContentsDisplayed) {
		this.mIsContentsDisplayed = isContentsDisplayed;
	}

	/**
	 * Hides the virtual keypad.
	 */
	public void hideVirtualKeypad() {
		uListView.hideVirtualKeypad();
	}

}
