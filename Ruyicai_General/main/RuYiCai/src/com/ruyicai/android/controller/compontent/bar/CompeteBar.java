package com.ruyicai.android.controller.compontent.bar;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 竞彩栏类：包含玩法切换按钮、赛事选择按钮和即时比分按钮。用于竞彩足球和竞彩篮球选号页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-5-4
 */
public class CompeteBar extends LinearLayout {
	/** 上下文对象 */
	private Context				_fContext;
	/** 玩法切换按钮 */
	public Button				_fPlayMehodChangeButton;
	/** 赛事选择按钮 */
	public Button				_fEventSelectButton;
	/** 即时比分按钮 */
	public Button				_fRunTimeScoreButton;

	/** 竞彩栏接口 */
	private CompeteBarInterface	_fCompeteBarInterface;

	public CompeteBarInterface get_fCompeteBarInterface() {
		return _fCompeteBarInterface;
	}

	public void set_fCompeteBarInterface(
			CompeteBarInterface _fCompeteBarInterface) {
		this._fCompeteBarInterface = _fCompeteBarInterface;
	}

	public CompeteBar(Context aContext) {
		super(aContext);
		_fContext = aContext;
	}

	public CompeteBar(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
		_fContext = aContext;

		LayoutInflater layoutInflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.compete_bar, this);

		_fPlayMehodChangeButton = (Button) findViewById(R.id.competebar_button_playmethodchange);
		_fEventSelectButton = (Button) findViewById(R.id.competebar_button_eventchange);
		_fRunTimeScoreButton = (Button) findViewById(R.id.competebar_button_realtimescore);
	}

	/**
	 * 初始化竞彩栏显示
	 */
	public void initCompeteBarShow() {
		if (_fCompeteBarInterface != null) {
			_fCompeteBarInterface.setPlayMethodChangeButton();
			_fCompeteBarInterface.setEventSelectButton();
			_fCompeteBarInterface.setRealTimeScoreButton();
		}
	}
}
