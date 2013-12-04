package com.ruyicai.activity.buy.guess.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class MyViewFilper extends ViewFlipper {

	private float myStartX = -1; 
	public MyViewFilper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获得第一个点的x坐标
			myStartX = event.getX();
			break;

		case MotionEvent.ACTION_MOVE:
			//得到最后一个点的坐标
			float myLastX = event.getX() - myStartX;
			if (myLastX > 50) {
				showNext();
			} else if (myLastX < -50) {
				showPrevious();
			}
			Log.i("yejc", "=====myLastX="+myLastX);
			// 可以滑动删除的条件：横向滑动大于100个单位坐标，竖直差小于50个单位坐标
			break;

		case MotionEvent.ACTION_UP:
			myLastX = -1 ;
			break;
		}
		return super.onTouchEvent(event);
	}

}
