package com.ruyicai.custom.drawer;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

// TODO:

// 1. In order to improve performance Cache screen bitmap and use for animation
// 2. Establish superfluous memory allocations and delay or replace with reused objects
//	  Probably need to make sure we are not allocating objects (strings, etc.) in loops

/**
 * 类似抽屉类
 * @author Administrator
 *
 */

public class DrawerGallery extends FrameLayout {
	// Constants

	// private final int swipe_min_distance = 120;
	private final int velocity_X = 1500 ;//水平滑动引擎值
	private final int swipe_max_off_path = 100;//判断是否上下滑动临界值
	private final int swipe_min_distance = 40;
	private final int swipe_threshold_veloicty = 400;

	// Properties

	private int mViewPaddingWidth = 0;
	private int mAnimationDuration = 300;
	private float mSnapBorderRatio = 0.5f;
	private boolean mIsGalleryCircular = true;

	// Members

	private int mGalleryWidth = 0;
	private boolean mIsTouched = false;
	private boolean mIsDragging = false;
	private float mCurrentOffset = 0.0f;
	private long mScrollTimestamp = 0;
	private int mFlingDirection = 0;
	private int mCurrentPosition = 0;

	private Context mContext;
	private FlingGalleryView[] mViews;
	private FlingGalleryAnimation mAnimation;
	private GestureDetector mGestureDetector;
	private Interpolator mDecelerateInterpolater;
    private int mWith = 0;
    boolean isAction = true;
	public DrawerGallery(Context context) {
		super(context);
		mContext = context;
		mViews = new FlingGalleryView[2];
		mViews[0] = new FlingGalleryView(0, this);
		mViews[1] = new FlingGalleryView(1, this);
		mAnimation = new FlingGalleryAnimation();
		mGestureDetector = new GestureDetector(new FlingGestureDetector());
		mDecelerateInterpolater = AnimationUtils.loadInterpolator(mContext,android.R.anim.decelerate_interpolator);
	}

	public DrawerGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;

		mViews = new FlingGalleryView[2];
		mViews[0] = new FlingGalleryView(0, this);
		mViews[1] = new FlingGalleryView(1, this);

