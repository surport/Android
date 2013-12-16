/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.ruyicai.activity.buy.guess.view;

import com.palmdream.RuyicaiAndroid.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	private int mState = STATE_NORMAL;

	private Context mContext;

	private View mContentView;
	private TextView mHintView;
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	private final int ROTATE_ANIM_DURATION = 180;
	private ImageView mArrowImageView;
	private LinearLayout mLoadMoreLayout;
	private TextView mFooterTimeView;
	

	public ListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public ListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public void setState(int state) {
		if (state == mState) {
			return;
		}
		
		if (state == STATE_LOADING) { // 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
		} else { // 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
		}
		
		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}else if (mState == STATE_LOADING) {
				mArrowImageView.clearAnimation();
			} /*else {
				hide();
			}*/
			mArrowImageView.setVisibility(View.GONE);
			mHintView.setVisibility(View.GONE);
			mFooterTimeView.setVisibility(View.GONE);
			mHintView.setText(R.string.listview_footer_up_pull);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mFooterTimeView.setVisibility(View.VISIBLE);
				mHintView.setVisibility(View.VISIBLE);
				mHintView.setText(R.string.listview_footer_hint_ready);
			}
			break;
		case STATE_LOADING:
			mArrowImageView.clearAnimation();
			mFooterTimeView.setVisibility(View.VISIBLE);
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.ruyi_guess_loading);
			break;
		}

		mState = state;
		
		
//		if (state == STATE_READY) {
//			mHintView.setVisibility(View.VISIBLE);
//			mHintView.setText(R.string.listview_footer_hint_ready);
//			mArrowImageView.setVisibility(View.VISIBLE);
//			mArrowImageView.clearAnimation();
//			mArrowImageView.startAnimation(mRotateDownAnim);
//		} else if (state == STATE_LOADING) {
//			mArrowImageView.clearAnimation();
//			mArrowImageView.setVisibility(View.INVISIBLE);
//			mHintView.setText(R.string.ruyi_guess_loading);
//		} else {
//			mArrowImageView.clearAnimation();
//			mHintView.setVisibility(View.VISIBLE);
//			mArrowImageView.setVisibility(View.VISIBLE);
////			mHintView.setText(R.string.listview_footer_hint_ready); 
//		}
		
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.listview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		mArrowImageView = (ImageView) findViewById(R.id.listview_footer_arrow);
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mFooterTimeView = (TextView) moreView
				.findViewById(R.id.xlistview_header_time);
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}
	
	public TextView getHeaderTimeView() {
		return mFooterTimeView;
	}

}
