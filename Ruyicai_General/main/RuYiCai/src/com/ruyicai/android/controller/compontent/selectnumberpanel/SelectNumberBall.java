package com.ruyicai.android.controller.compontent.selectnumberpanel;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 选号小球类：实现了选号的点击图片更换，遗漏值的显示功能
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-9
 */
public class SelectNumberBall extends LinearLayout {
	/** 上下文对象 */
	private Context					_fContext;
	/** 选号球单选按钮 */
	private CheckBox				_fSelectBallCheckBox;
	/** 编号文本框 */
	private TextView				_fNumberTextView;
	/** 遗漏值文本框 */
	private TextView				_fLossValueTextView;

	/** 小球编号：默认标号为0 */
	private String					_fNumber				= "0";
	/** 小球遗漏值：默认遗漏值为0 */
	private String					_fLossValue				= "0";
	/** 选号球类型：默认为红球 */
	private SelectNumberBallType	_fSelectNumberBallType	= SelectNumberBallType.REDBALL;
	/** 是否显示遗漏值：默认显示遗漏值 */
	private boolean					_fIsShowLossValue		= true;

	/**
	 * 获取选号小球的编号
	 * 
	 * @return 选号小球编号
	 */
	public String get_fNumber() {
		return _fNumber;
	}

	public SelectNumberBall(Context aContext) {
		super(aContext);
		_fContext = aContext;

		LayoutInflater layoutInflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.selectnumber_ball, this);

		_fSelectBallCheckBox = (CheckBox) findViewById(R.id.selectnumberball_checkbox_selectball);
		_fSelectBallCheckBox
				.setOnCheckedChangeListener(new SelectNumberBallSelectedListener());
		_fNumberTextView = (TextView) findViewById(R.id.selectnumberball_textview_number);
		_fLossValueTextView = (TextView) findViewById(R.id.selectnumberball_textview_lossvalue);
	}

	public SelectNumberBall(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
		_fContext = aContext;
	}

	/**
	 * 设置选号球的数字
	 * 
	 * @param aNumber
	 *            选号球数字字符串
	 */
	public void setNumber(String aNumber) {
		_fNumber = aNumber;
		_fNumberTextView.setText(aNumber);
	}

	/**
	 * 设置选号球的遗漏值：如果遗漏值可见，则显示遗漏值；如果遗漏值不可见，则设置为不可见
	 * 
	 * @param aLossValue
	 *            遗漏值字符串
	 * @param aIsShowLossValue
	 *            是否显示遗漏值标识
	 */
	public void setLossValue(String aLossValue, Boolean aIsShowLossValue) {
		_fIsShowLossValue = aIsShowLossValue;
		_fLossValue = aLossValue;

		if (_fIsShowLossValue) {
			_fLossValueTextView.setText(aLossValue);
		} else {
			_fLossValueTextView.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置选号小球的种类：根据小球的种类，设置不同的图片选择器
	 * 
	 * @param aSelectNumberBallType
	 *            选号小球种类枚举
	 */
	public void setSelectNumberBallType(
			SelectNumberBallType aSelectNumberBallType) {
		_fSelectNumberBallType = aSelectNumberBallType;
		switch (aSelectNumberBallType) {
		case REDBALL:
			// FIXME 图片的大小不合适，应该处理一下
			_fSelectBallCheckBox
					.setBackgroundResource(R.drawable.selectnumberball_red_selector);
			break;
		case BLUEBALL:
			_fSelectBallCheckBox
					.setBackgroundResource(R.drawable.selectnumberball_blue_selector);
			break;

		}
	}

	/**
	 * 检查该选号小球当前是否被选中
	 * 
	 * @return 是否被选中标识
	 */
	public boolean isSelected() {
		if (_fSelectBallCheckBox.isChecked()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取选球的号码
	 * 
	 * @return 选球小球的号码
	 */
	public Integer getNumber() {
		return Integer.valueOf((String) _fNumberTextView.getText());
	}

	/**
	 * 取消小球的选中状态
	 */
	public void cancelSelected() {
		if (_fSelectBallCheckBox.isChecked()) {
			_fSelectBallCheckBox.setChecked(false);
		}
	}

	/**
	 * 设置小球为选中状态
	 */
	public void setSelected() {
		if (!_fSelectBallCheckBox.isChecked()) {
			_fSelectBallCheckBox.setChecked(true);
		}
	}

	/**
	 * 选号小球选择选择事件监听类：当按钮的选中状态发生变化的时候 ，选号小球号码的颜色进行变化：选中为变色，未选中为黑色。
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-16
	 */
	class SelectNumberBallSelectedListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				// 如果为选中状态，则号码颜色为白色
				_fNumberTextView.setTextColor(getResources().getColor(
						R.color.white));
			} else {
				// 如果为选中状态，则号码颜色为黑色
				_fNumberTextView.setTextColor(getResources().getColor(
						R.color.black));
			}

		}

	}
}
