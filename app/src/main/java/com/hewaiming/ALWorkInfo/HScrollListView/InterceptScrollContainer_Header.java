package com.hewaiming.ALWorkInfo.HScrollListView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class InterceptScrollContainer_Header extends LinearLayout {
	private static final String TAG = "InterceptScrollContainer_Header";

	public InterceptScrollContainer_Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public InterceptScrollContainer_Header(Context context) {
		super(context);
		
	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 * À¹½ØTouchEvent
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		Log.i(TAG,"onInterceptTouchEvent_Header");
		return false;
		//return super.onInterceptTouchEvent(ev);
	}
}
