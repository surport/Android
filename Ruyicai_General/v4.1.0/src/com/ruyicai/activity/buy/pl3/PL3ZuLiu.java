package com.ruyicai.activity.buy.pl3;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.code.pl3.PL3ZiHeZhiCode;
import com.ruyicai.code.pl3.PL3ZiZuXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class PL3ZuLiu extends ZixuanActivity implements OnCheckedChangeListener {
	private int iCurrentButton;
	private LinearLayout topLinear;
	private LinearLayout topLinearTwo;
	private RadioGroup topButton;
	private String topTitle[] = { "组六", "组六和值" };
	private AreaInfo areaInfos[];// 选区
	private PL3ZiZuXuanCode pl3Code = new PL3ZiZuXuanCode();
	private PL3ZiHeZhiCode pl3CodeHZ = new PL3ZiHeZhiCode();
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	BallTable oneBallTable;

	/**
	 * 初始化组件
	 */
	public void init() {
		topLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top);
		topLinearTwo = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_two);
		topLinear.setVisibility(LinearLayout.VISIBLE);
		topLinearTwo.setVisibility(LinearLayout.VISIBLE);
		initRadioGroup();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setAddView(((PL3) getParent()).addView);
		super.onCreate(savedInstanceState);
		MAX_ZHU = 600;
		ALL_ZHU = 99;
		init();
	}

	/**
	 * 初始化单选按钮组
	 */
	public void initRadioGroup() {
		topButton = (RadioGroup) findViewById(R.id.buy_zixuan_radiogroup_top);
		for (int i = 0; i < topTitle.length; i++) {
			RadioButton radio = new RadioButton(this);
			radio.setText(topTitle[i]);
			radio.setTextColor(Color.BLACK);
			radio.setTextSize(13);
			radio.setId(i);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 10, 0);
			topButton.addView(radio);

		}
		topButton.setOnCheckedChangeListener(this);
		topButton.check(0);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (group.getId()) {
		case R.id.buy_zixuan_radiogroup_top:
			switch (checkedId) {
			case 0:// 组六直选
				iCurrentButton = PublicConst.BUY_PL3_ZU6;
				create_ZU6();
				break;
			case 1:// 组六和值
				iCurrentButton = PublicConst.BUY_PL3_HEZHI_ZU6;
				create_PL3_HEZHI_ZU6();
				break;
			}
		}

	}

	/**
	 * 福彩3D和值组6
	 */
	private void create_PL3_HEZHI_ZU6() {
		pl3CodeHZ.setiCurrentButton(iCurrentButton);
		setCode(pl3CodeHZ);
		setIsTen(true);
		initViewItem();
		oneBallTable = itemViewArray.get(0).areaNums[0].table;
	}

	/**
	 * 组六
	 */
	private void create_ZU6() {
		pl3Code.setiCurrentButton(iCurrentButton);
		iCurrentButton = PublicConst.BUY_PL3_ZU6;
		setCode(pl3Code);
		setIsTen(false);
		initViewItem();
		oneBallTable = itemViewArray.get(0).areaNums[0].table;
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		// TODO Auto-generated method stub
		itemViewArray.clear();
		layoutView.removeAllViews();
		BuyViewItem buyView = new BuyViewItem(this, initArea());
		itemViewArray.add(buyView);
		layoutView.addView(buyView.createView());
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		AreaNum areaNums[] = null;
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_HEZHI_ZU6:
			areaNums = new AreaNum[1];
			String title = getResources().getString(
					R.string.fc3d_text_hezhi_title).toString();
			areaNums[0] = new AreaNum(22, 8, 1, 1, ballResId, 0, 3, Color.RED,
					title, false, true);
			return areaNums;
		case PublicConst.BUY_PL3_ZU6:
			areaNums = new AreaNum[1];
			String title2 = getResources().getString(
					R.string.fc3d_text_hezhi_title).toString();
			areaNums[0] = new AreaNum(10, 10, 3, 9, ballResId, 0, 0, Color.RED,
					title2, false, true);
			return areaNums;
		}
		return areaNums;
	}

	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String mTextSumMoney = "";
		int iZhuShu = getZhuShu();
		switch (iCurrentButton) {

		case PublicConst.BUY_PL3_HEZHI_ZU6:

			if (iZhuShu == 0) {
				mTextSumMoney = "需要1个球";
			} else {
				mTextSumMoney = "共" + (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
			}
			break;
		case PublicConst.BUY_PL3_ZU6: // 福彩3D组6
			if (oneBallTable.getHighlightBallNums() > 2) {
				mTextSumMoney = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			} else {
				int num = 3 - oneBallTable.getHighlightBallNums();
				mTextSumMoney = "还需要" + num + "个球";
			}
			break;
		}
		return mTextSumMoney;
	}

	/**
	 * 
	 * @return
	 */
	public int getZhuShu() {
		int iReturnValue = 0;
		// 福彩3D组3
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZU6:
			int iZu6Balls = oneBallTable.getHighlightBallNums();
			iReturnValue = (int) getFc3dZu6FushiZhushu(iZu6Balls);
			break;
		case PublicConst.BUY_PL3_HEZHI_ZU6:
			iReturnValue = getFc3dZu6HezhiZhushu();
			break;
		}
		return iReturnValue * iProgressBeishu;

	}

	/**
	 * 获得福彩3D组6和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZu6HezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = oneBallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9,
				8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 3) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值组6，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得福彩3D组6复式注数
	 * 
	 * @param iZu6balls
	 *            选择小球个数
	 * @return 返回注数
	 */
	private long getFc3dZu6FushiZhushu(int iZu6balls) {
		long tempzhushu = 0l;
		if (iZu6balls > 0) {
			tempzhushu += PublicMethod.zuhe(3, iZu6balls);
		}
		return tempzhushu;

	}

	public String isTouzhu() {
		// TODO Auto-generated method stub
		String isTouzhu = "";
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZU6:
			if (oneBallTable.getHighlightBallNums() < 3) {
				isTouzhu = "请至少选择3个小球后再投注";
			} else {
				int[] fushiNums = oneBallTable.getHighlightBallNOs();
				String fushiStr = "";
				for (int i = 0; i < fushiNums.length; i++) {
					fushiStr += (fushiNums[i]) + ",";
					if (i == fushiNums.length - 1) {
						fushiStr = fushiStr.substring(0, fushiStr.length() - 1);
					}
				}
				int iZhuShu = getZhuShu();

				if (iZhuShu * 2 > 100000) {
					isTouzhu = "false";
				} else {
					isTouzhu = "true";
				}
			}
			break;

		case PublicConst.BUY_PL3_HEZHI_ZU6:
			if (oneBallTable.getHighlightBallNums() < 1) {
				isTouzhu = "请选择小球号码后再投注";
			} else if (oneBallTable.getHighlightBallNums() == 1) {
				// wangyl 7.13 配合陈晨投注时用
				int iZhuShu = getZhuShu();
				String fushiStr = PublicMethod.getZhuMa(oneBallTable
						.getHighlightBallNOs()[0]) + "";
				if (iZhuShu * 2 > 100000) {
					isTouzhu = "false";
				} else {
					isTouzhu = "true";
				}
			}
			break;
		}
		return isTouzhu;
	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId) {
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZU6:
			itemViewArray.get(0).areaNums[0].table.changeBallState(
					itemViewArray.get(0).areaNums[0].chosenBallSum, iBallId);
			break;

		case PublicConst.BUY_PL3_HEZHI_ZU6:
			itemViewArray.get(0).areaNums[0].table.clearAllHighlights();
			itemViewArray.get(0).areaNums[0].table.changeBallState(
					itemViewArray.get(0).areaNums[0].chosenBallSum, iBallId);
			break;
		}

	}

	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_PL3);
	}

	@Override
	public String getZhuma() {
		// TODO Auto-generated method stub
		return null;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_PL3);
		codeInfo.setTouZhuType("zu6");
	}
}