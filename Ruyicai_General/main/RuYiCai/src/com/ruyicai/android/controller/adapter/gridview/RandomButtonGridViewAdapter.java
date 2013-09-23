package com.ruyicai.android.controller.adapter.gridview;

import com.ruyicai.android.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 * 随机按钮下拉列表网格布局适配器：负责随机按钮下拉菜单中选择随机号码个数的列表的显示，设置最小的随机号码的个数
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-13
 */
public class RandomButtonGridViewAdapter extends BaseAdapter {
	/** 上下文对象 */
	private Context	_fContext;
	/** 最小的随机数的个数 */
	private int		_fMinRandomNum;
	/** 选择随机个数按钮的个数 */
	private int		_fSelectButtonNum;

	/**
	 * 构造函数
	 * 
	 * @param _fContext
	 *            上下文对象
	 * @param _fSelectButtonNum
	 *            随机按钮个数
	 */
	public RandomButtonGridViewAdapter(Context aContext, int aMinRandomNum,
			int aSelectButtonNum) {
		super();
		_fContext = aContext;
		_fMinRandomNum = aMinRandomNum;
		_fSelectButtonNum = aSelectButtonNum;
	}

	@Override
	public int getCount() {
		return _fSelectButtonNum;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Button button;

		if (convertView == null) {
			button = new Button(_fContext);
			// 不设置该属性，在PupWindow中的GridView的onItemClickListener事件无法响应
			button.setFocusable(false);
			button.setClickable(false);
			button.setLayoutParams(new GridView.LayoutParams(40, 40));
			button.setBackgroundResource(R.drawable.randomdropdownmenu_button_selector);
		} else {
			button = (Button) convertView;
		}

		button.setText(String.valueOf(_fMinRandomNum + position));

		return button;
	}

}
