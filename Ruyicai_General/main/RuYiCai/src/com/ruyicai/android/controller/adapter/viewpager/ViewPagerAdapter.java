package com.ruyicai.android.controller.adapter.viewpager;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 滑动面板适配器：该适配器用于viewPager控件，加载多个视图页面，并进行滑动切换
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-25
 */
public class ViewPagerAdapter extends PagerAdapter {
	/** 页面视图集合 */
	private List<View>	_fViewList;

	/**
	 * 构造方法
	 * 
	 * @param _fViewList
	 *            页面视图对象集合
	 */
	public ViewPagerAdapter(List<View> _fViewList) {
		super();
		this._fViewList = _fViewList;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 初始化position位置的界面
		View subView = _fViewList.get(position);
		container.addView(subView);
		return subView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// 销毁position页的页面
		container.removeView(_fViewList.get(position));
	}

	@Override
	public int getCount() {
		// 获取当前页面的数目
		return _fViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// 判断是否有对象生成界面
		return arg0 == arg1;
	}

}
