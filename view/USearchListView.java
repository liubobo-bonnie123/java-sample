/**
 * File: $URL: $
 * Abstract: Manages the UI. This class handles all the events.
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 23rd Nov 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */
package com.forside.android.view;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.forside.android.Hades9780759519732HA.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.forside.android.Uniberg;
import com.forside.android.xml.NavPointData;
import com.forside.android.xml.NavPointMap;

/**
 * This class is used to display a search result list. Item of this list is
 * consist of Here text views are used to display the page number and metadata
 * of the line.
 * 
 * @author ScrollMotion
 * 
 */
public class USearchListView extends LinearLayout {
	private static final String SPECIAL_CHARS = "\\<[^>]*>";
	public static String searchText;
	private Context mContext;
	/**
	 * List title
	 */
	private USearchListTitleView title;
	/**
	 * Scrollable container for the list
	 */
	private ScrollView mScrollView;
	/**
	 * Container for the list items
	 */
	private LinearLayout container;
	/**
	 * Counter to maintain the list item's position index.
	 */
	private int count = (-1);
	/**
	 * Listens the onclick event on list item.
	 */
	private OnListItemClickListener listener;
	/**
	 * used for the tap listener for each row of the list
	 */
	private OnTapListener mOnTapListener;
	/**
	 * Listens the onclick event on the list title.
	 */
	private OnTitleBarClickListener titleBarClickListener;
	/**
	 * Buffer Storage for all the pages of the book
	 */
	private List<NavPointData> pages;

	/**
	 * Seach field edit box
	 */
	private EditText searchTextfileld = null;

	/**
	 * used to store the x co-ordinate value when first touch with the list is
	 * made
	 */
	private float preX;
	/**
	 * used to store the y co-ordinate value when first touch with the list is
	 * made
	 */
	private float preY;

	/**
	 * Listener for Tap event of List.
	 * 
	 * @author ScrollMotion
	 * 
	 */
	private String aesDecrytekey = null;

	/**
	 * Handler for UI rendering.
	 */
	private Handler mUiHandler = new Handler();

	/**
	 * Stores the previously searched string.
	 */
	private String mPreSearchedString;
	/**
	 * List to store the string which are already searched but not found.
	 */
	private List<String> mNotFoundStrings = new ArrayList<String>();

	/**
	 * To show a progress dialog while search is in progress.
	 */
	private ProgressDialog mProgressDialog;

	String decryptedData = null;
	Cipher cipher = null;
	byte fullkey[] = null;
	byte firsthalfkey[] = new byte[16];
	byte secondhalfkey[] = new byte[16];
	NavPointMap navMap = NavPointMap.getInstance();