		mAnimation = new FlingGalleryAnimation();
		mGestureDetector = new GestureDetector(new FlingGestureDetector());
		mDecelerateInterpolater = AnimationUtils.loadInterpolator(mContext,
				android.R.anim.decelerate_interpolator);
	}

	public DrawerGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;

		mViews = new FlingGalleryView[2];
		mViews[0] = new FlingGalleryView(0, this);
		mViews[1] = new FlingGalleryView(1, this);

		mAnimation = new FlingGalleryAnimation();
		mGestureDetector = new GestureDetector(new FlingGestureDetector());
		mDecelerateInterpolater = AnimationUtils.loadInterpolator(mContext,android.R.anim.decelerate_interpolator);
	}
    public void setViews(View view1,View view2){
    	mViews[0].addFrameLayoutView(view1);
    	mViews[1].addFrameLayoutView(view2);
		mViews[0].setOffset(0, 0);
		mViews[1].setOffset(0, 0);
    }
    
    public void setAreaWith(int with){
    	this.mWith = with;
    }
	public void setPaddingWidth(int viewPaddingWidth) {
		mViewPaddingWidth = viewPaddingWidth;
	}

	public void setAnimationDuration(int animationDuration) {
		mAnimationDuration = animationDuration;
	}

	public void setSnapBorderRatio(float snapBorderRatio) {
		mSnapBorderRatio = snapBorderRatio;
	}



	public int getFirstPosition() {
		return 0;
	}

	private int getPrevViewNumber(int relativeViewNumber) {
		return (relativeViewNumber == 0) ? 2 : relativeViewNumber - 1;
	}

	private int getNextViewNumber(int relativeViewNumber) {
		return (relativeViewNumber == 2) ? 0 : relativeViewNumber + 1;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// Calculate our view width
		mGalleryWidth = right - left;
		if (changed == true) {
			// Position views at correct starting offsets
			mViews[0].setOffset(0, 0);
			mViews[1].setOffset(0, 0);
		}
	}

	private int getViewOffset(int viewNumber) {
		// Determine width including configured padding width
		int offsetWidth = mGalleryWidth + mViewPaddingWidth;

		// Position the previous view one measured width to left
		if (viewNumber == 1) {
			return -offsetWidth;
		}


		return 0;
	}
	private int getViewOffset(int viewNumber, int relativeViewNumber) {
		// Determine width including configured padding width
		int offsetWidth = mGalleryWidth + mViewPaddingWidth;

		// Position the previous view one measured width to left
		if (viewNumber == getPrevViewNumber(relativeViewNumber)) {
			return offsetWidth;
		}

		// Position the next view one measured width to the right
		if (viewNumber == getNextViewNumber(relativeViewNumber)) {
			return offsetWidth * -1;
		}

		return 0;
	}
	public void movePrevious() {
		// Slide to previous view
		mFlingDirection = 1;
		processGesture();
	}

	public void moveNext() {
		// Slide to next view
		mFlingDirection = -1;
		processGesture();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			movePrevious();
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			moveNext();
			return true;

		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
		}

		return super.onKeyDown(keyCode, event);
	}


	public boolean onGalleryTouchEvent(MotionEvent event) {
		boolean consumed = mGestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mIsTouched || mIsDragging) {
				move();
			}
		}
	
		return consumed;
	}
    public int getPosition(){
    	return mCurrentPosition;
    }
	public void move() {
		processScrollSnap();
		processGesture();
	}

	void processGesture() {
		int reloadViewNumber = 0;
		int reloadPosition = 0;
		mIsTouched = false;
		mIsDragging = false;
		isAction = true;
		if (mFlingDirection > 0) {//向右
			mCurrentPosition = 0;
		}

		if (mFlingDirection < 0) {//向左
			mCurrentPosition = 1;
		}
		// Ensure input focus on the current view
		mViews[mCurrentPosition].requestFocus();

		// Run the slide animations for view transitions
		mAnimation.prepareAnimation(mCurrentPosition);
		this.startAnimation(mAnimation);

		// Reset fling state
		mFlingDirection = 0;
	}

	void processScrollSnap() {
		// Snap to next view if scrolled passed snap position
		float rollEdgeWidth = mGalleryWidth * mSnapBorderRatio;
		int rollOffset = mGalleryWidth - (int) rollEdgeWidth;
		int currentOffset = mViews[0].getCurrentOffset();

		if (currentOffset <= rollOffset * -1) {
			// Snap to previous view
			mFlingDirection = 1;
		}

		if (currentOffset >= rollOffset) {
			// Snap to next view
			mFlingDirection = -1;
		}
	}

	private class FlingGalleryView {
		private int mViewNumber;
		private FrameLayout mParentLayout;
		private LinearLayout mInternalLayout = null;
		public int mLastMotionY;//当前y轴坐标
		public float mStartY;//刚点击时y轴坐标
		public int height = 0;

		public FlingGalleryView(int viewNumber, FrameLayout parentLayout) {
			mViewNumber = viewNumber;
			mParentLayout = parentLayout;
			
		}
		public void addFrameLayoutView(View view){
			mInternalLayout = new LinearLayout(mContext);
			mInternalLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			mInternalLayout.addView(view);
			mParentLayout.addView(mInternalLayout);
		}

		public void setOffset(int xOffset, int yOffset) {
			// Scroll the target view relative to its own position relative to
			// currently displayed view
			
			mInternalLayout.scrollTo(getViewOffset(mViewNumber)+ xOffset, yOffset);
		}
		public void setOffset(int xOffset, int yOffset, int relativeViewNumber) {
			// Scroll the target view relative to its own position relative to
			// currently displayed view
			mInternalLayout.scrollTo(getViewOffset(mViewNumber,relativeViewNumber)+ xOffset, yOffset);
		}
		
		

		public int getCurrentOffset() {
			// Return the current scroll position
			return mInternalLayout.getScrollX();
		}

		public void requestFocus() {
			mInternalLayout.requestFocus();
		}
		public void myScrollBy(int x,int y){
			mInternalLayout.scrollBy(x, y);
		}
		public boolean isMove(float deletY){
			float sY = mInternalLayout.getScrollY() + deletY;
			if(sY>0&&sY<height){
				return true;
			}
			return false;
		}
		public void setHeight(int height){
		    int sHeight = mInternalLayout.getHeight() - height;
		    if(sHeight>0){
		    	this.height = sHeight;
		    }
		}
	}
    
	private class FlingGalleryAnimation extends Animation {
		private boolean mIsAnimationInProgres;
		private int mRelativeViewNumber;
		private int mInitialOffset;
		private int mTargetOffset;
		private int mTargetDistance;
		

		public FlingGalleryAnimation() {
			mIsAnimationInProgres = false;
			mRelativeViewNumber = 0;
			mInitialOffset = 0;
			mTargetOffset = 0;
			mTargetDistance = 0;
		}

		public void prepareAnimation(int relativeViewNumber) {
			// If we are animating relative to a new view
			if (mRelativeViewNumber != relativeViewNumber) {
				if (mIsAnimationInProgres == true) {
					// We only have three views so if requested again to animate
					// in same direction we must snap
					int newDirection = (relativeViewNumber == getPrevViewNumber(mRelativeViewNumber)) ? 1 : -1;
					int animDirection = (mTargetDistance < 0) ? 1 : -1;

					// If animation in same direction
					if (animDirection == newDirection) {
						// Ran out of time to animate so snap to the target
						// offset
						mViews[0].setOffset(mTargetOffset, mViews[0].mLastMotionY,mRelativeViewNumber);
						mViews[1].setOffset(mTargetOffset, mViews[1].mLastMotionY,mRelativeViewNumber);
					}
				}

				mRelativeViewNumber = relativeViewNumber;
			}

			mInitialOffset = mViews[mRelativeViewNumber].getCurrentOffset();
			mTargetOffset = getViewOffset(mRelativeViewNumber,
					mRelativeViewNumber);
			mTargetDistance = mTargetOffset - mInitialOffset;

			this.setDuration(mAnimationDuration);
			this.setInterpolator(mDecelerateInterpolater);

			mIsAnimationInProgres = true;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation transformation) {
			// Ensure interpolatedTime does not over-shoot then calculate new
			// offset
			interpolatedTime = (interpolatedTime > 1.0f) ? 1.0f
					: interpolatedTime;
			int offset = mInitialOffset
					+ (int) (mTargetDistance * interpolatedTime);

			for (int viewNumber = 0; viewNumber < 2; viewNumber++) {
				// Only need to animate the visible views as the other view will
				// always be off-screen
				if ((mTargetDistance > 0 && viewNumber != getNextViewNumber(mRelativeViewNumber))
						|| (mTargetDistance < 0 && viewNumber != getPrevViewNumber(mRelativeViewNumber))) {
					mViews[viewNumber].setOffset(offset, mViews[viewNumber].mLastMotionY, mRelativeViewNumber);
				}
			}
		}

		@Override
		public boolean getTransformation(long currentTime,
				Transformation outTransformation) {
			if (super.getTransformation(currentTime, outTransformation) == false) {
				// Perform final adjustment to offsets to cleanup animation
				mViews[0].setOffset(mTargetOffset, mViews[0].mLastMotionY, mRelativeViewNumber);
				mViews[1].setOffset(mTargetOffset, mViews[1].mLastMotionY, mRelativeViewNumber);

				// Reached the animation target
				mIsAnimationInProgres = false;

				return false;
			}

			// Cancel if the screen touched
			if (mIsTouched || mIsDragging) {
				// Note that at this point we still consider ourselves to be
				// animating
				// because we have not yet reached the target offset; its just
				// that the
				// user has temporarily interrupted the animation with a touch
				// gesture

				return false;
			}

			return true;
		}
	}
	private class FlingGestureDetector extends GestureDetector.SimpleOnGestureListener {
	    
		@Override
		public boolean onDown(MotionEvent e) {
			// Stop animation
			mIsTouched = true;
			mFlingDirection = 0;
			mViews[mCurrentPosition].mStartY = e.getY();
			int with = PublicMethod.getDisplayWidth(mContext);
			if(e.getX() < (with - mWith)&& mCurrentPosition==0){
				isAction = false;
			}
			return true;
		}
        int newX =0;
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
	
			if (e2.getAction() == MotionEvent.ACTION_MOVE) {
				if (mIsDragging == false) {
					mIsTouched = true;
					
					mIsDragging = true;
					mFlingDirection = 0;
					mScrollTimestamp = System.currentTimeMillis();
					mCurrentOffset = mViews[mCurrentPosition].getCurrentOffset();
				}
				float maxVelocity = mGalleryWidth / (mAnimationDuration / 1000.0f);
				long timestampDelta = System.currentTimeMillis() - mScrollTimestamp;
				float maxScrollDelta = maxVelocity * (timestampDelta / 1000.0f);
				float currentScrollDelta = e1.getX() - e2.getX();

				if (currentScrollDelta < maxScrollDelta * -1)
					currentScrollDelta = maxScrollDelta * -1;
				if (currentScrollDelta > maxScrollDelta)
					currentScrollDelta = maxScrollDelta;
				int scrollOffset = Math.round(mCurrentOffset + currentScrollDelta);

				// We can't scroll more than the width of our own frame layout
				if (scrollOffset >= mGalleryWidth)scrollOffset = mGalleryWidth;
				if (scrollOffset <= mGalleryWidth * -1)scrollOffset = mGalleryWidth * -1;
				if(mCurrentPosition == 0&&scrollOffset<0){
					scrollOffset = 0;
				}else if(mCurrentPosition == 1&&scrollOffset>0){
					scrollOffset = 0;
				}
				if (Math.abs(e1.getY() - e2.getY()) < Math.abs(e1.getX() - e2.getX())&&isAction){
					mViews[0].setOffset(scrollOffset, mViews[0].mLastMotionY,mCurrentPosition);
					mViews[1].setOffset(scrollOffset, mViews[1].mLastMotionY,mCurrentPosition);
				}
				else{
					float deletY =  mViews[mCurrentPosition].mStartY - e2.getY();
					FlingGalleryView view = mViews[mCurrentPosition];
					if(view.isMove(deletY)){
						view.mLastMotionY += deletY;
			            view.myScrollBy(0,(int) deletY);
					}
		            mViews[mCurrentPosition].mStartY = e2.getY();
				}
			}

			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) < Math.abs(e1.getX() - e2.getX())&&Math.abs(e1.getY() - e2.getY())<=swipe_max_off_path) {
					
				if (e2.getX() - e1.getX() > swipe_min_distance && Math.abs(velocityX) > swipe_threshold_veloicty&&isAction) {
					movePrevious();
				}
				if (e1.getX() - e2.getX() > swipe_min_distance && Math.abs(velocityX) > swipe_threshold_veloicty&&isAction) {
					moveNext();
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			mFlingDirection = 0;
			processGesture();
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// Reset fling state
			mFlingDirection = 0;
			return false;
		}
	}
	public void startAnimation(){
		mAnimation.prepareAnimation(0);
		this.startAnimation(mAnimation);
	}
	public int getViewScrollY(){
		 return mViews[mCurrentPosition].mInternalLayout.getScrollY();
	}
	public void setViewHeight(int height){
		if(mViews[mCurrentPosition].height==0){
			mViews[mCurrentPosition].setHeight(height);
		}
	}
}

