package com.ruyicai.android.controller.activity.viewpagers;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.compontent.button.PageChangeRadioButtons;
import com.ruyicai.android.controller.compontent.button.PageChangeRadioButtonsInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.textview.PlayMethodTextView;

/* 选号页面基类：包含滑动区域和底部投注栏。
 *
 * @author xiang_000
 * @since RYC1.0 2013-4-21
 */
public abstract class SelectNumberActivity extends ViewPagesActivity {
	/** 选号面板集合 */
	protected List<ArrayList<SelectNumberPanel>>	_fSelectNumberPanelList;
	/** 页面中选号面板的个数 */
	protected int									_fNumOfSelectNumberPanel;

	/** 页面切换单选按钮组 */
	protected PageChangeRadioButtons				_fPageChangeRadioButtons;
	/** 玩法说明文本框 */
	protected PlayMethodTextView					_fPlayMethodTextView;

	/** 页面切换单选按钮接口 */
	private PageChangeRadioButtonsInterface			_fPageChangeRadioButtonsInterface;
	/** 玩法文本框接口 */
	private PlayMethodTextViewInterface				_fPlayMethodTextViewInterface;
	
	public PageChangeRadioButtonsInterface get_fPageChangeRadioButtonsInterface() {
		return _fPageChangeRadioButtonsInterface;
	}

	public void set_fPageChangeRadioButtonsInterface(
			PageChangeRadioButtonsInterface _fPageChangeRadioButtonsInterface) {
		this._fPageChangeRadioButtonsInterface = _fPageChangeRadioButtonsInterface;
	}

	public PlayMethodTextViewInterface get_fPlayMethodTextViewInterface() {
		return _fPlayMethodTextViewInterface;
	}

	public void set_fPlayMethodTextViewInterface(
			PlayMethodTextViewInterface _fPlayMethodTextViewInterface) {
		this._fPlayMethodTextViewInterface = _fPlayMethodTextViewInterface;
	}


	{
		_fSelectNumberPanelList = new ArrayList<ArrayList<SelectNumberPanel>>();
	}

	@Override
	protected boolean IsAddBettingBar() {
		return true;
	}

	@Override
	protected void addPageChangeRadioButtons() {
		_fPageChangeRadioButtons = new PageChangeRadioButtons(this);
		_fPageChangeRadioButtons.setId(1);
		android.widget.RelativeLayout.LayoutParams relativeLayoutParams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeLayoutParams.addRule(RelativeLayout.ALIGN_TOP);
		relativeLayoutParams.setMargins(5, 0, 0, 0);
		_fContainerRelativeLayout.addView(_fPageChangeRadioButtons,
				relativeLayoutParams);

		((android.widget.RelativeLayout.LayoutParams) _fSlideAreaViewPager
				.getLayoutParams()).addRule(RelativeLayout.BELOW,
				_fPageChangeRadioButtons.getId());

		_fPageChangeRadioButtonsInterface
				.setPageChangeRadioButtonTextResouceIds();
		_fPageChangeRadioButtons.initPageChangeButtonsShow();

		_fPageChangeRadioButtons
				.setOnCheckedChangedListener(new PageChangeRadioButtonsOnCheckedChangedListener());
	}

	@Override
	protected void addPlayMethodTextView() {
		_fPlayMethodTextView = new PlayMethodTextView(this);
		_fPlayMethodTextView.setId(2);
		android.widget.RelativeLayout.LayoutParams relativeLayoutParams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeLayoutParams.setMargins(5, 0, 0, 0);

		if (isAddPageChangeRadioButtons()) {
			relativeLayoutParams.addRule(RelativeLayout.BELOW,
					_fPageChangeRadioButtons.getId());
		} else {
			relativeLayoutParams.addRule(RelativeLayout.ALIGN_TOP);
		}

		_fContainerRelativeLayout.addView(_fPlayMethodTextView,
				relativeLayoutParams);

		((android.widget.RelativeLayout.LayoutParams) _fSlideAreaViewPager
				.getLayoutParams()).addRule(RelativeLayout.BELOW,
				_fPlayMethodTextView.getId());

		_fPlayMethodTextViewInterface.setPlayMethodTextViewContent();
	}

	/**
	 * 设置选号面板的个数
	 */
	protected abstract void setSelectNumberPanelNum();

	/**
	 * 根据页面索引初始化选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 */
	protected abstract void initSelectNumberPanelsWithPage(int aPage_i);

	@Override
	protected View getAndInitSlideAreaViewShow(int aPage_i) {
		// 使用布局资源id获取滑动页面视图
		View selectNumberAreaView = getSlideAreaViewWithLayoutResourceId(R.layout.selectnumber_viewpager_item);

		// 获取选号面板容器
		LinearLayout containerLinearLayout = (LinearLayout) selectNumberAreaView
				.findViewById(R.id.selectnumber_linearlayout_selectnumberpanelcontainer);

		// 创建并向滑动区域添加选号面板
		setSelectNumberPanelNum();
		createAndAddSelectNumberPanelsToSlideArea(containerLinearLayout);

		// 根据索引初始化选号面板
		initSelectNumberPanelsWithPage(aPage_i);

		return selectNumberAreaView;
	}

	/**
	 * 创建并向滑动区域添加选号面板
	 * 
	 * @param containerLinearLayout
	 *            容器布局
	 */
	protected void createAndAddSelectNumberPanelsToSlideArea(
			LinearLayout containerLinearLayout) {
		// 创建当前页面的选号面板集合
		ArrayList<SelectNumberPanel> pageSelectNumberPanelList = new ArrayList<SelectNumberPanel>();

		for (int panel_i = 0; panel_i < _fNumOfSelectNumberPanel; panel_i++) {
			// 创建选号面板，并添加到选号面板集合中
			SelectNumberPanel selectNumberPanel = new SelectNumberPanel(this);

			// 将选号面板添加到滑动区域中
			addSelectNumberPanelToSlideArea(containerLinearLayout,
					selectNumberPanel);

			// 将选号面板添加到当前页面的选号面板集合中
			pageSelectNumberPanelList.add(selectNumberPanel);
		}

		// 将当前滑动页面选号面板集合添加到总集合中
		_fSelectNumberPanelList.add(pageSelectNumberPanelList);
	}

	/**
	 * 将选号面板添加到容器中
	 * 
	 * @param containerLinearLayout
	 *            容器对象
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	protected void addSelectNumberPanelToSlideArea(
			LinearLayout containerLinearLayout,
			SelectNumberPanel selectNumberPanel) {
		// 设置选号面板布局属性，并添加到容器中
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = 10;
		layoutParams.leftMargin = 5;
		layoutParams.rightMargin = 5;
		containerLinearLayout.addView(selectNumberPanel, layoutParams);
	}

	/**
	 * 单选按钮组选中状态监听器实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-30
	 */
	class PageChangeRadioButtonsOnCheckedChangedListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			_fSelectNumberPanelList.clear();
			_fPlayMethodTextViewInterface.setPlayMethodTextViewContent();
			initSlideAreaShow();
		}
	}
}
