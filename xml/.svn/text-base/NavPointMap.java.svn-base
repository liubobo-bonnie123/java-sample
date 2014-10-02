/**
 * File: $URL: $
 *
 * Abstract: Datastructre which represents an eBook.
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 15th Nov 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */

package com.forside.android.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the data structure for an e-book. It contains
 * title,author and all the Navigation Point Data for the e-book.
 * 
 * @author ScrollMotion
 */
public class NavPointMap {

	public static final String APP_FULL = "Full";
	public static final String APP_SAMPLE = "Sample";
	public static final String TYPE_AGENCY = "Agency";
	private static NavPointMap instance; // to make this class as Singleton
	private String docTitle;// to hold value of e-book title
	private String docAuthor;// to hold value of e-book author
	private String docPublisher; // to hold value of e-book publisher
	private String docType; // to hold value of document type;
	private String appType; // to hold value of application type

	private Map<Integer, NavPointData> map;// to hold value of all the
	// navigation points with key as
	// Play order(page number)
	private List<NavPointData> list;// to hold the list of navigation points
	private int chapterOneLocation = -1;//
	private int tenPercentOfBook = -1;

	public int getTenPercentOfBook() {
		return tenPercentOfBook;
	}

	public void setTenPercentOfBook(int tenPercentOfBook) {
		this.tenPercentOfBook = tenPercentOfBook;
	}

	/**
	 * Function to get the instance of this class
	 * 
	 * @return instance of this class
	 */
	public static NavPointMap getInstance() {
		if (instance == null)
			instance = new NavPointMap();
		return instance;
	}

	/**
	 * Constructor to initialize attributes of this class
	 */
	private NavPointMap() {
		docTitle = null;
		docAuthor = null;
		map = new HashMap<Integer, NavPointData>();
		list = new ArrayList<NavPointData>();
	}

	/**
	 * Function to get the Map holding key,value pairs of Navigation Point Data
	 * 
	 * @return
	 */
	public Map<Integer, NavPointData> getMap() {
		return this.map;
	}

	/**
	 * Function to get the list of object of navigation point data
	 * 
	 * @return
	 */
	public List<NavPointData> getList() {
		return list;
	}

	/**
	 * Function to get the document title
	 * 
	 * @return
	 */
	public String getDocTitle() {
		return docTitle;
	}

	/**
	 * Function to set the document title
	 * 
	 * @param docTitle
	 */
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	/**
	 * Function to set the value for map
	 * 
	 * @param navPointMap
	 */
	public void setMap(Map<Integer, NavPointData> navPointMap) {
		this.map = navPointMap;
	}

	/**
	 * Function to get the value of author of the document
	 * 
	 * @return
	 */
	public String getDocAuthor() {
		return docAuthor;
	}

	/**
	 * Function to set the value of author of this document
	 * 
	 * @param docAuthor
	 */
	public void setDocAuthor(String docAuthor) {
		this.docAuthor = docAuthor;
	}

	/**
	 * Function to get the value of publisher of the document
	 * 
	 * @return docPublisher
	 */
	public String getDocPublisher() {
		return docPublisher;
	}

	/**
	 * Function to set the value of publisher of this document
	 * 
	 * @param docPublisher
	 */
	public void setDocPublisher(String docPublisher) {
		this.docPublisher = docPublisher;
	}

	/**
	 * Function to get the value of DocType
	 * 
	 * @return docType
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * Function to set the value of DocType
	 * 
	 * @param docType
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * Function to get the value of application type
	 * 
	 * @return
	 */
	public String getAppType() {
		return appType;
	}

	/**
	 * Function to set the value of application type
	 * 
	 * @param appType
	 */
	public void setAppType(String appType) {
		this.appType = appType;
	}

	/**
	 * Function to release the memory used by object of this class
	 */
	public void clean() {
		docTitle = null;
		docAuthor = null;
		if (map != null) {
			map.clear();
			map = null;
		}
		if (list != null) {
			list.clear();
			list = null;
		}
		instance = null;
	}

	/**
	 * To store the chapter one location in sectionChildren of root.Json, for
	 * sample apps
	 * 
	 * @return
	 */
	public int getChapterOneLocation() {
		return chapterOneLocation;
	}

	public void setChapterOneLocation(int chapterOneLocation) {
		this.chapterOneLocation = chapterOneLocation;
	}
}
