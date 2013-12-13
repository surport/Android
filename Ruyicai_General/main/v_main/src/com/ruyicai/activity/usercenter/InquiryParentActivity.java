package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.CustomPopWindow;
import com.ruyicai.activity.usercenter.InquiryAdapter.OnChickItem;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 投注查询和追号查询父类
 * 
 * @author yejc
 * 
 */
public abstract class  InquiryParentActivity extends Activity {
	/**
	 * 用于存放各个彩种的总页数
	 */
	protected int[] mTotalPageArray = new int[19];
	
	/**
	 * 用于存放各个彩种的当前页的索引
	 */
	protected int[] mPageIndexArray = new int[19];
	
	/**
	 * 用于存放各个彩种的当前数据
	 */
	protected ArrayList[] mListArray = new ArrayList[19];
	
	/**
	 * 彩种数组
	 */
	protected String[] mLotnoArray = null;
	
	/**
	 * 所有状态数组
	 */
	protected String[] mStateArray = null;
	
	/**
	 * 所有时间数组
	 */
	protected String[] mTimeArray = null;
	
	/**
	 * 按彩种显示按钮
	 */
	protected Button mLotnoBtn = null;
	
	/**
	 * 按中奖状态显示按钮
	 */
	protected Button mAwardStateBtn = null;
	
	/**
	 * 按时间显示按钮
	 */
	protected Button mTimeBtn = null;
	
	/**
	 * 当前按彩种查询索引
	 */
	protected int mCurrentLotnoIndex = 0;
	
	/**
	 * 当前按中奖状态查询索引
	 */
	protected int mCurrentAwardStateIndex = 0;
	
	/**
	 * 当前按时间查询索引
	 */
	protected int mCurrentTiemIndex = 0;
	
	/**
	 * 全部彩种、全部状态、全部时间的切换窗口
	 */
	protected CustomPopWindow mPopupWindow = null;
	
	/**
	 * 按时间查询请求参数数组
	 */
	protected String[] mTimeType = {"","1","2","4"};
	
	/**
	 * 主线性布局
	 */
	protected LinearLayout mUsecenerLinear = null;
	
	/**
	 * 标题栏
	 */
	protected TextView mTitleTextView = null;
	
	/**
	 * 用户名
	 */
	protected String mUserNo = "";
	
	/**
	 * 进度对话框
	 */
	protected ProgressDialog mProgressDialog = null;
	
	protected final int DIALOG1_KEY = 0;
	
	/**
	 * 显示数据的listview
	 */
	protected ListView mListView = null;
	
	/**
	 * 获取更多布局
	 */
	protected View mView = null;
	
	/**
	 * 界面详情View
	 */
	protected View mMainView = null;
	
	/**
	 * 进度条
	 */
	protected ProgressBar mProgressbar = null;
	
	protected boolean mIsFirst = false;
	
	protected int mOrangeColor = 0;
	
	protected int mBlackColor = 0;
	
//	protected String mSubType = "";
	
//	protected abstract void getData(int type);
	
	protected abstract void initListView();
	
