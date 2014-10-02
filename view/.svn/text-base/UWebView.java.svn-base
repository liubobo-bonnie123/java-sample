/**
 * File: $URL: $
 *
 * Abstract: A webview which generates swipe and tap events. 
 *
 * Documents: A reference to the applicable design documents.
 *
 * Author: ScrollMotion
 * 
 * Date: 10th Nov 2010
 *
 * $Id: $
 *
 * Copyright Notice
 *
 */
package com.forside.android.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.forside.android.UIManager;

/**
 * Custom WebView which handles the Left to Right and Right to Left swipe
 * events.
 * 
 * @author ScrollMotion
 * 
 */
public class UWebView extends WebView {
	/**
	 * Swipe is generated if the movement distance is greater than or equal to
	 * this value.
	 */
	// private static final int SWIPE_MINIMUM = 120;
	// private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_MINIMUM = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;

	/**
	 * Minimum horizontal move distance value to generate swipe event.
	 */
	private static final int DX = 60;
	/**
	 * Maximum vertical move distance value to generate swipe event.
	 */
	private static final int DY = 2;

	/**
	 * Delta factor which would be used in implementation of Zoom-In/Out
	 * functionality.
	 */
	private static float DELTA = 35;

	private static final int NONE = 0;
	private static final int DRAG = NONE + 1;
	private static final int ZOOM = DRAG + 1;

	private int mode = NONE;

	private float preX;
	private float preY;
	private float oldDist = 1f;

	/**
	 * Variable to check whether scroll has changed or not. Swipe event will be
	 * generated only if the scroll has not changed.
	 */
	private boolean mIsScrollChanged = false;

	private GestureDetector mGestureDetector;

	private View.OnTouchListener mOnTouchListener;

	private OnSwipeListener mOnSwipeListener;

	private OnTapListener mOnTapListener;

	/**
	 * Singleton instance of UIManager.
	 */
	private UIManager mUIManager = UIManager.getInstance();
	SharedPreferences myPrefs = PreferenceManager
			.getDefaultSharedPreferences(this.getContext());

	public UWebView(Context context) {

		super(context);
//		this.setWebViewClient(new WebViewClient());

		// this.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		mGestureDetector = new GestureDetector(new WebViewGestureListener());
		mOnTouchListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (mGestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};

		setOnTouchListener(mOnTouchListener);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
	}

	/**
	 * Sets the listener to listen swipe events.
	 * 
	 * @param mOnSwipeListener
	 */
	public void setSwipeListener(OnSwipeListener mOnSwipeListener) {
		this.mOnSwipeListener = mOnSwipeListener;

	}

	/**
	 * Sets the listener to listen tap events.
	 * 
	 * @param mOnSwipeListener
	 */
	public void setTapListener(OnTapListener mOnTapListener) {
		this.mOnTapListener = mOnTapListener;

	}

