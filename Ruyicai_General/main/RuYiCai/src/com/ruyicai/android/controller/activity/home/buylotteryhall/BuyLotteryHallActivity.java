package com.ruyicai.android.controller.activity.home.buylotteryhall;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.activity.loginorregister.LoginActivity;
import com.ruyicai.android.controller.adapter.viewpager.ViewPagerAdapter;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;
import com.ruyicai.android.controller.compontent.pagerindicator.BuyLotteryHallPageIndicator;
import com.ruyicai.android.controller.compontent.panel.BuyLotteryHallSlidePanel;
import com.ruyicai.android.model.bean.lottery.Lottery;
import com.ruyicai.android.model.bean.lottery.LotteryType;

public class BuyLotteryHallActivity extends BaseActivity implements
		OnClickListener, OnPageChangeListener {
	/** 底部栏彩票资讯按钮id */
	private static final int			BOTTOMBAR_BUTTON_LOTTERINFO_ID		= 0;
	/** 底部栏彩活动中心按钮id */
	private static final int			BOTTOMBAR_BUTTON_ACTIONCENTER_ID	= 1;
	/** 底部栏彩票幸运选号id */
	private static final int			BOTTOMBAR_BUTTON_LUCKLYPICK_ID		= 2;

	/** ViewPager滑动视图集合 */
	private List<View>					_fViewList;

	/** 引用视图：标题栏 */
	@InjectView(R.id.buylotteryhall_title_bar)
	private TitleBar					_fTitleBar;
	/** 引用视图：滑动面板 */
	@InjectView(R.id.buyloteryhall_slidepanel_viewpager)
	private ViewPager					_fPanelViewPager;
	/** 引用视图：页面指示器 */
	@InjectView(R.id.buylotteryhall_pageindicator_linearlayout)
	private BuyLotteryHallPageIndicator	_fPageIndicator;
	/** 引用视图：彩票资讯按钮 */
	@InjectView(R.id.buylotteryhall_buttombar_button_lotteryinfo)
	private Button						_fLotteryInfoButton;
	/** 引用视图：活动中心按钮 */
	@InjectView(R.id.buylotteryhall_buttombar_button_actioncenter)
	private Button						_fActionCenterButton;
	/** 引用视图： 幸运选号按钮 */
	@InjectView(R.id.buylotteryhall_buttombar_button_lucklypick)
	private Button						_fLucklyPickButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.buylotteryhall_activity);

		// 初始化标题栏显示
		_fTitleBar
				.set_fTitleBarInterface(new BuyLotteryHallTitleBarInterface());
		_fTitleBar.initTitleBarShow();

		// 初始化中部滑动面板显示
		initMiddleSlidePanelShow();

		// 初始化页面指示器显示
		initPageIndicatorShow();

		// 初始化底部栏的显示
		initButtomBarShow();
	}

	/**
	 * 初始化中部滑动面板的显示
	 */
	private void initMiddleSlidePanelShow() {
		// 获取滑动面板中页面的视图集合
		_fViewList = getSlidePannelPageViewList();

		// 创建滑动面板适配器对象，并设置适配器
		ViewPagerAdapter slidePannelAdapter = new ViewPagerAdapter(_fViewList);
		_fPanelViewPager.setAdapter(slidePannelAdapter);
		// 设置当前的页面为第2页和页面改变监听器
		_fPanelViewPager.setOnPageChangeListener(this);
		_fPanelViewPager.setCurrentItem(1);
	}

	/**
	 * 获取滑动页面的视图集合
	 * 
	 * @return 滑动页面的视图集合
	 */
	private List<View> getSlidePannelPageViewList() {
		List<View> viewList = new ArrayList<View>();
		// 获取中奖排行榜页面的视图
		View winningView = getWinningListView();
		viewList.add(winningView);

		// 获取购彩页面视图集合
		List<View> buyLotteryViews = getBuyLotteryPannelViewList();
		viewList.addAll(buyLotteryViews);

		return viewList;
	}

	/**
	 * 获取中奖排行的页面视图
	 * 
	 * @return 中奖排行页面视图
	 */
	private TextView getWinningListView() {
		// 如果是第一个页面则返回**视图
		TextView textView = new TextView(this);
		textView.setText("page1");
		return textView;
	}

	/**
	 * 获取购彩页面的视图集合：购彩页面的集合根据屏幕分辨率和彩种的变化而变化
	 * 
	 * @return 购彩页面视图集合
	 */
	private List<View> getBuyLotteryPannelViewList() {
		List<View> viewList = new ArrayList<View>();

		// 分配每个页面显示的彩种选项
		List<List<Object>> prePageItemList = distributePanelItemOfPrePage();

		int pageNums = prePageItemList.size();
		for (int page_i = 0; page_i < pageNums; page_i++) {
			BuyLotteryHallSlidePanel buyLotteryHallSlidePanel = new BuyLotteryHallSlidePanel(
					this);

			// 设置页面视图的布局属性
			LayoutParams layoutParams = new LayoutParams();
			layoutParams.width = LayoutParams.FILL_PARENT;
			layoutParams.height = LayoutParams.FILL_PARENT;
			buyLotteryHallSlidePanel.setLayoutParams(layoutParams);
			// 设置页面视图视图的内容边距
			buyLotteryHallSlidePanel.setPadding(20, 10, 20, 10);

			buyLotteryHallSlidePanel
					.initBuyLotteryHallPanelWithList(prePageItemList
							.get(page_i));
			viewList.add(buyLotteryHallSlidePanel);
		}

		return viewList;
	}

	/**
	 * 分配每页显示的彩种选项
	 * 
	 * @return 返回页面显示彩种选项集合
	 */
	private List<List<Object>> distributePanelItemOfPrePage() {
		// FIXME 根据屏幕和彩种的变化动态变化每页彩种的显示
		List<List<Object>> pageList = new ArrayList<List<Object>>();

		// 第一个页面显示的选项的对象集合，合买大厅和各个彩种
		List<Object> firstPageList = new ArrayList<Object>();
		firstPageList.add("合买大厅");
		for (LotteryType lotteryType : EnumSet.range(LotteryType.DOUBLE_BALL,
				LotteryType.ELEVEN_LUCKGOLD)) {
			Lottery lottery = new Lottery(lotteryType, false, false, false);
			firstPageList.add(lottery);
		}
		pageList.add(firstPageList);

		// 第二个页面显示的选项集合，专家荐号和各个彩种
		List<Object> secondPageList = new ArrayList<Object>();
		secondPageList.add("专家荐号");
		for (LotteryType lotteryType : EnumSet.range(
				LotteryType.GUANGDONG_ELEVENE_SELECT_FIVE,
				LotteryType.COMPETE_BASKETBALL)) {
			Lottery lottery = new Lottery(lotteryType, false, false, false);
			secondPageList.add(lottery);
		}
		pageList.add(secondPageList);

		return pageList;
	}

	/**
	 * 初始化页面指示器的显示
	 */
	private void initPageIndicatorShow() {
		_fPageIndicator.setPageNums(_fViewList.size());
	}

	/**
	 * 初始化底部栏的显示
	 */
	private void initButtomBarShow() {
		// 设置按钮的事件监听器
		_fLotteryInfoButton.setOnClickListener(this);
		_fActionCenterButton.setOnClickListener(this);
		_fLucklyPickButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = Integer.valueOf(v.getTag().toString());
		switch (id) {
		case BOTTOMBAR_BUTTON_LOTTERINFO_ID:
			goToDestinationScreen(LotteryInformationActivity.class);
			break;
		case BOTTOMBAR_BUTTON_ACTIONCENTER_ID:
			goToDestinationScreen(ActionCenterActivity.class);
			break;
		case BOTTOMBAR_BUTTON_LUCKLYPICK_ID:
			goToDestinationScreen(LucklyPickActivity.class);
			break;
		}
	}

	/**
	 * 跳转到目标页面
	 * 
	 * @param aClass
	 *            页面的Activity类
	 */
	private void goToDestinationScreen(Class<?> aClass) {
		Intent intent = new Intent(BuyLotteryHallActivity.this, aClass);
		startActivity(intent);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		// 当一个新的页面被选中的时候调用
		_fPageIndicator.setPresentPage(arg0);
	}

	/**
	 * 购彩大厅显示标题栏接口类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-9
	 */
	class BuyLotteryHallTitleBarInterface implements TitleBarInterface {
		@Override
		public void setTitleTextView() {
			_fTitleBar._fTitleTextView
					.setText(R.string.buylotteryhall_titlebar_text);
		}

		@Override
		public void setTitleButton() {
			_fTitleBar._fLoginOrRegistButton.setVisibility(View.VISIBLE);
			_fTitleBar._fLoginOrRegistButton
					.setText(R.string.buylotteryhall_titlebar_buttontext);
			_fTitleBar._fLoginOrRegistButton
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									BuyLotteryHallActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
					});
		}
	}

}
