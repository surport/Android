package com.ruyicai.android.controller.compontent.dropdownmenu;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.adapter.gridview.RandomButtonGridViewAdapter;
import com.ruyicai.android.controller.compontent.button.RandomSelectNumberButton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

/**
 * 随机选号下拉菜单：选择随机选号的个数
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-12
 */
public class RandomButtonDropDownMenu {
	/** 上下文对象 */
	private Context						_fContext;
	/** 产生下拉菜单的按钮 */
	private RandomSelectNumberButton	_fRandomSelectNumberButton;
	/** popupwindow对象 */
	private PopupWindow					_fPopupWindow;
	/** 选号按钮网格布局 */
	private GridView					_fSelectButtonGridView;

	/** 下拉菜单选择随机号码的最小个数：默认为6个 */
	private int							_fMinRandomNum		= 6;
	/** 下拉菜单选择按钮的个数：默认为9个 */
	private int							_fSelectButtonNum	= 9;

	/**
	 * 设置产生下拉菜单的按钮
	 * 
	 * @param _fRandomSelectNumberButton
	 *            产生下拉菜单的按钮
	 */
	public void set_fRandomSelectNumberButton(
			RandomSelectNumberButton _fRandomSelectNumberButton) {
		this._fRandomSelectNumberButton = _fRandomSelectNumberButton;
	}

	public RandomButtonDropDownMenu(Context aContext, int aMinRandomNum,
			int aSelectBallNum) {
		setAttributes(aContext, aMinRandomNum, aSelectBallNum);

		View contentView = setDropDownMenuLayout();

		initSelectButtonsShow(contentView);
	}

	/**
	 * 初始化选择按钮的显示
	 * 
	 * @param contentView
	 *            下拉列表布局视图对象
	 */
	private void initSelectButtonsShow(View contentView) {
		if (_fPopupWindow == null) {
			_fPopupWindow = new PopupWindow(contentView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			// 不设置该属性，无法响应onItemClickListener事件
			_fPopupWindow.setFocusable(true);
		}

		_fSelectButtonGridView = (GridView) contentView
				.findViewById(R.id.randomdropdownmenu_gridview);
		_fSelectButtonGridView.setAdapter(new RandomButtonGridViewAdapter(
				_fContext, _fMinRandomNum, _fSelectButtonNum));
		_fSelectButtonGridView
				.setOnItemClickListener(new GridViewOnItemClickListener());
	}

	/**
	 * 获取下拉列表的布局视图对象
	 * 
	 * @return 下拉列表布局视图对象
	 */
	private View setDropDownMenuLayout() {
		LayoutInflater layoutInflater = (LayoutInflater) _fContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(R.layout.random_dropdownmenu,
				null);
		return contentView;
	}

	/**
	 * 设置相关属性
	 * 
	 * @param aContext
	 *            上下文对象
	 */
	private void setAttributes(Context aContext, int aMinRandomNum,
			int aSelectButtonNum) {
		_fContext = aContext;
		_fMinRandomNum = aMinRandomNum;
		_fSelectButtonNum = aSelectButtonNum;
	}

	/**
	 * 获取选择随机按钮个数
	 * 
	 * @return 选择随机按钮个数
	 */
	public int get_fSelectButtonNum() {
		return _fSelectButtonNum;
	}

	/**
	 * 设置选择随机号码个数
	 * 
	 * @param _fSelectButtonNum
	 *            选择随机号码个数
	 */
	public void set_fSelectButtonNum(int _fSelectButtonNum) {
		this._fSelectButtonNum = _fSelectButtonNum;
	}

	/**
	 * 显示随机按钮下拉菜单
	 * 
	 * @param aView
	 *            显示菜单相对视图位置
	 */
	public void ShowAsRandomButtonDropDownMenu(View aView) {
		_fPopupWindow.setOutsideTouchable(true);

		if (!_fPopupWindow.isShowing() && _fPopupWindow != null) {
			_fPopupWindow.showAsDropDown(aView);
		}
	}

	/**
	 * 关闭随机按钮下拉菜单
	 */
	public void dismissRandomButtonDropDownMenu() {
		if (_fPopupWindow != null && _fPopupWindow.isShowing()) {
			_fPopupWindow.dismiss();
		}
	}

	/**
	 * 随机按钮下拉菜单网格列表选项点击事件监听实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-13
	 */
	class GridViewOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String randomNum = (String) ((Button) view).getText();
			_fRandomSelectNumberButton.set_fRandomSelectNum(Integer
					.valueOf(randomNum));
			dismissRandomButtonDropDownMenu();
		}
	}
}
