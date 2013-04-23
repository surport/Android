package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MainViewPagerAdapter extends PagerAdapter {
	private List<View> viewsBufList;

	public MainViewPagerAdapter(List<View> viewsBufList) {
		if (viewsBufList != null) {
			this.viewsBufList = viewsBufList;
		} else {
			this.viewsBufList = new ArrayList<View>();
		}

	}

	public void addView(View view) {
		viewsBufList.add(view);
	}

	public void addView(View view, int position) {
		viewsBufList.add(position, view);
	}

	@Override
	public int getCount() {

		// Log.d(TAG, "PagerAdapter:getCount");
		/*
		 * 返回提供给ViewPager的视图总数. 一般我们会把View群先插入一个List<View>中缓存,
		 * 然后在这里就返回这个List<View>.size()即可.
		 */
		return viewsBufList.size();
	}

	@Override
	public void startUpdate(ViewGroup container) {
		// Log.d(TAG, "PagerAdapter:startUpdate");
	}

	@Override
	public int getItemPosition(Object object) {
		// Log.d(TAG, "PagerAdapter:getItemPosition");
		return POSITION_UNCHANGED;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Log.d(TAG, "PagerAdapter:destroyItem");
		container.removeView(viewsBufList.get(position));
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// Log.d(TAG, "PagerAdapter:getPageTitle");
		// 设置每个tab view的标题
		// return viewsTitleBufList.get(position);
		return null;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// Log.d(TAG, "PagerAdapter:instantiateItem");
		container.addView(viewsBufList.get(position), 0);
		return viewsBufList.get(position);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		// Log.d(TAG, "PagerAdapter:finishUpdate");
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// Log.d(TAG, "PagerAdapter:isViewFromObject");
		return arg0 == (arg1);

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// Log.d(TAG, "PagerAdapter:restoreState");
	}

	@Override
	public Parcelable saveState() {
		// Log.d(TAG, "PagerAdapter:saveState");
		return null;

	}

	@Override
	public void notifyDataSetChanged() {
		// Log.d(TAG, "PagerAdapter:notifyDataSetChanged");
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// Log.d(TAG, "PagerAdapter:setPrimaryItem");
	}
}