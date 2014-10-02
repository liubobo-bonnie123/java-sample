/**
 * File: $URL: $

 *
 * Abstract: JSON Parser
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 2nd Dec 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */
package com.forside.android.json;

import java.io.IOException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.forside.android.Uniberg;
import com.forside.android.xml.NavPointData;
import com.forside.android.xml.NavPointMap;

/**
 * This class is used to parse the JSON content.
 * 
 * @author ScrollMotion
 * 
 */
public class JSONParser {
	/**
	 * Instance to achieve singleton design pattern
	 */
	private static JSONParser mInstance = null;
	/**
	 * A string in JSON format which to be parsed.
	 */
	private String json;
	private JSONObject mJSONObject;


	/**
	 * Returns instance of the JSONParser.
	 * 
	 */
	static public JSONParser getInstance() {
		if (mInstance == null)
			mInstance = new JSONParser();
		return mInstance;
	}

	/**
	 * Constructor
	 */
	private JSONParser() {
		InputStream fRoot;
		try {
			fRoot = Uniberg.getInstance().getAssets().open("root.json");
			byte[] buffer = new byte[fRoot.available()];
			fRoot.read(buffer);
			json = new String(buffer);
			fRoot.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		json = json.substring(json.indexOf("{", 0), json.lastIndexOf("}") + 1);
		json = json.replace("\n", " ");

		try {
			mJSONObject = (JSONObject) new JSONTokener(json).nextValue();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private JSONArray find_stripPages() throws JSONException {
		JSONObject strips = mJSONObject.getJSONObject("strips");
		JSONArray _strip_root = strips.getJSONArray("_strip_root");
		JSONObject Strip = _strip_root.getJSONObject(1);
		JSONArray stripPages = Strip.getJSONArray("stripPages");

		return stripPages;
	}

	private static boolean JSONArrayContains(JSONArray array, String str,
			boolean caseSensitive) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			if (array.getString(i).compareTo(str) == 0) {
				return true;
			}
			if (!caseSensitive
					&& array.getString(i).compareToIgnoreCase(str) == 0) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Parses the JSON file
	 */
	public void parse() {
		NavPointMap navPointMap = NavPointMap.getInstance();
		Map<Integer, NavPointData> map = navPointMap.getMap();
		List<NavPointData> list = navPointMap.getList();
		String appType = null;

		NavPointData navPointData = null;
		JSONArray sectionDataArray = null;
		JSONObject sectionDataObject = null;
		JSONArray pagesDataArray = null;
		JSONObject pagesDataObject = null;

		try {
			/*
			 * get sections node.
			 */
			JSONObject sectionsObject = mJSONObject.getJSONObject("sections");
			JSONObject pagesObject = mJSONObject.getJSONObject("pages");
			JSONArray rootArray = sectionsObject.getJSONArray("root");
			JSONObject rootElementObject = rootArray.getJSONObject(1);

			// String sectionTitle =
			// rootElementObject.getString("sectionTitle");
			JSONArray sectionChildren = rootElementObject
					.getJSONArray("sectionChildren");
			JSONArray bookInfoArray = mJSONObject.getJSONArray("bookInfo");
			JSONObject bookInfoObject = bookInfoArray.getJSONObject(1);
			navPointMap.setDocAuthor(bookInfoObject.getString("bookAuthor"));
			navPointMap.setDocTitle(bookInfoObject.getString("bookTitle"));
			navPointMap.setDocPublisher(bookInfoObject
					.getString("bookPublisher"));
			appType = bookInfoObject.getString("bookApplicationType");
			navPointMap.setAppType(appType);
			navPointMap.setDocType(bookInfoObject.getString("bookType"));
			JSONArray stripPages = find_stripPages();
			int k = 0;
			for (int i = 0; i < stripPages.length(); i++) {
				// tocChk is the node name of one section, like 'cvi', 'c01'...
				String tocChk = stripPages.getString(i);

				// ignore title of content page
				if (tocChk.equalsIgnoreCase("toc")
						|| tocChk.equalsIgnoreCase("toc01")
						|| tocChk.equalsIgnoreCase("con01")
						|| tocChk.toLowerCase().contains("contents"))
					continue;
				navPointData = new NavPointData();
				// Check if this node also exists in Sections(Title of content)
				if (JSONArrayContains(sectionChildren, tocChk, true)) {
					navPointData.setInToC(true);

					/*
					 * the sectionDataArray looks like this: "Section", {
					 * "sectionCategories": [], "sectionTitle": "Cover",
					 * "sectionStartPage": "cvi", "sectionChildren": [],
					 * "sectionSubtitle": null, "sectionImage": null }
					 */
					if (!sectionsObject.isNull(tocChk)) {
						try {
							sectionDataArray = sectionsObject.getJSONArray(tocChk);
							sectionDataObject = sectionDataArray.getJSONObject(1);
							navPointData.setNavLabelText(sectionDataObject
									.getString("sectionTitle"));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}

				}
				// save the order in catalog
				navPointData.setNavPointPlayOrder(Integer.toString(k + 1));

				navPointData.setNavPointId(tocChk);

				/*
				 * get the section file definition from children nodes of
				 * "pages" The pagesDataArray definition like this:
				 * 
				 * "Page", { "pageTitle": "", "pageFilename":
				 * "OEBPS/Hant_9780375891960_epub_p03_r1.htm" }
				 */
				pagesDataArray = pagesObject.getJSONArray(tocChk);
				/*
				 * pagesDataObject look like this: "pageTitle": "",
				 * "pageFilename": "OEBPS/Hant_9780375891960_epub_p03_r1.htm"
				 */
				pagesDataObject = pagesDataArray.getJSONObject(1);
				// read file name
				String contentSrc = pagesDataObject.getString("pageFilename");
				if (contentSrc != null && !contentSrc.equals("")) {
					navPointData.setContentSrc(contentSrc);
				}

				// save this title record.
				map.put(list.size(), navPointData);

				list.add(navPointData);

				navPointData = null;
				k++;

			}
			if (appType.equalsIgnoreCase(NavPointMap.APP_SAMPLE))
				checkChapterOneLocation(stripPages, navPointMap);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * If app type is sample, this method will store the chapter one location in
	 * order to restrict user to navigate any page behind chapter one
	 * 
	 * @param sectionChildren
	 * @param navPointMap
	 */
	private void checkChapterOneLocation(JSONArray sectionChildren,
			NavPointMap navPointMap) {
		String navPointIds[] = { "c01", "c001", "c1", "c0001", "chap_1",
				"Chap_1", "chapter1", "chapter_1", "chapter_01", "chapter_001",
				"chapter_0001", "chapter-1", "chapter-01", "chapter-001",
				"chapter-0001", "chapter01", "chapter001", "chapter0001",
				"c00001", "ch01", "ch001", "ch0001", "ch1", "chap-1",
				"chap-01", "chap-001", "chap-0001", "chapter01-html",
				"chapter1-html", "TOCREF-1", "pt01ch01" };
		int k = 0;
		boolean isChapeterOneFound = false;
		for (int j = 0; j < sectionChildren.length(); j++) {
			try {
				String tocChk = sectionChildren.getString(j);
				tocChk = tocChk.toLowerCase();
				if (tocChk.equalsIgnoreCase("toc")
						|| tocChk.equalsIgnoreCase("toc01")
						|| tocChk.equalsIgnoreCase("con01")
						|| tocChk.contains("contents"))
					continue;
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 0; i < navPointIds.length; i++) {
				try {
					if (navPointIds[i].equalsIgnoreCase(sectionChildren
							.getString(j))) {
						navPointMap.setChapterOneLocation((k + 1));
						isChapeterOneFound = true;
						// chapterLimit = false;
						// return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			k++;
		}
		if (!isChapeterOneFound)
			calculateTenPercentOfBook(sectionChildren.length(), navPointMap);

	}

	/**
	 * If the book does not have any standard chapter representation(like
	 * c1,ch1, chapter-1 etc), this function will get call to calculate the %
	 * percentage to allow for user to navigate in case of sample app
	 * 
	 * @param length
	 *            is no of page contains in the book
	 * @param navPointMap
	 * 
	 */
	private void calculateTenPercentOfBook(int length, NavPointMap navPointMap) {
		int tenPercentOfBook = 0;
		if (length <= 6)
			tenPercentOfBook = (50 * length / 100);
		else if (length > 6 && length <= 10)
			tenPercentOfBook = (40 * length / 100);

		else if (length > 10 && length <= 20)
			tenPercentOfBook = (30 * length / 100);
		else
			tenPercentOfBook = (20 * length / 100);
		navPointMap.setTenPercentOfBook(tenPercentOfBook);

	}
}
