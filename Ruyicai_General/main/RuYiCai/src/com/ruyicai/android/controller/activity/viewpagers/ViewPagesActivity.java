package com.ruyicai.android.controller.activity.viewpagers;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.inject.Inject;
import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.adapter.viewpager.ViewPagerAdapter;
import com.ruyicai.android.tools.LogTools;

/**
 * 滑动页面：实现了页面的左右滑动切换功能，该页面包含页面切换按钮（可选）、玩法展示文本框（可选）、滑动区域、投注栏（可选）等组件。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-15
 */
public abstract class ViewPagesActivity extends BaseActivity {
	private static final String	TAG					= "ViewPagesActivity";

	/** 滑动页面的页数 */
	private static final int	VIEWPAGE_PAGE_NUM	= 2;

	/** 滑动ViewPager适配器对象 */
	public ViewPagerAdapter		_fSelectNumberAdapter;

	/** 引用对象：布局对象 */
	@Inject
	protected LayoutInflater	_fLayoutInflater;

	/** 视图引用：相对布局容器对象 */
	@InjectView(R.id.viewpagers_relativelayout_container)
	protected RelativeLayout	_fContainerRelativeLayout;
	/** 视图引用：滑动ViewPager对象 */
	@InjectView(R.id.viewpagers_viewpager_slidearea)
	protected ViewPager			_fSlideAreaViewPager;

	/**
	 * 是否添加投注栏
	 * 
	 * @return 是否添加标识
	 */
	protected abstract boolean IsAddBettingBar();

	/**
	 * 是否添加玩法介绍文本框
	 * 
	 * @return 是否添加标识
	 */
	protected abstract boolean isAddPlayMethodTextView();

	/**
	 * 是否添加页面改变单选按钮组
	 * 
	 * @return 是否添加标识
	 */
	protected abstract boolean isAddPageChangeRadioButtons();

	/**
	 * 添加并初始化页面切换单选按钮组
	 */
	protected abstract void addPageChangeRadioButtons();

	/**
	 * 设置玩法文本框
	 */
	protected abstract void addPlayMethodTextView();


	/**
	 * 根据页面索引获取并初始化滑动区域视图
	 * 
	 * @param aSlideAreaView
	 *            滑动区域页面视图
	 * @param aPage_i
	 *            页面的索引
	 */
	protected abstract View getAndInitSlideAreaViewShow(int aPage_i);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogTools.showLog(LogTools.TAG, getClass().getSimpleName()
				+ ":onCreate()", LogTools.INFO);

		setContentView(R.layout.viewpagers_activity);

		if (isAddPageChangeRadioButtons()) {
			// 添加顶部页面切换单选按钮组
			addPageChangeRadioButtons();
		}

		if (isAddPlayMethodTextView()) {
			// 添加玩法说明栏
			addPlayMethodTextView();
		}

		// 初始化选号区域显示
		initSlideAreaShow();
	}

	/**
	 * 初始化滑动区域的显示
	 */
	public void initSlideAreaShow() {
		List<View> slideAreaViews = getSlideAreaViewList();
		setSlideAreaViewPagerAdapter(slideAreaViews);
	}

	/**
	 * 获取滑动区域的视图集合
	 * 
	 * @return 滑动区域视图对象
	 */
	protected List<View> getSlideAreaViewList() {
		// 滑动区域视图数组
		List<View> slideAreaViewList = new ArrayList<View>();

		// 设置滑动区域的视图选号面板属性
		for (int page_i = 0; page_i < VIEWPAGE_PAGE_NUM; page_i++) {
			View slideAreaView = getAndInitSlideAreaViewShow(page_i);
			slideAreaViewList.add(slideAreaView);
		}

		return slideAreaViewList;
	}

	/**
	 * 根据布局资源Id获取滑动区域布局对象
	 * 
	 * @return 视图对象
	 */
	protected View getSlideAreaViewWithLayoutResourceId(int aLayoutResourceId) {
		View selectNumberAreaView = _fLayoutInflater.inflate(aLayoutResourceId,
				null);
		return selectNumberAreaView;
	}

	/**
	 * 设置滑动ViewPager适配器
	 * 
	 * @param selectNumberAreaViews
	 *            滑动区域视图集合
	 */
	private void setSlideAreaViewPagerAdapter(List<View> selectNumberAreaViews) {
		_fSelectNumberAdapter = new ViewPagerAdapter(selectNumberAreaViews);
		_fSlideAreaViewPager.setAdapter(_fSelectNumberAdapter);
		_fSlideAreaViewPager.setCurrentItem(0);
	}
}