	public interface OnTapListener {
		/**
		 * Handles the tap event.
		 */
		public void onTap(View v);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public USearchListView(Context context) {
		super(context);
		this.mContext = context;
		mScrollView = new ScrollView(mContext);
		/*
		 * mScrollView.setFocusable(true); mScrollView.setClickable(true);
		 * mScrollView.setFocusableInTouchMode(true);
		 * mScrollView.setVerticalFadingEdgeEnabled(false);
		 */
		enableTheScrollView();

		container = new LinearLayout(mContext);
		container.setOrientation(LinearLayout.VERTICAL);
		container.setFocusable(true);
		container.setClickable(true);
		container.setFocusableInTouchMode(true);
		container.setPadding(0, 0, 0, 0);

		setOrientation(LinearLayout.VERTICAL);
		RelativeLayout.LayoutParams paramsUU = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsUU.addRule(RelativeLayout.CENTER_VERTICAL);
		title = new USearchListTitleView(mContext);
		addView(title, paramsUU);
		title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				titleBarClickListener.onTitleBarClicked();
			}
		});
		mScrollView.addView(container, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams scrlParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		addView(mScrollView, scrlParams);

		setClickable(true);
		setFocusable(true);
		setFocusableInTouchMode(true);

	}

	public void addPages(List<NavPointData> pages) {
		this.pages = pages;
	}

	/**
	 * Adds an item to the list
	 * 
	 * @param pageNumber
	 *            Page Number
	 * @param lineMetaData
	 *            content of line
	 * @value "true" Listener is required
	 * 
	 */
	public void add(String chapterName, String pageNumber, String lineMetaData) {
		UCombinedTextView u = new UCombinedTextView(mContext, chapterName,
				pageNumber, lineMetaData, ++count, true);
		LinearLayout.LayoutParams paramsUU = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		container.addView(u, paramsUU);
	}

	/**
	 * 
	 * @param pageNumber
	 * @param lineMetaData
	 * @value "false" Listener is not required
	 * 
	 */
	public void addNoSearchResult(String pageNumber, String lineMetaData) {
		UCombinedTextView u = new UCombinedTextView(mContext, pageNumber, "",
				lineMetaData, ++count, false);
		LinearLayout.LayoutParams paramsUU = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		container.addView(u, paramsUU);
	}

	/**
	 * Sets a listener to listen onclick event on list title.
	 * 
	 * @param listener
	 */
	public void setTitleBarClickListener(OnTitleBarClickListener listener) {
		this.titleBarClickListener = listener;
	}

	/**
	 * Sets a listener to listen onclick event on list item.
	 * 
	 * @param listener
	 */
	public void setListItemClickListener(OnListItemClickListener listener) {
		this.listener = listener;
	}

	/**
	 * Listener to listen onclick event on list item
	 * 
	 * @author ScrollMotion
	 * 
	 */
	public interface OnListItemClickListener {
		void onListItemClicked(int itemIndex);
	}

	/**
	 * Listeber to listen onclick event on list's title bar
	 * 
	 * @author ScrollMotion
	 * 
	 */
	public interface OnTitleBarClickListener {
		void onTitleBarClicked();
	}

	/**
	 * This class is responsible to handle onclick and ontouch event of list
	 * items
	 * 
	 * @author ScrollMotion
	 * 
	 */
	public class OnItemClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v instanceof UCombinedTextView) {

				String pageNumber = ((UCombinedTextView) v).mpageNumber;
				/*
				 * int index = pageNumber.indexOf("#"); pageNumber =
				 * pageNumber.substring(index + 1);
				 */
				int pageNo = Integer.parseInt(pageNumber);
				onItemClicked(pageNo);
			}
		}
	}

	private void onItemClicked(int index) {
		if (listener != null && index != -1) {
			listener.onListItemClicked(index);
		}
	}

	/**
	 * This class represents an item in list.
	 * 
	 * @author ScrollMotion
	 * 
	 */
	private class UCombinedTextView extends RelativeLayout {

		private Context mContext;
		private String mpageNumber;
		private String mChapterName;
		private String mlineContent;
		/**
		 * Index of the item in a list
		 */
		private int itemIndex;

		private TextView contentLine = null;
		private TextView chapterName = null;
		private int pageNumFontColor = 0xff3399cc;
		/**
		 * This vector is used to store the indexes of search text appearing in
		 * content line.
		 */
		private Vector<Integer> indexes = new Vector<Integer>();

		public UCombinedTextView(Context context, String chapterName,
				String pageNumber, String lineContent, int itemIndex,
				boolean isListenersRequire) {
			super(context);
			this.mContext = context;
			this.mpageNumber = pageNumber;
			this.mChapterName = chapterName;
			this.mlineContent = lineContent;
			this.itemIndex = itemIndex;
			setBackgroundResource(R.drawable.cell_bg_search);
			setPadding(15, 5, 10, 10);
			setFocusable(true);
			setClickable(true);
			setFocusableInTouchMode(true);
			createView();

			if (isListenersRequire) {
				this.setItemClickListener();
				this.setTapListener();

			}

		}

		/**
		 * Function to set the click listener for each row (of the list)
		 * representing page number name and line meta data
		 */
		public void setItemClickListener() {
			UCombinedTextView.this
					.setOnClickListener(new OnItemClickListener());
		}

		/**
		 * Function to set the tap listener for each row (of the list)
		 * representing page number name and line meta data
		 */
		public void setTapListener() {
			UCombinedTextView.this
					.setOnTapListener(new USearchListView.OnTapListener() {

						@Override
						public void onTap(View v) {
							if (v instanceof UCombinedTextView) {
								String pageNumber = ((UCombinedTextView) v).mpageNumber;
								/*
								 * int index = pageNumber.indexOf("#");
								 * pageNumber = pageNumber.substring(index + 1);
								 */
								int pageNo = Integer.parseInt(pageNumber);
								onItemClicked(pageNo - 1);

							}
						}
					});
		}

		/**
		 * Function to initialize the mOnTapListener variable to the parameter
		 * passed to this method
		 * 
		 * @param onTapListener
		 */
		private void setOnTapListener(
				com.forside.android.view.USearchListView.OnTapListener onTapListener) {
			mOnTapListener = onTapListener;

		}

		/**
		 * This method checks whether the event is onTap or not
		 */
		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			// the first touch co-ordinates are set to these variables to
			// compare them with the co-ordinates
			// when the touch is released
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				UCombinedTextView.this.requestFocus();
				preX = ev.getRawX();
				preY = ev.getRawY();
			}
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				// when touch is released
				UCombinedTextView.this.requestFocus();
				float x = ev.getRawX();
				float y = ev.getRawY();

				if (((x >= (preX - 20)) && (x <= (preX + 20)))
						&& (y >= (preY - 5) && (y <= (preY + 5)))) {
					// the touch was a tap
					if (mOnTapListener != null) {
						mOnTapListener.onTap(UCombinedTextView.this);
					}
				}
			}
			return super.onTouchEvent(ev);
		}

		int getItemIndex() {
			return itemIndex;
		}

		private void createView() {
			chapterName = new TextView(mContext);
			
			chapterName.setLines(1);
			chapterName.setText(this.mChapterName);
			chapterName.setTextColor(pageNumFontColor);
			// chapterName.setTypeface(Typeface.DEFAULT_BOLD);
			chapterName.setTextSize(17);

			LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			// pageNumber.setId(1);
			addView(chapterName, params1);
			contentLine = new TextView(mContext);

			// contentLine.setText(this.mlineContent);
			contentLine.setTextColor(Color.WHITE);
			contentLine.setTextSize(12);
			contentLine.setLines(2);
			contentLine.setPadding(0, 25, 0, 0);
			SpannableString str = new SpannableString(mlineContent);

			int start = 0;
			int index;
			if (USearchListView.searchText != null
					&& (!USearchListView.searchText.equalsIgnoreCase(""))
					&& USearchListView.searchText.length() > 1) {
				final int LEN = USearchListView.searchText.length();
				String mLineContentLC = mlineContent.toLowerCase();

				index = mLineContentLC.indexOf(USearchListView.searchText,
						start);
				while (index != -1) {
					indexes.add(index);
					str.setSpan(new BackgroundColorSpan(0xff3399cc), index,
							index + LEN, 0);
					str.setSpan(new ForegroundColorSpan(Color.WHITE), index,
							index + LEN, 0);
					start = index + LEN;
					index = mLineContentLC.indexOf(USearchListView.searchText,
							start);
				}
			}
			contentLine.setText(str);
			LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			// contentLine.setId(2);
			addView(contentLine, params2);
		}

		@Override
		public void setSelected(boolean selected) {
			if (selected) {

				if (contentLine != null) {
					contentLine.setTextColor(Color.WHITE);
					setSpannedText(selected);
					pageNumFontColor = Color.WHITE;
					chapterName.setTextColor(pageNumFontColor);
					setBackgroundResource(R.drawable.cell_bg_search_tap);
				}

			} else {

				if (contentLine != null) {
					contentLine.setTextColor(Color.WHITE);
					setSpannedText(selected);
					pageNumFontColor = 0xff3399cc;
					chapterName.setTextColor(pageNumFontColor);
					setBackgroundResource(R.drawable.cell_bg_search);
				}

			}

			super.setSelected(selected);
		}

		/**
		 * Sets the spanned text. It highlights the searched text according to
		 * the list item is selected or not.
		 * 
		 * @param selected
		 *            true if the list item is selected.
		 */
		private void setSpannedText(boolean selected) {
			int bgColor = selected ? Color.WHITE : 0xff3399cc;
			int fgColor = selected ? 0xff3399cc : Color.WHITE;
			SpannableString str = new SpannableString(mlineContent);
			int index;
			final int LEN = USearchListView.searchText.length();
			for (int i = 0; i < indexes.size(); i++) {
				index = indexes.elementAt(i);
				str.setSpan(new BackgroundColorSpan(bgColor), index, index
						+ LEN, 0);
				str.setSpan(new ForegroundColorSpan(fgColor), index, index
						+ LEN, 0);
			}
			contentLine.setText(str);
		}

		@Override
		protected void onFocusChanged(boolean gainFocus, int direction,
				Rect previouslyFocusedRect) {

			setSelected(gainFocus);
			super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		}
	}

	/**
	 * This class represents the title of the List.
	 * 
	 * @author ScrollMotion
	 * 
	 */
	private class USearchListTitleView extends RelativeLayout {

		/**
		 * Title text font size.
		 */
		private static final int TITLE_TEXT_SIZE = 13;
		/**
		 * 
		 * Search filed width
		 */
		private static final int SEARCHFIELD_WIDTH = 265;

		USearchListTitleView(Context context) {
			super(context);
			setBackgroundResource(R.drawable.drag_bar_search);
			searchTextfileld = new EditText(context);
			searchTextfileld.setHint(R.string.search_field_hint);
			searchTextfileld.setTextSize(TITLE_TEXT_SIZE);
			// searchTextfileld.setTypeface(Typeface.DEFAULT_BOLD);
			searchTextfileld.setLines(1);
			searchTextfileld.setSingleLine();

			searchTextfileld
					.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus)
								searchTextfileld.setHint("");
							else
								searchTextfileld
										.setHint(R.string.search_field_hint);
						}
					});

			searchTextfileld.setFocusable(true);
			final Button goButton = new Button(context);
			goButton.setBackgroundResource(R.drawable.go_button);

			goButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					enableTheScrollView();
					try {
						hideVirtualKeyboard();
						Resources res = mContext.getResources();
						mProgressDialog = ProgressDialog.show(mContext, res
								.getString(R.string.searching), res
								.getString(R.string.please_wait));
						mProgressDialog.show();

						new Thread() {
							@Override
							public void run() {

								searchContent(searchTextfileld.getText()
										.toString());

								mUiHandler.post(new Runnable() {
									@Override
									public void run() {
										mProgressDialog.cancel();
									}
								});
							}
						}.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			// params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params2.addRule(RelativeLayout.CENTER_VERTICAL);
			params2.setMargins(0, 0, Utility.ConvertDPToPix(context,10), 0);

			 goButton.setId(2);
			addView(goButton, params2);
			
			
			LayoutParams params1 = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			params1.addRule(RelativeLayout.LEFT_OF, 2);
			params1.setMargins(Utility.ConvertDPToPix(context, 10),Utility.ConvertDPToPix(context, 3), Utility.ConvertDPToPix(context,5), 0);
			setPadding(0, 0, 0, 0);
			addView(searchTextfileld, params1);

		}

		/**
		 * searchContent : searches of the searchText in the list of .html
		 * files.
		 * 
		 * @param searchText
		 */
		private synchronized void searchContent(String searchText) {
			USearchListView.searchText = searchText.toLowerCase();
			if (mPreSearchedString != null
					&& mPreSearchedString.equals(USearchListView.searchText)) {
				return;
			}
			mPreSearchedString = USearchListView.searchText;

			String lineContent = null;
			String originalLine = null;

			final List<SearchItem> searchItemList = new ArrayList<SearchItem>();
			if (searchText != null && (!searchText.equalsIgnoreCase(""))
					&& searchText.length() > 1) {
				searchText = searchText.toLowerCase();
				// Return if string is already searched and not found.
				if (mNotFoundStrings.contains(searchText)) {
					return;
				}

				for (int page = 0; page < pages.size(); page++) {
					String fileName = ((NavPointData) pages.get(page))
							.getContentSrc();

					try {

						InputStream inputStream = mContext.getAssets().open(
								fileName);

						byte[] buffer = new byte[inputStream.available()];
						inputStream.read(buffer);
						inputStream.close();
						lineContent = getDecryptedData(buffer);

						if (lineContent != null) {
							// currentLine++;
							// get a line of text from the file
							/*
							 * try { lineContent = reader.readLine(); } catch
							 * (IOException e) { e.printStackTrace(); break; }
							 */
							// checks to see if the file ended
							// (reader.readLine()
							// returns null if the end is reached)
							/*
							 * if (lineContent == null) { break; } if
							 * (lineContent.length() == 0) { continue; }
							 */
							/*
							 * originalLine = lineContent; lineContent =
							 * lineContent.toLowerCase(); int index =
							 * lineContent.indexOf(searchText); if (index <= -1)
							 * { continue; } else {
							 */originalLine = lineContent;
							lineContent = Html.fromHtml(originalLine)
									.toString();
							originalLine = lineContent;
							lineContent = lineContent.toLowerCase();
							int index = lineContent.indexOf(searchText);
							if (index >= 0) {

								/*
								 * lineContent = lineContent.replaceAll(
								 * SPECIAL_CHARS, ""); // Remove Carriage return
								 * from java String lineContent =
								 * lineContent.replaceAll("\r", "");
								 * 
								 * // Remove New line from java string and
								 * replace html break lineContent =
								 * lineContent.replaceAll("\\<.*?\\>", "");
								 * lineContent = lineContent.replaceAll("\n",
								 * ""); lineContent =
								 * lineContent.replaceAll("\'", ""); lineContent
								 * = lineContent.replaceAll("\"", "");
								 * lineContent = lineContent.replaceAll("&#39",
								 * "");
								 */
								// lineContent = Html.fromHtml(lineContent)
								// .toString();
								// originalLine =
								// Html.fromHtml(originalLine)
								// .toString();
								// lineContent = originalLine.toLowerCase();
								lineContent = formatLineMetadata(originalLine,
										index);
								String pageNumber = ((NavPointData) pages
										.get(page)).getNavPointPlayOrder();
								String chapterName = ((NavPointData) pages
										.get(page)).getNavLabelText();
								chapterName = Html.fromHtml(chapterName)
										.toString();

								searchItemList.add(new SearchItem(chapterName,
										pageNumber, lineContent));

							} else
								continue;

						}

					} catch (FileNotFoundException e) {
						break;// for sample apps break the parse the HTML if
						// file not found exception occurs
						// removing all the epub pages which are behind chapter
						// one
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (searchItemList.size() <= 0) {
				if (searchText != null && searchText.length() > 0) {
					mNotFoundStrings.add(searchText);
				}
				showStringNotFoundAlert();
			} else {
				mUiHandler.post(new Runnable() {
					@Override
					public void run() {
						container.removeAllViews();
						for (int i = 0; i < searchItemList.size(); i++) {
							SearchItem item = searchItemList.get(i);
							add(item.mChapterName, item.mPageNumber,
									item.mLineContent);
						}
					}
				});
			}
		}

		/**
		 * Displays a string not found alert.
		 */
		private void showStringNotFoundAlert() {
			mOnTapListener = null;
			mUiHandler.post(new Runnable() {
				@Override
				public void run() {
					container.removeAllViews();
					Resources res = mContext.getResources();
					addNoSearchResult(res.getString(R.string.search_result),
							res.getString(R.string.match_not_found));
				}
			});
		}

		/**
		 * This class is a data structure which represent a search item.
		 * 
		 * @author ScrollMotion
		 * 
		 */
		class SearchItem {
			private String mChapterName;
			private String mPageNumber;
			private String mLineContent;

			public SearchItem(String chapterName, String pageNumber,
					String lineContent) {
				super();
				mChapterName = chapterName;
				mPageNumber = pageNumber;
				mLineContent = lineContent;
			}
		}

		/**
		 * formatLineMetadata : format the string lineContent.
		 * 
		 * @param lineContent
		 *            Meta data of the line
		 * @param index
		 *            Index from where the substring of the line would be
		 *            returned.
		 * @return substring of the meta-data
		 */
		private String formatLineMetadata(String lineContent, int index) {
			if (index > 10) {
				return lineContent.substring(index - 10);
			} else {
				return lineContent.substring(index);
			}
		}
	}

	/**
	 * Getting the decryption key from the root.json for full apps and from
	 * root_keycheck.json for sample apps
	 * 
	 * @return
	 */
	private byte[] getAESDecrytKey() {
		byte[] mdbytes = null;
		try {
			InputStream rsfile = null;
			String strLine = "";
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			int by = 256;
			if (navMap.getAppType().equalsIgnoreCase(NavPointMap.APP_FULL)) {
				rsfile = Uniberg.getInstance().getAssets().open("root.json");
			} else {
				// root_keycheck.json will be created by build script
				// dynamically, it will be used only for encrypted key check for
				// sample apps

				rsfile = Uniberg.getInstance().getAssets().open(
						"root_keycheck.json");
			}
			byte[] buffer = new byte[by];
			long read = 0;
			long offset = by;
			int size;
			while (read < offset) {
				size = (int) (((offset - read) >= by) ? by : (offset - read));
				rsfile.read(buffer, 0, size);
				read += size;
			}
			rsfile.close();
			strLine = new String(buffer);
			md.update(strLine.getBytes());
			mdbytes = md.digest();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return mdbytes;
	}

	/**
	 * Converts the encrypted data to decrypted data for each and every epubs
	 * page
	 * 
	 * @param encryptedData
	 * @return decrypted data
	 */
	private String getDecryptedData(byte encryptedData[]) {

		try {
			if (aesDecrytekey == null) {
				fullkey = getAESDecrytKey();
				aesDecrytekey = fullkey.toString();
				int k = 16;
				for (int i = 0; i < 16; i++) {

					firsthalfkey[i] = fullkey[i];
					secondhalfkey[i] = fullkey[k];
					k++;
				}
				SecretKeySpec skeySpec = new SecretKeySpec(firsthalfkey, "AES");
				cipher = Cipher.getInstance("AES/CBC/noPadding");

				AlgorithmParameterSpec paramSpec = new IvParameterSpec(
						secondhalfkey);

				cipher.init(Cipher.DECRYPT_MODE, skeySpec, paramSpec);
			}

			byte[] decrypted = cipher.doFinal(encryptedData);
			decryptedData = new String(decrypted);

		} catch (java.security.InvalidAlgorithmParameterException e) {
		} catch (javax.crypto.NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (java.security.InvalidKeyException e) {
			e.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedData;
	}

	/**
	 * Still setting focus for mScroolView
	 */
	private void enableTheScrollView() {
		mScrollView.setFocusable(true);
		mScrollView.setClickable(true);
		mScrollView.setFocusableInTouchMode(true);
		mScrollView.setVerticalFadingEdgeEnabled(false);
		mScrollView.setPadding(0, 0, 0, 0);
	}

	/**
	 * hiding the virtual keyboard once user click GO button
	 */
	public void hideVirtualKeyboard() {
		InputMethodManager inputManger = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManger.hideSoftInputFromWindow(this.searchTextfileld
				.getWindowToken(), 0);
	}

	/**
	 * Showing the virtual keyboard once user click search button
	 */
	public void showVirtualKeyboard() {
		InputMethodManager inputManger = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManger.showSoftInputFromInputMethod(this.searchTextfileld
				.getWindowToken(), 0);
	}

	public void clearSearch() {
		if (searchTextfileld != null)
			searchTextfileld.setText("");
	}

	public void clear() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
		}
	}
}