	/**
	 * Gesture listener for WebView. This class extends SipleOnGestureListener
	 * class and overrides onFling method to generate swipe events.
	 * 
	 * @author ScrollMotion
	 * 
	 */
	private class WebViewGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (mIsScrollChanged) {
				mIsScrollChanged = false;
				return false;
			}
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				if (e1.getX() - e2.getX() > SWIPE_MINIMUM
				/** && Math.abs(velocityX) > SWIPE_VELOCITY */
				) {
					if (mOnSwipeListener != null) {
						mOnSwipeListener.onSwipe(OnSwipeListener.LEFT_TO_RIGHT);
					}
				} else if (e2.getX() - e1.getX() > SWIPE_MINIMUM
				/** && Math.abs(velocityX) > SWIPE_VELOCITY */
				) {
					if (mOnSwipeListener != null) {
						mOnSwipeListener.onSwipe(OnSwipeListener.RIGHT_TO_LEFT);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Listener for Swipe events.
	 * 
	 * @author ScrollMotion
	 * 
	 */
	public interface OnSwipeListener {
		/**
		 * Constant to indicate the Left to Right swipe event.
		 */
		int LEFT_TO_RIGHT = 0;
		/**
		 * Constant to indicate the Right to Left swipe event.
		 */
		int RIGHT_TO_LEFT = LEFT_TO_RIGHT + 1;

		/**
		 * Handles the swipe event.
		 * 
		 * @param event
		 *            Swipe event type.
		 */
		public void onSwipe(int event);
	}

	/**
	 * Listener for Tap events.
	 * 
	 * @author ScrollMotion
	 * 
	 */
	public interface OnTapListener {
		/**
		 * Handles the tap event.
		 */
		public void onTap();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		mIsScrollChanged = false;
		SingleTouchEvent wrappedEvent = SingleTouchEvent.wrap(ev);
		
		switch (wrappedEvent.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			preX = ev.getRawX();
			preY = ev.getRawY();

			mode = DRAG;
			break;

		case MotionEvent.ACTION_UP:
			
			float x = ev.getRawX();
			float y = ev.getRawY();
			/* float f= ev.getX();
			 if(f < 100)
				 mOnSwipeListener.onSwipe(OnSwipeListener.RIGHT_TO_LEFT);	
			 if(f > 200)
				 mOnSwipeListener.onSwipe(OnSwipeListener.LEFT_TO_RIGHT);
			 if(f >=100 && f <=200 )
			 {*/
			if (((x >= (preX - DX)) && (x <= (preX + DX)))
					&& (y >= (preY - DY) && (y <= (preY + DY)))) {
				if (mOnTapListener != null) {
					mOnTapListener.onTap();
				}
			}
			 //}
			mode = NONE;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			oldDist = 1f;
			break;

		case MotionEvent.ACTION_MOVE:

			Boolean isLockEnable = myPrefs.getBoolean("lock", false);
			if (mode == ZOOM && !isLockEnable) {

				float newDist = distance(wrappedEvent);
				float fontSize = mUIManager.getDefaultFontSize();

				if (newDist > 10f
						&& ((newDist <= (oldDist - DELTA)) || (newDist >= (oldDist + DELTA)))) {
					float scale = newDist / oldDist;
					final int fontChangeDistance = 5;//change font size +- 5 each time.
					if (scale < 1) {
						
						int minimumFontSize = mUIManager.getMinimumFontSize();
						if (fontSize > minimumFontSize) {// Restrict the user to not minimize
											// the font less then 20
											// pixels(minimum font)
							int font = (int) mUIManager.getDefaultFontSize() - fontChangeDistance;
							if (font < minimumFontSize)
								mUIManager.setDefaultFontSize(minimumFontSize);
							else
								mUIManager.setDefaultFontSize(font);
							

							//zoomOut();
							oldDist = newDist;

						}
					}
					else if (scale > 1) {

						int maximumFontSize = mUIManager.getMaximumFontSize();
						if (fontSize < maximumFontSize) {// Restrict the user not to maximize
											// the font more then 50
											// pixels(maximum font)
							int font = (int) mUIManager.getDefaultFontSize() + fontChangeDistance;
							if (font > maximumFontSize)
								mUIManager.setDefaultFontSize(maximumFontSize);
							else
								mUIManager.setDefaultFontSize(font);
							//zoomIn();
							oldDist = newDist;
						}

					}
					
				}
				//return true to stop super class continue process this message. 
				return true;
			}
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = distance(wrappedEvent);
			//to zoom in/out screen, the pointer count must be greater than 1.
			//which can fix the below bug: 
			//reproduce steps:
			//1, slide the screen, so the view will scroll up or down
			//2, keep sliding to a long distance, so the flag 'mode' will be set to ZOOM in ACTION_POINTER_DOWN event.
			//3, keep sliding, and the font size may be changed in ACTION_MOVE event.
			if (oldDist > 10f && ev.getPointerCount()>1) {
				mode = ZOOM;
			}
			break;

		}

		return super.onTouchEvent(ev);

	}

	private float distance(SingleTouchEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		mIsScrollChanged = true;
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
