package com.ruyicai.android.controller.compontent.bar;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 竞彩投注栏
 * 
 * @author Administrator
 * @since RYC1.0 2013-8-6
 */
public class CompeteBetBar extends RelativeLayout {
	/** 上下文对象 */
	private Context					_fContext;
	/** 重选按钮 */
	private Button					_fReSelectButton;
	/** 选择场次文本框 */
	private TextView				_fSelectedNumberTextView;
	/** 立即投注按钮 */
	private Button					_fRecentlyBetButton;
	/** 竞彩投注栏接口 */
	private CompeteBetBarInterface	_fCompeteBetBarInterface;

	{
		// 初始化代码块
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.competebetbar, this);

		_fReSelectButton = (Button) findViewById(R.id.competebetbar_reselect_button);
		_fSelectedNumberTextView = (TextView) findViewById(R.id.competebar_selectednum_textview);
		_fRecentlyBetButton = (Button) findViewById(R.id.competebetbar_recentlybet_button);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文对象
	 */
	public CompeteBetBar(Context context) {
		super(context);
		_fContext = context;
	}

	/**
	 * 构造方法
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aAttributeSet
	 *            属性对象
	 */
	public CompeteBetBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_fContext = context;
	}

	/**
	 * 构造方法
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aAttributeSet
	 *            属性对象
	 */
	public CompeteBetBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		_fContext = context;
	}
}
