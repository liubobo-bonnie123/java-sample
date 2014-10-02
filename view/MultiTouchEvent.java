/**
 * File: $URL: $
 *
 * Abstract: Handler for multi-touch events
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 7th Dec 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */
package com.forside.android.view;

import android.view.MotionEvent;

/**
 * This class is used to handle the touch events of multi-touch devices
 */
class MultiTouchEvent extends SingleTouchEvent {

	protected MultiTouchEvent(MotionEvent event) {
		super(event);
	}

	public float getX(int pointerIndex) {
		return event.getX(pointerIndex);
	}

	public float getY(int pointerIndex) {
		return event.getY(pointerIndex);
	}

	public int getPointerCount() {
		return event.getPointerCount();
	}

	public int getPointerId(int pointerIndex) {
		return event.getPointerId(pointerIndex);
	}
}
