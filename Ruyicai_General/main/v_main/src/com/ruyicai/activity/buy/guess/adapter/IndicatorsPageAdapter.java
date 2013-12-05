package com.ruyicai.activity.buy.guess.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class IndicatorsPageAdapter extends PagerAdapter {
	
//	private View[] mPageViews;
	private List<View> mPageViews;

	public IndicatorsPageAdapter(List<View> mPageViews) {
		this.mPageViews = mPageViews;
	}
	
	public void setPageViewList(List<View> mPageViews) {
		this.mPageViews = mPageViews;
	}

	@Override
	public int getCount() {
		if (mPageViews == null) {
			return 0;
		}
		return mPageViews.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
//		try {  
//			((ViewPager)container).addView(mPageViews[position % mPageViews.length], 0);
//		}catch(Exception e){
//			//handler something
//		}
//		return mPageViews[position % mPageViews.length];
		
		View indexView = mPageViews.get(position);
//		((ViewPager) container).addView(indexView);
		container.addView(indexView);
		return indexView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