	protected abstract void netting(int index);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayoutold);
		mOrangeColor = getResources().getColor(R.color.Inquiry_text_color);
		mBlackColor = getResources().getColor(R.color.black);
		mIsFirst = true;
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		mUserNo = shellRW.getStringValue("userno");
		mMainView = initLinearView();
		initView();
	}
	
	protected void initView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.usercenter_join_layout);
		layout.setVisibility(View.VISIBLE);
		mTimeArray = getResources().getStringArray(R.array.time_state_list);
		mTitleTextView = (TextView) findViewById(R.id.usercenter_mainlayou_text_title);
		mLotnoBtn = (Button) findViewById(R.id.lotno_change_state_title);
		mAwardStateBtn = (Button) findViewById(R.id.award_change_state_title);
		mTimeBtn = (Button) findViewById(R.id.time_change_state_title);
		mTimeBtn.setText(mTimeArray[0]);
		StateChangeClickListener clickListener = new StateChangeClickListener();
		mLotnoBtn.setOnClickListener(clickListener);
		mAwardStateBtn.setOnClickListener(clickListener);
		mTimeBtn.setOnClickListener(clickListener);
		mUsecenerLinear = (LinearLayout) findViewById(R.id.usercenterContent);
		mUsecenerLinear.addView(mMainView);
	}
	
	protected View initLinearView() {
		LayoutInflater inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewlist = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		mListView = (ListView) viewlist
				.findViewById(R.id.usercenter_listview_queryinfo);
		LayoutInflater mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = mInflater.inflate(R.layout.lookmorebtn, null);
		mProgressbar = (ProgressBar) mView.findViewById(R.id.getmore_progressbar);
		mListView.addFooterView(mView);
		mView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mView.setEnabled(false);
				addmore();
			}
		});
		if (mListArray[0] == null) {
			mListArray[0] = new ArrayList();
		}
		initListView();
		return viewlist;
	}
	
	protected void addmore() {
		int pageIndex = getNewPage();
		int allpagenum = getAllPage();
		mIsFirst = false;
		pageIndex++;
		if (pageIndex < allpagenum) {
			netting(pageIndex);
		} else {
			mProgressbar.setVisibility(View.INVISIBLE);
			pageIndex = allpagenum - 1;
			mView.setEnabled(true);
			Toast.makeText(this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		}
	}
	
	public class StateChangeClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			PopOnItemChick popClick = new PopOnItemChick();
			switch (v.getId()) {
			case R.id.lotno_change_state_title:
				mPopupWindow = new CustomPopWindow(InquiryParentActivity.this,
						mLotnoArray, 3, popClick, R.id.lotno_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_left);
				mPopupWindow.setItemSelect(mCurrentLotnoIndex);
				mLotnoBtn.setTextColor(mOrangeColor);
				break;
			case R.id.award_change_state_title:
				mPopupWindow = new CustomPopWindow(InquiryParentActivity.this,
						mStateArray, 4, popClick, R.id.award_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_center);
				mPopupWindow.setItemSelect(mCurrentAwardStateIndex);
				mAwardStateBtn.setTextColor(mOrangeColor);
				break;
			case R.id.time_change_state_title:
				mPopupWindow = new CustomPopWindow(InquiryParentActivity.this,
						mTimeArray, 4, popClick, R.id.time_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_right);
				mPopupWindow.setItemSelect(mCurrentTiemIndex);
				mTimeBtn.setTextColor(mOrangeColor);
				break;
			}
			mPopupWindow.showAsDropDown(v);
			mPopupWindow.getPopupWindow().setTouchInterceptor(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						mPopupWindow.dismiss();
						setTextColor();
						return true;
					}
					return false;
				}
			});
		}
	}
	
	public class PopOnItemChick implements OnChickItem {

		@Override
		public void onChickItem(int position, int type) {
			if (mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
				setTextColor();
			}
			switch (type) {
			case R.id.lotno_change_state_title:
				mCurrentLotnoIndex = position;
				mLotnoBtn.setText(mLotnoArray[mCurrentLotnoIndex]);
				break;

			case R.id.award_change_state_title:
				mCurrentAwardStateIndex = position;
				mAwardStateBtn.setText(mStateArray[position]);
				break;

			case R.id.time_change_state_title:
				mCurrentTiemIndex = position;
				mTimeBtn.setText(mTimeArray[position]);
				break;
			}
			getData(type);
		}
	}
	
	protected void setTextColor() {
		mLotnoBtn.setTextColor(mBlackColor);
		mAwardStateBtn.setTextColor(mBlackColor);
		mTimeBtn.setTextColor(mBlackColor);
	}
	
	protected void getData(int type) {
		if (mListArray[mCurrentLotnoIndex] == null) {
			mListArray[mCurrentLotnoIndex] = new ArrayList();
		}
//		if ((R.id.lotno_change_state_title == type)
//				&& (mListArray[mCurrentLotnoIndex].size() > 0)) {
//			initListView();
//		} else {
			showDialog(0);
			netting(0);
//		}
	}
	
	protected Dialog onCreateDialog(int id) {
		mProgressDialog = new ProgressDialog(this);
		switch (id) {
		case DIALOG1_KEY: {
			mProgressDialog.setTitle(R.string.usercenter_netDialogTitle);
			mProgressDialog.setMessage(getString(R.string.usercenter_netDialogRemind));
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			return mProgressDialog;
		}
		}
		return mProgressDialog;
	}
	
	protected void setNewPage(int page) {
		mPageIndexArray[mCurrentLotnoIndex] = page;
	}

	protected int getNewPage() {
		return mPageIndexArray[mCurrentLotnoIndex];
	}

	protected void setAllPage(int page) {
		mTotalPageArray[mCurrentLotnoIndex] = page;
	}

	protected int getAllPage() {
		return mTotalPageArray[mCurrentLotnoIndex];
	}
	
	protected void dismiss() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}
