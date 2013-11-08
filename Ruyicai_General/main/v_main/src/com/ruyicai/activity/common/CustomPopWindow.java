package com.ruyicai.activity.common;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.InquiryAdapter;
import com.ruyicai.activity.usercenter.InquiryAdapter.OnChickItem;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class CustomPopWindow{
	
	private PopupWindow mPopupWindow;
//	private OnChickItem mOnChickItem;
	private View mMainLayout;
	private InquiryAdapter mAdapter;

	public CustomPopWindow(Context context, String[] info, int count, 
			OnChickItem onChickItem, int type) {
//		super(getView(context, info, count), LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//		mOnChickItem = onChickItem;
//		mMainLayout = getView(context, info, count);
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainLayout = (LinearLayout) inflate.inflate(R.layout.popwindow_layout, null);
		GridView gridView = (GridView) mMainLayout.findViewById(R.id.gridview);
		gridView.setNumColumns(count);
		mAdapter = new InquiryAdapter(context, info, onChickItem, type);
		gridView.setAdapter(mAdapter);
		mPopupWindow = new PopupWindow(mMainLayout, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}
	
//	private View getView(Context context, String[] info, int count) {
//		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		LinearLayout layout = (LinearLayout) inflate.inflate(R.layout.popwindow_layout, null);
//		GridView gridView = (GridView) layout.findViewById(R.id.gridview);
//		gridView.setNumColumns(count);
//		mAdapter = new InquiryAdapter(context, info);
//		gridView.setAdapter(mAdapter);
//		return layout;
//	}
	
	public PopupWindow getPopupWindow() {
		return mPopupWindow;
	}
	
	public void setBackground(int resid) {
		mMainLayout.setBackgroundResource(resid);
	}
	
	public void showAsDropDown(View view) {
		mPopupWindow.showAsDropDown(view);
	}
	
	public void setItemSelect(int index) {
		mAdapter.setItemSelect(index);
		mAdapter.notifyDataSetChanged();
	}
	
	public boolean isShowing() {
		return mPopupWindow.isShowing();
	}
	
	public void dismiss() {
		mPopupWindow.dismiss();
	}
}
