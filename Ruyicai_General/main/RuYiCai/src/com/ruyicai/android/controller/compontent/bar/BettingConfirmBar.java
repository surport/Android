package com.ruyicai.android.controller.compontent.bar;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * 投注确认栏：包含投注确认和取消按钮
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-19
 */
public class BettingConfirmBar extends RelativeLayout {

	/** 上下文对象 */
	private Context						_fContext;
	/** 投注确认按钮 */
	private Button						_fBettingConfirmButton;
	/** 投注取消按钮 */
	private Button						_fBettingCancelButton;
	/** 投注确认栏接口 */
	private BettingConfirmBarInterface	_fBettingConfirmBarInterface;

	public void set_fBettingConfirmBarInterface(
			BettingConfirmBarInterface aBettingConfirmBarInterface) {
		_fBettingConfirmBarInterface = aBettingConfirmBarInterface;
	}

	public BettingConfirmBar(Context aContext) {
		super(aContext);
		_fContext = aContext;
	}

	public BettingConfirmBar(Context aContext, AttributeSet aAttributeSet) {
		super(aContext, aAttributeSet);
		_fContext = aContext;

		LayoutInflater layoutInflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.bettingconfirm_bar, this);
		_fBettingConfirmButton = (Button) findViewById(R.id.bettingconfirmbar_button_confirm);
		_fBettingCancelButton = (Button) findViewById(R.id.bettingconfirmbar_btton_cancle);
	}

	public BettingConfirmBar(Context aContext, AttributeSet aAttrs,
			int aDefStyle) {
		super(aContext, aAttrs, aDefStyle);
		_fContext = aContext;
	}

	/**
	 * 初始化投注确认栏的显示
	 */
	public void initBettingConfirmBarShow() {
		_fBettingConfirmBarInterface.setBettingConfirmButton();
		_fBettingConfirmBarInterface.setBettingCancelButton();
	}

}
