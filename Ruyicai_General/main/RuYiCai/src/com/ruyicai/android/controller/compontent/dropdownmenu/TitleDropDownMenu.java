package com.ruyicai.android.controller.compontent.dropdownmenu;

import com.ruyicai.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 标题下拉菜单类：提供下拉菜单，在点击的按钮下方显示，默认的情况下，所有的按钮都显示。用户各个彩种的投注界面标题栏下拉展示按钮点击后显示
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-6
 */
public class TitleDropDownMenu {
	/** 上下文对象 */
	private Context		_fContext;
	/** popupwindow对象 */
	private PopupWindow	_fPopupWindow;
	/** 玩法介绍按钮 */
	private Button		_fPlayMethodIntroduceButton;
	/** 历史开奖按钮 */
	private Button		_fHistoryRunLotteryButton;
	/** 开奖走势按钮 */
	private Button		_fRunLotteryTreadButton;
	/** 投注查询按钮 */
	private Button		_fBetQueryButton;
	/** 幸运选号按钮 */
	private Button		_fLuckSelectNumberButton;
	/** 模拟选号按钮 */
	private Button		_fSimulateSelectNumberButton;

	/**
	 * 构造函数
	 * 
	 * @param contentView
	 *            下拉列菜单的布局视图对象
	 * @param width
	 *            下拉菜单的宽度
	 * @param height
	 *            下拉菜单的高度
	 */
	public TitleDropDownMenu(Context aContext) {
		_fContext = aContext;

		LayoutInflater layoutInflater = (LayoutInflater) _fContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(R.layout.title_dropdownmenu,
				null);
		if (_fPopupWindow == null) {
			_fPopupWindow = new PopupWindow(contentView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}

		_fPlayMethodIntroduceButton = (Button) contentView
				.findViewById(R.id.popupwindow_button_playmethodintroduce);
		_fHistoryRunLotteryButton = (Button) contentView
				.findViewById(R.id.popupwindow_button_historyrunlotter);
		_fRunLotteryTreadButton = (Button) contentView
				.findViewById(R.id.popupwindow_button_historyrunlotter);
		_fBetQueryButton = (Button) contentView
				.findViewById(R.id.popupwindow_button_betquery);
		_fLuckSelectNumberButton = (Button) contentView
				.findViewById(R.id.popupwindow_button_luckselectnumber);
		_fSimulateSelectNumberButton = (Button) contentView
				.findViewById(R.id.popupwindow_button_simulateselectnumber);
	}

	/**
	 * 设置下拉菜单选项的可见性（钩子方法，如果子类有不同，则覆盖）：默认情况下，所有的选项都是可见的
	 */
	public void setMenueItemVisibility() {

	}

	/**
	 * 显示下拉菜单
	 * 
	 * @param view
	 */
	public void ShowAsDropDownMenu(View aView) {
		setMenueItemVisibility();
		setMenueButtonEvents();

		_fPopupWindow.setOutsideTouchable(true);
		if (!_fPopupWindow.isShowing() && _fPopupWindow != null) {
			_fPopupWindow.showAsDropDown(aView);
		}
	}

	/**
	 * 关闭下拉菜单
	 */
	public void dismissDropDownMenu() {
		if (_fPopupWindow.isShowing() && _fPopupWindow != null) {
			_fPopupWindow.dismiss();
		}
	}

	/**
	 * 设置下拉菜单按钮的事件
	 */
	private void setMenueButtonEvents() {
		_fPlayMethodIntroduceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(_fContext, "你点击的玩法介绍按钮", Toast.LENGTH_LONG)
						.show();
				dismissDropDownMenu();

			}
		});

		_fHistoryRunLotteryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(_fContext, "你点击了历史开奖按钮", Toast.LENGTH_LONG)
						.show();
				dismissDropDownMenu();
			}
		});

		_fRunLotteryTreadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(_fContext, "你点击了开奖走势按钮", Toast.LENGTH_LONG)
						.show();
				dismissDropDownMenu();
			}
		});

		_fBetQueryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(_fContext, "你点击了投注查询按钮", Toast.LENGTH_LONG)
						.show();
				dismissDropDownMenu();
			}
		});

		_fLuckSelectNumberButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(_fContext, "你点击了幸运选号按钮", Toast.LENGTH_LONG)
						.show();
				dismissDropDownMenu();
			}
		});

		_fSimulateSelectNumberButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(_fContext, "你点击了模拟选号按钮", Toast.LENGTH_LONG)
						.show();
				dismissDropDownMenu();
			}
		});
	}

}
