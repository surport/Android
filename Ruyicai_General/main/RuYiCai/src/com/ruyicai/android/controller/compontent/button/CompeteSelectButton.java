package com.ruyicai.android.controller.compontent.button;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 竞彩选择按钮自定义控件
 * 
 * @author Administrator
 * @since RYC1.0 2013-7-11
 */
public class CompeteSelectButton extends FrameLayout {

	/** 球队按钮 */
	public CheckBox	_fTeamButton;
	/** 赔率文本框 */
	public TextView	_fSPTextView;

	public CompeteSelectButton(Context aContext) {
		super(aContext);
	}

	public CompeteSelectButton(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);

		LayoutInflater layoutInflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.compete_selectbutton, this);

		_fTeamButton = (CheckBox) findViewById(R.id.compete_selectbutton_team);
		_fSPTextView = (TextView) findViewById(R.id.compete_selectbutton_sp);
	}

	public CompeteSelectButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置球队名称
	 * 
	 * @param aTeamTextString
	 *            球队的名称字符串
	 */
	public void setTeamText(String aTeamTextString) {
		_fTeamButton.setText(aTeamTextString);
	}

	/**
	 * 设置赔率
	 * 
	 * @param aSPTextString
	 *            赔率字符串
	 */
	public void setSPText(String aSPTextString) {
		_fSPTextView.setText(aSPTextString);
	}
}
