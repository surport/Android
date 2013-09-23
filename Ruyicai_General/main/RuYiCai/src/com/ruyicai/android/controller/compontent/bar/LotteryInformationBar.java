package com.ruyicai.android.controller.compontent.bar;

import com.ruyicai.android.R;
import com.ruyicai.android.model.bean.lottery.Lottery;
import com.ruyicai.android.model.bean.lottery.LotteryType;
import com.ruyicai.android.model.intenet.home.buylotteryhall.HistoryOfLotteryInfoInterface;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 开奖信息显示栏：显示上期开奖号码，开奖期号和截止时间。用于各个彩种的选号页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-6
 */
public class LotteryInformationBar extends RelativeLayout {
	/** 上下文对象 */
	private Context		_fContext;
	/** 上期开奖号码 */
	private TextView	_fLastIssueNumbersTextView;
	/** 开奖期号 */
	private TextView	_fIssueNumberTextView;
	/** 截止时间 */
	private TextView	_fDeadTimeTextView;

	/** 彩种类型 */
	private LotteryType	_fLotteryTypeEnum;

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 */
	public LotteryInformationBar(Context aContext) {
		super(aContext);
	}

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aAttrs
	 *            属性对象
	 */
	public LotteryInformationBar(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);

		_fContext = aContext;

		LayoutInflater inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.lotteryinformation_bar, this);

		_fLastIssueNumbersTextView = (TextView) findViewById(R.id.lotteryinformationbar_textview_lastissuenumbers);
		_fIssueNumberTextView = (TextView) findViewById(R.id.lotteryinformationbar_textview_issuenumber);
		_fDeadTimeTextView = (TextView) findViewById(R.id.lotteryinformationbar_textview_deadtime);
	}

	public LotteryInformationBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 根据彩种的类型初始化彩种信息栏的显示
	 * 
	 * @param aLotteryType
	 *            彩种类型枚举
	 */
	public void initLotteryInformationBarShow(LotteryType aLotteryType) {
		Thread thread = new Thread();
		
		// 获取彩种信息栏显示上期开奖号码，期数为一期
		HistoryOfLotteryInfoInterface historyOfLotteryInfoInterface = new HistoryOfLotteryInfoInterface(
				aLotteryType.get_fLotteryNumber(), "1", "1");
		historyOfLotteryInfoInterface
				.getDataFromIntenet(_fContext);
	}

}
