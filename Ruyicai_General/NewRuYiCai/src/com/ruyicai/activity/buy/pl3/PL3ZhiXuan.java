package com.ruyicai.activity.buy.pl3;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.code.pl3.PL3ZiHeZhiCode;
import com.ruyicai.code.pl3.PL3ZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class PL3ZhiXuan extends ZixuanActivity implements
		OnCheckedChangeListener {
	private int iCurrentButton;
	private LinearLayout topLinear;
	private LinearLayout topLinearTwo;
	private RadioGroup topButton;
	private String topTitle[] = { "普通直选", "直选和值" };

	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfosZH[] = new AreaInfo[1];// 直选和值 1个选区
	private AreaInfo areaInfosZ[] = new AreaInfo[3];// 直选3个选区
	private PL3ZiHeZhiCode pl3CodeH = new PL3ZiHeZhiCode();
	private PL3ZiZhiXuanCode pl3CodeZ = new PL3ZiZhiXuanCode();
	// 福彩3D直选的三个BallTable
	BallTable baiBallTable;
	BallTable shiBallTable;
	BallTable geBallTable;

	BallTable ballTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setAddView(((PL3)getParent()).addView);
		super.onCreate(savedInstanceState);
		MAX_ZHU = 600;
		ALL_ZHU = 99;
		topLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top);
		topLinearTwo = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_two);
		topLinear.setVisibility(LinearLayout.VISIBLE);
		topLinearTwo.setVisibility(LinearLayout.VISIBLE);
		topButton = (RadioGroup) findViewById(R.id.buy_zixuan_radiogroup_top);
		initTopButton();
		topButton.setOnCheckedChangeListener(this);
		topButton.check(0);
	}

	public void initTopButton() {
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
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String mTextSumMoney = "";
		int iZhuShu = getZhuShu();
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_HEZHI_ZHIXUAN:
			if (iZhuShu == 0) {
				mTextSumMoney = "需要1个球";
			} else {
				mTextSumMoney = "共" + (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
			}
			break;
		case PublicConst.BUY_PL3_ZHIXUAN:
			if (baiBallTable.getHighlightBallNums() > 0
					&& shiBallTable.getHighlightBallNums() > 0
					&& geBallTable.getHighlightBallNums() > 0) {
				mTextSumMoney = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			} else if (baiBallTable.getHighlightBallNums() == 0) {
				mTextSumMoney = "至少还需要1个百位小球";
			} else if (shiBallTable.getHighlightBallNums() == 0) {
				mTextSumMoney = "至少还需要1个十位小球";
			} else if (geBallTable.getHighlightBallNums() == 0) {
				mTextSumMoney = "至少还需要1个个位小球";
			}
			break;
		}
		return mTextSumMoney;
	}

	@Override
	public String isTouzhu() {
		// TODO Auto-generated method stub
		String isTouzhu = "";
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_HEZHI_ZHIXUAN:
			if (ballTable.getHighlightBallNums() < 1) {
				isTouzhu = "请选择小球号码后再投注";
			} else if (ballTable.getHighlightBallNums() == 1) {
				// wangyl 7.13 配合陈晨投注时用
				int iZhuShu = getZhuShu();
				String fushiStr = PublicMethod.getZhuMa(ballTable
						.getHighlightBallNOs()[0]) + "";
				if (iZhuShu * 2 > 100000) {
					// dialogExcessive();
					isTouzhu = "false";
				} else {
					// setZhuShu(iZhuShu);
					// alert( "注码：" + fushiStr);
					isTouzhu = "true";
				}
			}
			break;

		case PublicConst.BUY_PL3_ZHIXUAN:
			int baiweiNums = baiBallTable.getHighlightBallNums();
			int shiweiNums = shiBallTable.getHighlightBallNums();
			int geweiNums = geBallTable.getHighlightBallNums();
			int[] baiweis = baiBallTable.getHighlightBallNOs();
			int[] shiweis = shiBallTable.getHighlightBallNOs();
			int[] geweis = geBallTable.getHighlightBallNOs();
			String baiweistr = "";
			String shiweistr = "";
			String geweistr = "";
			for (int i = 0; i < baiweiNums; i++) {
				baiweistr += (baiweis[i]) + ",";
				if (i == baiweiNums - 1) {
					baiweistr = baiweistr.substring(0, baiweistr.length() - 1);
				}
			}
			for (int i = 0; i < shiweiNums; i++) {
				shiweistr += (shiweis[i]) + ",";
				if (i == shiweiNums - 1) {
					shiweistr = shiweistr.substring(0, shiweistr.length() - 1);
				}
			}
			for (int i = 0; i < geweiNums; i++) {
				geweistr += (geweis[i]) + ",";
				if (i == geweiNums - 1) {
					geweistr = geweistr.substring(0, geweistr.length() - 1);
				}
			}
			if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
				isTouzhu = "请在百位，十位，个位均至少选择一个小球后再投注";
			} else {
				int iZhuShu = getZhuShu();
				isTouzhu = "true";
			}
			break;
		}
		return isTouzhu;
	}

	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_PL3);
	}

	/**
	 * 重写RadioGroup监听方法onCheckedChanged
	 * 
	 * @param RadioGroup
	 *            RadioGroup
	 * @param int checkedId 当前被选择的RadioId
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (group.getId()) {
		case R.id.buy_zixuan_radiogroup_top:
			switch (checkedId) {
			case 0:// 排列三直选
				iCurrentButton = PublicConst.BUY_PL3_ZHIXUAN;
				create_PL3_ZHIXUAN();
				break;
			case 1:// 排列三直选和值
				iCurrentButton = PublicConst.BUY_PL3_HEZHI_ZHIXUAN;
				create_PL3_HEZHI_ZHIXUAN();
				break;
			}
		}
	}

	/**
	 * 获取注数
	 * 
	 */
	public int getZhuShu() {
		int zhushu = 0;
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_HEZHI_ZHIXUAN:
			zhushu = getPL3ZhixuanHezhiZhushu();
			break;
		case PublicConst.BUY_PL3_ZHIXUAN:
			zhushu = getPL3ZhixuanZhushu();
		}
		return zhushu * iProgressBeishu;
	}

	/**
	 * 获得福彩3D直选注数
	 * 
	 * @return
	 */
	private int getPL3ZhixuanZhushu() {
		int zhushu = 0;
		zhushu = baiBallTable.getHighlightBallNums()
				* shiBallTable.getHighlightBallNums()
				* geBallTable.getHighlightBallNums();
		return zhushu;
	}

	/**
	 * 获得福彩3D直选和值注数
	 * 
	 * @return 返回注数
	 */
	private int getPL3ZhixuanHezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = ballTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得1）
		int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69,
				73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 };// 0~27

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i]) {// 因为数组是从0开始的，小球号码从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;

				}
			}
		}
		return iZhuShu;
	}

	private void create_PL3_ZHIXUAN() {
		initZXArea();
		setCode(pl3CodeZ);
		setIsTen(false);
		initViewItem();
		baiBallTable = itemViewArray.get(0).areaNums[0].table;
		shiBallTable = itemViewArray.get(0).areaNums[1].table;
		geBallTable = itemViewArray.get(0).areaNums[2].table;

	}

	private void initZXArea() {

	}

	/**
	 * 福彩3D和值直选
	 */
	private void create_PL3_HEZHI_ZHIXUAN() {
		pl3CodeH.setiCurrentButton(iCurrentButton);
		setCode(pl3CodeH);
		setIsTen(true);
		initViewItem();
		ballTable = itemViewArray.get(0).areaNums[0].table;
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
		case PublicConst.BUY_PL3_HEZHI_ZHIXUAN:
			areaNums = new AreaNum[1];
			String title = getResources().getString(
					R.string.fc3d_text_hezhi_title).toString();
			areaNums[0] = new AreaNum(28, 8, 1, 1, ballResId, 0, 0, Color.RED,
					title, false, true);
			return areaNums;
		case PublicConst.BUY_PL3_ZHIXUAN:
			areaNums = new AreaNum[3];
			String baiTitle = getResources().getString(
					R.string.fc3d_text_bai_title).toString();
			String shiTitle = getResources().getString(
					R.string.fc3d_text_shi_title).toString();
			String geTitle = getResources().getString(
					R.string.fc3d_text_ge_title).toString();
			areaNums[0] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0,
					Color.RED, baiTitle, false, true);
			areaNums[1] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0,
					Color.RED, shiTitle, false, true);
			areaNums[2] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0,
					Color.RED, geTitle, false, true);
			return areaNums;
		}
		return areaNums;
	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId) {
		AreaNum[] areaNums = itemViewArray.get(0).areaNums;
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_HEZHI_ZHIXUAN:
			areaNums[0].table.clearAllHighlights();
			areaNums[0].table.changeBallState(areaNums[0].chosenBallSum,
					iBallId);
			break;
		case PublicConst.BUY_PL3_ZHIXUAN:
			int nBallId = 0;
			if (iBallId >= 0 && iBallId < 10) {
				nBallId = iBallId;
				areaNums[0].table.changeBallState(areaNums[0].chosenBallSum,
						nBallId);
			} else if (iBallId < 20 && iBallId >= 10) {
				nBallId = iBallId - 10;
				areaNums[1].table.changeBallState(areaNums[1].chosenBallSum,
						nBallId);
			} else {
				nBallId = iBallId - 20;
				areaNums[2].table.changeBallState(areaNums[2].chosenBallSum,
						nBallId);
			}
			break;
		}

	}

	@Override
	public String getZhuma() {
		// TODO Auto-generated method stub
		return null;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_PL3);
		codeInfo.setTouZhuType("zhixuan");
	}
}