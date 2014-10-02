/**
 * File: $URL: $
 *
 * Abstract: Handler for Single-Touch events
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
 * This class is used to handle the touch events for Single Touch devices
 */
class SingleTouchEvent {
	protected MotionEvent event;

	protected SingleTouchEvent(MotionEvent event) {
		this.event = event;
	}

	public final int getAction() {
		return event.getAction();
	}

	public int getPointerCount() {
		return 1;
	}

	public int getPointerId(int pointerIndex) {
		verifyPointerIndex(pointerIndex);
		return 0;
	}

	public final float getX() {
		return event.getX();
	}

	public float getX(int pointerIndex) {
		verifyPointerIndex(pointerIndex);
		return getX();
	}

	public final float getY() {
		return event.getY();
	}

	public float getY(int pointerIndex) {
		verifyPointerIndex(pointerIndex);
		return getY();
	}

	private void verifyPointerIndex(int pointerIndex) {
		if (pointerIndex > 0) {
			throw new IllegalArgumentException("Invalid pointer index");
		}
	}

	static public SingleTouchEvent wrap(MotionEvent event) {
		try {
			return new MultiTouchEvent(event);
			
			
		} catch (VerifyError e) {
			return new SingleTouchEvent(event);
			
		}
	}
}
