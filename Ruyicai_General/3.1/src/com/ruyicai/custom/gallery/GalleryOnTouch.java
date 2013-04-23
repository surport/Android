package com.ruyicai.custom.gallery;

import com.ruyicai.activity.buy.BuyActivity;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;

public abstract class GalleryOnTouch implements OnTouchListener{
	FlingGallery mGallery;
	private static final int UP_INT = 15; 
	private static final int MOVE_INT = 40; 
	float newX = 0;//上次记录的位置
	float startX = 0;//开始的位置
	float moveX = 0;//移动过的距离
	boolean isMoveX = false;//是否移动过
	int isMax = 10;
	int startMax = 0;
	public GalleryOnTouch(FlingGallery mGallery){
		this.mGallery = mGallery;
	}
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		return onTouchClik(v, ev);
	}
	public boolean onTouchClik(View v, MotionEvent ev) {
		final int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			newX = ev.getX();
			isMoveX = false;
			moveX = 0;
		    startX = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			moveX += ev.getX() - startX;
			ev.setLocation(moveX+startX, ev.getY());// X移动
			if (Math.abs(moveX) > MOVE_INT) {
				isMoveX = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			float mX = Math.abs(moveX);
			if(mX<UP_INT&&!isMoveX){
				actionUp(v);
				return false;
			}
			break;
		}
		return mGallery.onGalleryTouchEvent(ev);
	}
	public abstract void actionUp(View v);
}

