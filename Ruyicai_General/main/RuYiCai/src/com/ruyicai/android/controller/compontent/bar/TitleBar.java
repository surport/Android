package com.ruyicai.android.controller.compontent.bar;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.compontent.dropdownmenu.TitleDropDownMenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 标题栏自定义控件
 * 
 * @author PengCX
 * @since RYC1.0 2013-3-18
 */
public class TitleBar extends RelativeLayout {

	/** 标题 */
	public TextView				_fTitleTextView;
	/** 下拉菜单对象 */
	public TitleDropDownMenu	_fDropDownMenu;
	/** 登录注册按钮 */
	public Button				_fLoginOrRegistButton;
	/** 下拉按钮 */
	public Button				_fSpreadButton;

	/** 上下文对象 */
	protected Context			_fContext;
	/** 标题栏接口 */
	private TitleBarInterface	_fTitleBarInterface;

	public TitleBar(Context aContext) {
		super(aContext);
		_fContext = aContext;
	}

	/**
	 * 构造方法
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aAttributeSet
	 *            属性对象
	 */
	public TitleBar(Context aContext, AttributeSet aAttributeSet) {
		super(aContext, aAttributeSet);
		_fContext = aContext;

		LayoutInflater layoutInflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.title_bar, this);

		_fTitleTextView = (TextView) findViewById(R.id.titlebar_textview_title);
		_fLoginOrRegistButton = (Button) findViewById(R.id.titlebar_button_loginorregister);
		_fSpreadButton = (Button) findViewById(R.id.titlebar_button_dropdown);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void set_fTitleBarInterface(TitleBarInterface aTitleBarInterface) {
		_fTitleBarInterface = aTitleBarInterface;
	}

	/**
	 * 初始化标题栏的显示
	 */
	public void initTitleBarShow() {
		_fTitleBarInterface.setTitleTextView();
		_fTitleBarInterface.setTitleButton();
	}
}
