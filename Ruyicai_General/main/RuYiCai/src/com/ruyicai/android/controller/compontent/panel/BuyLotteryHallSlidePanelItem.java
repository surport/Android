package com.ruyicai.android.controller.compontent.panel;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.arrange3.ArrangeThreeActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.arrange5.ArrangeFiveActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.compete.basketball.CompeteBasketBallActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.compete.football.CompeteFootballActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.constantly.ConstantlyActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.doubleball.DoubleBallActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.elevenluckgold.ElevenLuckGoldActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.football.FootBallActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.guangdong11xuan5.GuangDongElevenSelectFiveActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.happyevery.GuangDongHappyVeryActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.sevenhappy.ServenHappyActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.sevenstar.ServenStarActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.superlotto.SuperLottoActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.twentytwoselectfive.TwentyTwoSelectFiveActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.welfare3d.Welfare3DActivityGroup;
import com.ruyicai.android.model.bean.lottery.Lottery;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 彩种展示面板控件面板项目控件：购彩大厅内，各个彩种的展示选项，一个彩种就是一个选项 1.彩种图标和标题的显示 2.彩种开奖和停售标题的显示
 * 3.彩种展示事件点击事件的处理
 * 
 * @author Administrator
 * @since RYC1.0 2013-3-20
 */
public class BuyLotteryHallSlidePanelItem extends RelativeLayout {
	/** 上下文对象 */
	private Context		_fContext;

	/** 彩种图标 */
	private ImageView	_fLotteryIcoImageView;
	/** 今日开奖或暂停销售图标 */
	private ImageView	_fNowOrSaleStopImageView;
	/** 加奖图标 */
	private ImageView	_fRewardImageView;
	/** 彩种名称 */
	private TextView	_fLotteryNameTextView;
	/** 彩种对象 **/
	private Lottery		_fLottery;

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 */
	public BuyLotteryHallSlidePanelItem(Context aContext) {
		super(aContext);
		_fContext = aContext;

		LayoutInflater inflater = (LayoutInflater) aContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.buylotteryhall_slidepanel_litem, this);

