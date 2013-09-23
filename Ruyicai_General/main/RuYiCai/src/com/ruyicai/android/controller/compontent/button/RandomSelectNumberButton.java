package com.ruyicai.android.controller.compontent.button;

import java.util.List;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.compontent.dropdownmenu.RandomButtonDropDownMenu;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallsTableLayout;
import com.ruyicai.android.tools.LogTools;
import com.ruyicai.android.tools.MathTools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 随机选号按钮：实现了对随机号码个数的选择，和随即号码的选择功能。用户双色球等选号界面。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-12
 */
public class RandomSelectNumberButton extends LinearLayout {
	/** 上下文对象 */
	private Context							_fContext;
	/** 随机按钮自我对象 */
	private RandomSelectNumberButton		_fSelf;
	/** 选择随机选号个数按钮 */
	private Button							_fSelectRandomNumButton;
	/** 随机选号按钮 */
	private Button							_fRandomSelectNumberButton;
	/** 随机选号个数下拉列表 */
	private RandomButtonDropDownMenu		_fRandomButtonDropDownMenu;
	/** 控制的选号小球表格表格 */
	private SelectNumberBallsTableLayout	_fSelectNumberBallsTableLayout;

	/** 随机按钮选择的随机号码个数 */
	private int								_fRandomNumberNum;
	/** 随机按钮下拉菜单最小随机号码个数 */
	private int								_fMinRandomNum;
	/** 随机按钮下拉菜单的选择按钮个数 */
	private int								_fSelectButtonNum;

	public RandomSelectNumberButton(Context aContext, int aRandomNumberNum) {
		super(aContext);

		_fContext = aContext;
		_fRandomNumberNum = aRandomNumberNum;
		_fSelf = this;
		LayoutInflater inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.randombutton, this);

		_fSelectRandomNumButton = (Button) findViewById(R.id.randombutton_button_selectnum);
		_fSelectRandomNumButton.setText(_fRandomNumberNum
				+ _fContext.getString(R.string.randombutton_button_unit));
		_fSelectRandomNumButton
				.setOnClickListener(new SelectNumButtonClickListener());
		_fRandomSelectNumberButton = (Button) findViewById(R.id.randombutton_button_randomselect);
		_fRandomSelectNumberButton
				.setOnClickListener(new RandomSelectButtonClickListener());
	}

	public RandomSelectNumberButton(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
	}

	/**
	 * 设置随机按钮控制的选号小球表格
	 * 
	 * @param _fSelectNumberBallsTableLayout
	 *            选号小球表格
	 */
	public void set_fSelectNumberBallsTableLayout(
			SelectNumberBallsTableLayout _fSelectNumberBallsTableLayout) {
		this._fSelectNumberBallsTableLayout = _fSelectNumberBallsTableLayout;
	}

	/**
	 * 设置随机按钮下拉菜单中选择的最小的随机数个数
	 * 
	 * @param aMinRandomNum
	 *            最小的随机数个数
	 */
	public void setDropDownMenuMinRandomNum(int aMinRandomNum) {
		_fMinRandomNum = aMinRandomNum;
	}

	/**
	 * 设置随机按钮随机选择号码的个数
	 * 
	 * @param _fRandomNumberNum
	 *            随机选择号码的个数
	 */
	public void set_fRandomSelectNum(int aRandomSelectNum) {
		_fRandomNumberNum = aRandomSelectNum;
		_fSelectRandomNumButton.setText(_fRandomNumberNum
				+ _fContext.getString(R.string.randombutton_button_unit));
	}

	/**
	 * 设置随机按钮下拉菜单中随机数个数选择按钮的个数
	 * 
	 * @param _fSelectButtonNum
	 *            随机数个数选择按钮的个数
	 */
	public void setDropDownMenuSelectButtonNum(int aSelectButtonNum) {
		_fSelectButtonNum = aSelectButtonNum;
	}

	/**
	 * 选择随机号码个数按钮事件监听实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-12
	 */
	class SelectNumButtonClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (_fRandomButtonDropDownMenu == null) {
				_fRandomButtonDropDownMenu = new RandomButtonDropDownMenu(
						_fContext, _fMinRandomNum, _fSelectButtonNum);
			}
			_fRandomButtonDropDownMenu.set_fRandomSelectNumberButton(_fSelf);
			_fRandomButtonDropDownMenu.ShowAsRandomButtonDropDownMenu(v);
		}
	}

	/**
	 * 随机选号按钮事件监听器实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-12
	 */
	class RandomSelectButtonClickListener implements OnClickListener {

		private static final String	TAG	= "RandomSelectButtonClickListener";

		@Override
		public void onClick(View v) {
			// 获取小球表格的起始号码，和小球的个数
			int startNumber = _fSelectNumberBallsTableLayout
					.get_fSelectNumberBallStartNum();
			int ballNum = _fSelectNumberBallsTableLayout.get_fSelectBallNum();
			// 使用数学工具类获取随机数集合
			List<Integer> specifyNumberList = MathTools
					.getSpecifiedRangeRadomNumberWithoutRepetation(startNumber,
							startNumber + ballNum, _fRandomNumberNum);
			LogTools.showLog(TAG,
					"产生的指定小球的编号集合为：" + specifyNumberList.toString() + "，大小为："
							+ specifyNumberList.size(), LogTools.INFO);
			// 根据随机数集合指定小球表格当中的小球为选定状态
			_fSelectNumberBallsTableLayout
					.selectSpecifiedNumberBalls(specifyNumberList);
		}
	}
}
