/**
 * File: $URL: $
 *
 * Abstract: XML parser
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

import java.io.IOException;


import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

/**
 * This class is used to parse the xml file containing title,author and
 * navigation points of e-books
 * 
 * @author ScrollMotion
 */
public class UnibergXMLParser {

	public Context context;

	/**
	 * Function to parse the xml file
	 */
	public void parse() {

		NavPointMap navPointMap = NavPointMap.getInstance();
		Map<Integer, NavPointData> map = navPointMap.getMap();
		List<NavPointData> list = navPointMap.getList();
		NavPointData navPointData = null;
		String attrVal = null;
		InputStream stream;
		String xmlString = new String();
		try {

			stream = context.getAssets().open(
					"Bras_9780385738132_epub_ncx_r1.ncx");

			byte[] buffer = new byte[stream.available()];
			stream.read(buffer);
			xmlString = new String(buffer);
			xmlString = xmlString.replace("\n", "");
			xmlString = xmlString.replace("\t", "");
			stream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			XmlPullParserFactory xppf = null;
			try {
				xppf = XmlPullParserFactory.newInstance();
			} catch (XmlPullParserException e) {
				throw e;
			}
			xppf.setNamespaceAware(true);

			XmlPullParser xpp = null;
			try {
				xpp = xppf.newPullParser();
			} catch (XmlPullParserException e) {
				throw e;
			}
			try {
				xpp.setInput(new StringReader(xmlString));
			} catch (XmlPullParserException e) {
				throw e;
			}
			int eType = 0;
			try {
				eType = xpp.getEventType();
			} catch (XmlPullParserException e) {
				throw e;
			}
			String startTag = null;
			String prevTag = null;
			String endTag = null;
			while (eType != XmlPullParser.END_DOCUMENT)// loop till end of
			// response is not
			// reached
			{
				if (eType == XmlPullParser.START_DOCUMENT) // if the control is
				// at start of
				// response
				{

				} else if (eType == XmlPullParser.START_TAG) // if the control
				// is at the
				// start of a
				// tag
				{
					startTag = xpp.getName().toString();

					if (!startTag.equals("text")) {
						prevTag = startTag;
					}
					if (startTag.equals("navPoint")) {
						navPointData = new NavPointData();
						int count = xpp.getAttributeCount();
						for (int i = 0; i < count; i++) {
							attrVal = xpp.getAttributeValue(i);

							if (i == 0) {

								navPointData.setNavPointClass(attrVal);
							} else if (i == 1) {
								navPointData.setNavPointId(attrVal);
							} else if (i == 2) {

								navPointData.setNavPointPlayOrder(attrVal);
							}
						}
					} else if (startTag.equals("content")) {
						attrVal = xpp.getAttributeValue(0);

						if (navPointData != null)
							navPointData.setContentSrc(attrVal);
					}
				} else if (eType == XmlPullParser.TEXT) // if control is at data
				{
					String data = xpp.getText().toString();
					if (startTag.equals("text")) {
						if (prevTag.equals("docTitle")) {
							navPointMap.setDocTitle(data);
							prevTag = " ";
						} else if (prevTag.equals("docAuthor")) {
							navPointMap.setDocAuthor(data);
							prevTag = " ";
						} else if (prevTag.equals("navLabel")) {
							if (navPointData != null)
								navPointData.setNavLabelText(data);
							prevTag = " ";
						}
					}

				} else if (eType == XmlPullParser.END_TAG)// if control is at
				// end of a tag.
				{
					endTag = xpp.getName().toString();
					if (endTag.equals("navPoint")) {
						if (navPointData != null
								&& navPointData.getNavPointPlayOrder() != null) {
							map.put(Integer.parseInt(navPointData
									.getNavPointPlayOrder()), navPointData);
							list.add(navPointData);
						}
						navPointData = null;
					}

				}
				try {
					eType = xpp.next();
				} catch (XmlPullParserException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				}
			}
			xppf = null;
			xpp = null;
			startTag = null;

		} catch (Exception e) {
		}
	}
}