		// 获取布局中的各个控件
		_fLotteryIcoImageView = (ImageView) findViewById(R.id.buylotteryhall_lotteryrevalitem_lotteryico);
		_fNowOrSaleStopImageView = (ImageView) findViewById(R.id.buylotteryhall_lotteryrevalitem_noworsalestop);
		_fRewardImageView = (ImageView) findViewById(R.id.buylotteryhall_lotteryrevalitem_reward);
		_fLotteryNameTextView = (TextView) findViewById(R.id.buylotteryhall_lotteryrevalitem_lotteryname);

	}

	public BuyLotteryHallSlidePanelItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 使用选项名称初始化选项显示
	 */
	public void initBuyLotteryHallPanelItemShowWithItemName(String aItemName) {
		initItemIconShow(aItemName);
		initItemNameShow(aItemName);
	}

	/**
	 * 初始化选项的名称显示
	 */
	private void initItemNameShow(String aItemName) {
		_fLotteryNameTextView.setText(aItemName);
	}

	/**
	 * 初始化选项的图标显示
	 */
	private void initItemIconShow(String aItemName) {
		// FIXME 是否可以将合买大厅和专家推荐优化为常量
		if (aItemName.equals("合买大厅")) {
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_collaboratebuyhall);
		} else if (aItemName.equals("专家荐号")) {
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_expertrecommand);
		}
	}

	/**
	 * 使用彩种类型初始化选项的显示
	 * 
	 * @param aLottrey
	 *            彩种对象
	 */
	public void initBuyLotteryHallPanelItemShowWithLottery(Lottery aLottrey) {
		_fLottery = aLottrey;

		initLotteryIcoShow(aLottrey);
		initLotteryNowOrSaleStopShow(aLottrey);
		initRewardShow(aLottrey);
		initLotteryNameShow(aLottrey);
		setPanelItemOnClickEvent(aLottrey);
	}

	/**
	 * 初始化彩种名称的显示
	 * 
	 * @param aLottrey
	 *            彩种对象
	 */
	private void initLotteryNameShow(Lottery aLottrey) {
		_fLotteryNameTextView.setText(aLottrey.get_fLotteryType()
				.get_fLotteryName());
	}

	/**
	 * 初始化加奖图片的显示
	 * 
	 * @param aLottrey
	 *            彩种对象
	 */
	private void initRewardShow(Lottery aLottrey) {
		if (aLottrey.get_fIsReward()) {
			_fRewardImageView.setVisibility(View.VISIBLE);
		} else {
			_fRewardImageView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 初始化今日开奖或者暂停销售的显示
	 * 
	 * @param aLottrey
	 *            彩种对象
	 */
	private void initLotteryNowOrSaleStopShow(Lottery aLottrey) {
		// 设置今日开奖图标
		if (aLottrey.get_fIsNowLottery()) {
			_fNowOrSaleStopImageView.setVisibility(View.VISIBLE);
			_fNowOrSaleStopImageView
					.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.buylotteryhall_lotteryrevalitem_nowlottery));
		}
		// 设置是否停售图标
		else if (aLottrey.get_fIsSaleStop()) {
			_fNowOrSaleStopImageView.setVisibility(View.VISIBLE);
			_fNowOrSaleStopImageView
					.setBackgroundDrawable(getResources()
							.getDrawable(
									R.drawable.buylotteryhall_lotteryrevalitem_salestop));
		}
	}

	/**
	 * 初始化彩种图标的显示
	 * 
	 * @param aLottrey
	 *            彩种对象
	 */
	private void initLotteryIcoShow(Lottery aLottrey) {
		LotteryType lotteryType = aLottrey.get_fLotteryType();

		switch (lotteryType) {
		case DOUBLE_BALL:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_doubleball);
			break;
		case SUPER_LOTTO:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_superlotto);
			break;
		case WELFARE_3D:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_welfare3d);
			break;
		case JIANGXI_ELEVEN_SELECT_FIVE:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_jiangxielevenselectfive);
			break;
		case CONSTANTLY:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_constantly);
			break;
		case COMPETE_FOOTBALL:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_bettingfootball);
			break;
		case GUANGDONG_HAPPYVERY:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_guangdonghappyevery);
			break;
		case ELEVEN_LUCKGOLD:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_elevenlucklygold);
			break;
		case GUANGDONG_ELEVENE_SELECT_FIVE:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_guangdongelevenselectfive);
			break;
		case ARRANGE_THREE:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_arrangethree);
			break;
		case SERVEN_HAPPY:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_sevenhappy);
			break;
		case TWENTYTWO_SELECT_FIVE:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_twentytowselectfive);
			break;
		case ARRANGE_FIVE:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_arrangefive);
			break;
		case SEVEN_STAR:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_sevenstart);
			break;
		case FOOTBALL:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_football);
			break;
		case COMPETE_BASKETBALL:
			_fLotteryIcoImageView
					.setBackgroundResource(R.drawable.buylotteryhall_pannelitem_bettingbasketball);
			break;
		}
	}

	/**
	 * 设置面板选项的点击事件
	 */
	public void setPanelItemOnClickEvent(Lottery aLottrey) {
		_fLotteryIcoImageView
				.setOnClickListener(new PanelItemOnClickListenter());
	}

	class PanelItemOnClickListenter implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			LotteryType lotteryType = _fLottery.get_fLotteryType();

			switch (lotteryType) {
			case DOUBLE_BALL:
				intent = new Intent(_fContext, DoubleBallActivityGroup.class);
				break;
			case SUPER_LOTTO:
				intent = new Intent(_fContext, SuperLottoActivityGroup.class);
				break;
			case WELFARE_3D:
				intent = new Intent(_fContext, Welfare3DActivityGroup.class);
				break;
			case JIANGXI_ELEVEN_SELECT_FIVE:
				intent = new Intent(_fContext,
						JiangXiElevenSelectFiveActivityGroup.class);
				break;
			case CONSTANTLY:
				intent = new Intent(_fContext, ConstantlyActivityGroup.class);
				break;
			case COMPETE_FOOTBALL:
				intent = new Intent(_fContext, CompeteFootballActivity.class);
				break;
			case GUANGDONG_HAPPYVERY:
				intent = new Intent(_fContext,
						GuangDongHappyVeryActivityGroup.class);
				break;
			case ELEVEN_LUCKGOLD:
				intent = new Intent(_fContext,
						ElevenLuckGoldActivityGroup.class);
				break;
			case GUANGDONG_ELEVENE_SELECT_FIVE:
				intent = new Intent(_fContext,
						GuangDongElevenSelectFiveActivityGroup.class);
				break;
			case ARRANGE_THREE:
				intent = new Intent(_fContext, ArrangeThreeActivityGroup.class);
				break;
			case SERVEN_HAPPY:
				intent = new Intent(_fContext, ServenHappyActivityGroup.class);
				break;
			case TWENTYTWO_SELECT_FIVE:
				intent = new Intent(_fContext,
						TwentyTwoSelectFiveActivityGroup.class);
				break;
			case ARRANGE_FIVE:
				intent = new Intent(_fContext, ArrangeFiveActivityGroup.class);
				break;
			case SEVEN_STAR:
				intent = new Intent(_fContext, ServenStarActivityGroup.class);
				break;
			case FOOTBALL:
				intent = new Intent(_fContext, FootBallActivity.class);
				break;
			case COMPETE_BASKETBALL:
				intent = new Intent(_fContext, CompeteBasketBallActivity.class);
				break;
			}

			_fContext.startActivity(intent);
		}

	}
}