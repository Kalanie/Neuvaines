package com.maya.neuvaines;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Sous typage d'un ScrollView de façon a pouvoir placer un listener pour
 * l'event scroll.
 * 
 * @link http://stackoverflow.com/questions/10316743/detect-end-of-scrollview
 * 
 */
public class ScrollViewExt extends ScrollView {
	private Runnable scrollerTask;
	private int initialPosition;

	private int newCheck = 100;

	private OnScrollStoppedListener onScrollStoppedListener;

	// Interface en inner class.
	public interface OnScrollStoppedListener {
		void onScrollStopped();
	}

	private ScrollViewListener scrollViewListener = null;

	public ScrollViewExt(Context context) {
		super(context);
	}

	public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollViewExt(Context context, AttributeSet attrs) {
		super(context, attrs);
		scrollerTask = new Runnable() {

			public void run() {

				int newPosition = getScrollY();
				if (initialPosition - newPosition == 0) {// has stopped

					if (onScrollStoppedListener != null) {

						onScrollStoppedListener.onScrollStopped();
					}
				} else {
					initialPosition = getScrollY();
					ScrollViewExt.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};
	}

	public void setOnScrollStoppedListener(
			ScrollViewExt.OnScrollStoppedListener listener) {
		onScrollStoppedListener = listener;
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	public void startScrollerTask() {

		initialPosition = getScrollY();
		ScrollViewExt.this.postDelayed(scrollerTask, newCheck);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

}
