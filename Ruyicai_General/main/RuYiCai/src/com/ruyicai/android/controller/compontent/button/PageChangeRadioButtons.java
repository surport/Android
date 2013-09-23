package com.ruyicai.android.controller.compontent.button;

import com.ruyicai.android.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 页面切换按钮：使用单选按钮组合，来进行页面的内容切换。用户福彩3D直选、组三、组六页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-29
 */
public class PageChangeRadioButtons extends LinearLayout {
	/** 上下文对象 */
	private Context							_fContext;
	/** 单选按钮组 */
	private RadioGroup						_fRadioGroup;
	/** 单选按钮文本字符串资源数组 */
	private int[]							_fRadioButtonTextResouceIds;
	/** 页面切换按钮接口对象 */
	private PageChangeRadioButtonsInterface	_fChangeRadioButtonsInterface;

	public void set_fRadioButtonTextResouceIds(int[] _fRadioButtonTextResouceIds) {
		this._fRadioButtonTextResouceIds = _fRadioButtonTextResouceIds;
	}

	public int[] get_fRadioButtonTextResouceIds() {
		return _fRadioButtonTextResouceIds;
	}

	public PageChangeRadioButtonsInterface get_fChangeRadioButtonsInterface() {
		return _fChangeRadioButtonsInterface;
	}

	public void set_fChangeRadioButtonsInterface(
			PageChangeRadioButtonsInterface _fChangeRadioButtonsInterface) {
		this._fChangeRadioButtonsInterface = _fChangeRadioButtonsInterface;
	}

	{
		setBackgroundResource(R.drawable.pagechangebutton_background);
		setGravity(Gravity.CENTER_HORIZONTAL);
	}

	public PageChangeRadioButtons(Context aContext) {
		super(aContext);
		_fContext = aContext;
	}

	public PageChangeRadioButtons(Context context, AttributeSet attrs) {
		super(context, attrs);
		_fContext = context;
	}

	/**
	 * 初始化页面切换按钮的显示，使用文本的字符串资源Id作为控件的ID
	 */
	public void initPageChangeButtonsShow() {

		_fRadioGroup = new RadioGroup(_fContext);
		_fRadioGroup.setOrientation(HORIZONTAL);
		addView(_fRadioGroup);

		int buttonNum = _fRadioButtonTextResouceIds.length;
		for (int button_i = 0; button_i < buttonNum; button_i++) {
			RadioButton radioButton = new RadioButton(_fContext);
			// 使用文本的字符串资源Id作为控件的ID
			radioButton.setId(_fRadioButtonTextResouceIds[button_i]);
			radioButton.setButtonDrawable(R.drawable.radiobutton_selector);
			radioButton.setTextColor(Color.BLACK);
			radioButton.setTextSize(13);
			radioButton.setText(_fRadioButtonTextResouceIds[button_i]);

			// 默认选中第一个
			if (button_i == 0) {
				radioButton.setChecked(true);
			}

			_fRadioGroup.addView(radioButton);
		}
	}

	/**
	 * 设置单选按钮组的选中变化监听器
	 */
	public void setOnCheckedChangedListener(
			OnCheckedChangeListener aOnCheckedChangeListener) {
		_fRadioGroup.setOnCheckedChangeListener(aOnCheckedChangeListener);
	}

	/**
	 * 获取被选中的单选按钮的Id
	 * 
	 * @return 被选中的单选按钮的Id
	 */
	public int getCheckedRadioButtonId() {
		return _fRadioGroup.getCheckedRadioButtonId();
	}
}
