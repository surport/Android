package com.ruyicai.activity.common;

import com.palmdream.RuyicaiAndroid.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class CustomPopWindow {
	protected Context mContext;
	protected View mLayout;
	protected LayoutInflater mLayoutInflater;
	protected LinearLayout mLinearLayout;
	protected PopupWindow mPopupWindow;
	protected TextView mTitle;
	protected LinearLayout mBottomViewContainer;

	protected Animation mEnterAnimation;
	protected int mAnimationStyle = -1;

	protected int mLacationInParent = Gravity.BOTTOM;
	protected int mLocationMarginTop = 70;

	public void setLocationMarginTop(int marginTop) {
		this.mLocationMarginTop = marginTop;
	}

	private final int MSG_DELAYED_DISMISS = 100;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_DELAYED_DISMISS:
				CustomPopWindow.this.close();
				break;

			default:
				break;
			}
		};
	};

	public CustomPopWindow(Context context, View layout) {
		this.mLayout = layout;
		init(context);
	}

	public CustomPopWindow(Context context, int layoutResID) {
		this.mLayout = LayoutInflater.from(context).inflate(layoutResID, null);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		if (mLinearLayout == null) {
			mLinearLayout = (LinearLayout) mLayoutInflater.inflate(
					R.layout.popwindow_layout, null);
			mTitle = (TextView) mLinearLayout.findViewById(R.id.title);

			mLinearLayout.setFocusable(true);
			mLinearLayout.setFocusableInTouchMode(true);
			mLinearLayout.requestFocus();
			mLinearLayout.setOnKeyListener(mOnKeyListener);
		}
		if (mPopupWindow == null && mLinearLayout != null) {
			mPopupWindow = new PopupWindow(mLinearLayout,
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//			mPopupWindow.setAnimationStyle(R.style.fmlib_MenuWindowAnimation);
			mPopupWindow.setFocusable(true);
		}

	}

	public void updatePopupWindow(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = metric.heightPixels;
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mPopupWindow.setWidth(height - 80);
		} else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (width <= 321) {
				mPopupWindow.setWidth(width - 50);
				return;
			} else if (width <= 481) {
				mPopupWindow.setWidth(width - 80);
				return;
			} else if (width > 481) {
				mPopupWindow.setWidth(width - 100);
			}

		}
	}

	public void show() {
		if (mPopupWindow.isShowing()) {
			return;
		}
		if (mAnimationStyle != -1) {
			mPopupWindow.setAnimationStyle(mAnimationStyle);
		}
		if (mEnterAnimation != null) {
			mLinearLayout.startAnimation(mEnterAnimation);
		} else {
//			Animation animation = AnimationUtils.loadAnimation(mContext,
//					R.anim.fmlib_menu_window_enter);
//			mLinearLayout.startAnimation(animation);
		}

		if (mLacationInParent == Gravity.TOP) {
			mPopupWindow.showAtLocation(mLayout, Gravity.CENTER_HORIZONTAL
					| mLacationInParent, 0, mLocationMarginTop);
		} else {
			mPopupWindow.showAtLocation(mLayout, Gravity.CENTER_HORIZONTAL
					| mLacationInParent, 0, 0);
		}
	}

	public void close() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			isActionUp = false;
			mPopupWindow.setFocusable(true);
			mPopupWindow.dismiss();
		}
	}

	public void dismissDelayed() {
		mHandler.removeMessages(MSG_DELAYED_DISMISS);
		mHandler.sendEmptyMessageDelayed(MSG_DELAYED_DISMISS, 100);
	}

	public void setContentView(View view) {
	}

	public void setContentView(int resid) {
		View view = LayoutInflater.from(mContext).inflate(resid, null);
		setContentView(view);
	}

	public void setContentBottomView(View view) {
		if (mBottomViewContainer == null) {
//			mBottomViewContainer = (LinearLayout) mLinearLayout
//					.findViewById(R.id.viewBottomContainer);
//			mBottomViewContainer.setVisibility(View.VISIBLE);
		}
		mBottomViewContainer.removeAllViews();
		mBottomViewContainer.addView(view);
	}

	public void setContentBottomView(int resid) {
		View view = LayoutInflater.from(mContext).inflate(resid, null);
		setContentBottomView(view);
	}

	public void setBackgroundColor(int color) {
		if (mLinearLayout != null) {
			mLinearLayout.setBackgroundColor(color);
		}
	}

	public void setBackgroundResource(int resid) {
		if (mLinearLayout != null) {
			mLinearLayout.setBackgroundResource(resid);
		}
	}

	public void setBackgroundDrawable(Drawable d) {
		if (mLinearLayout != null) {
			mLinearLayout.setBackgroundDrawable(d);
		}
	}

	public void setEnterAnimation(Animation animation) {
		mEnterAnimation = animation;
	}

	public void setEnterAnimation(int resid) {
		mEnterAnimation = AnimationUtils.loadAnimation(mContext, resid);
	}

	public void setAnimationStyle(int animationStyle) {
		mAnimationStyle = animationStyle;
	}

	private void setAnimation(int enterAnimationResid, int animationStyle) {
		if (mEnterAnimation == null) {
			setEnterAnimation(enterAnimationResid);
		}
		if (mAnimationStyle == -1) {
			mAnimationStyle = animationStyle;
		}
	}

	public void setLacationInParent(Location location) {
		switch (location) {
		case SHOW_IN_BOTTOM:
//			setAnimation(R.anim.fmlib_menu_window_enter,
//					R.style.fmlib_MenuWindowAnimation);
			mLacationInParent = Gravity.BOTTOM;
			break;
		case SHOW_IN_MIDDLE:
//			setAnimation(R.anim.fmlib_middle_menu_window_enter,
//					R.style.fmlib_MiddleMenuWindowAnimation);
			mLacationInParent = Gravity.CENTER_VERTICAL;
			break;
		case SHOW_IN_TOP:
//			setAnimation(R.anim.fmlib_top_menu_window_enter,
//					R.style.fmlib_TopMenuWindowAnimation);
			mLacationInParent = Gravity.TOP;
			break;
		default:
			mLacationInParent = Gravity.BOTTOM;
			break;
		}
	}

	public void setTitle(int resid) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(resid);
	}

	public void setTitle(CharSequence text) {
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(text);
	}

	public void setTitleBg(int resId) {
		mTitle.setBackgroundResource(resId);
	}

	public TextView getTitle() {
		mTitle.setVisibility(View.VISIBLE);
		return mTitle;
	}

	public void setMargins(int left, int top, int right, int bottom) {
		if (top < 0) {
			top = 10;
		}
		if (bottom < 0) {
			bottom = 10;
		}
		if (right < 0) {
			top = 10;
		}
		if (bottom < 0) {
			bottom = 10;
		}
	}

	public void setMenuWindwSize(int width, int height) {
		// System.out.println("setMenuWindwSize width:"+width+"height:"+height);
		if (width != 0) {
			mPopupWindow.setWidth(width);
		}
		if (height != 0) {
			mPopupWindow.setHeight(height);
		}

	}

	/**
	 * Sets the listener to be called when the window is dismissed.
	 * 
	 * @param onDismissListener
	 *            The listener.
	 */
	public void setOnDismissListener(OnDismissListener onDismissListener) {
		mPopupWindow.setOnDismissListener(onDismissListener);
	}

	public boolean isShowing() {
		return mPopupWindow.isShowing();
	}

	public enum Location {
		SHOW_IN_BOTTOM, SHOW_IN_MIDDLE, SHOW_IN_TOP
	}

	private boolean isActionUp = false;

	public void setActionToUp() {
		isActionUp = true;
	}

	private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (mLacationInParent == Gravity.BOTTOM) {
				/*
				 * if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode ==
				 * KeyEvent.KEYCODE_MENU) { if(isActionUp) { isActionUp = false;
				 * close(); } } else
				 */if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_MENU) {
					if (isActionUp) {
						close();
						return false;
					}
					if (isShowing()) {
						isActionUp = true;
					}
				}
			}
			return false;
		}
	};

	public void setOnKeyListener(View.OnKeyListener onKeyListener) {
		mOnKeyListener = onKeyListener;
		mLinearLayout.setOnKeyListener(mOnKeyListener);
	}
}
