package com.ruyicai.activity.buy.twentytwo;

import android.graphics.Color;
import android.os.Bundle;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.twenty.TwentyZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class TwentyZiXuan extends ZixuanActivity {

	private int redBallResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[1];// 1个选区
	private TwentyZhiXuanCode Code = new TwentyZhiXuanCode();
	BallTable redBallTable;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCode(Code);
		setIsTen(true);
		initViewItem();
		redBallTable = itemViewArray.get(0).areaNums[0].table;
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		BuyViewItem buyView = new BuyViewItem(this, initArea());
		itemViewArray.add(buyView);
		layoutView.addView(buyView.createView());
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[1];
		String redTitle = getResources().getString(
				R.string.please_choose_number).toString();
		areaNums[0] = new AreaNum(22, 8, 5, 18, redBallResId, 0, 1, Color.RED,
				redTitle, false, true);
		return areaNums;
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * 判断是否满足投注条件
	 */
	public String isTouzhu() {
		// TODO Auto-generated method stub
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		if (redBallTable.getHighlightBallNums() < 5) {
			isTouzhu = "请至少选择5个小球	";
		} else if (iZhuShu * 2 > 100000) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 投注提示框中的信息(注码)
	 * 
	 * @return
	 */
	public String getZhuma() {
		String red_zhuma_string = " ";
		int[] redZhuMa = redBallTable.getHighlightBallNOs();
		for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string = red_zhuma_string
					+ PublicMethod.isTen(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1) {
				red_zhuma_string = red_zhuma_string + ",";
			}
		}

		return "注码：" + red_zhuma_string;

	}

	/**
	 * 计算注数
	 */
	public int getZhuShu() {
		int iZhuShu = 0;
		int iRedHighlights = itemViewArray.get(0).areaNums[0].table
				.getHighlightBallNums();
		iZhuShu = (int) getTwentyZXZhuShu(iRedHighlights, iProgressBeishu);
		return iZhuShu;
	}

	/**
	 * 投注联网
	 */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_22_5);

	}

	/**
	 * 选择小球提示金额
	 */
	public String textSumMoney(AreaNum areaNum[], int iProgressBeishu) {
		// TODO Auto-generated method stub
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		String iTempString = "";
		int iZhuShu = 0;
		// 红球数 不足
		if (iRedHighlights < 5) {
			int num = 5 - iRedHighlights;
			return "至少还需" + num + "个小球";
		} else {
			iZhuShu = (int) getTwentyZXZhuShu(iRedHighlights, iProgressBeishu);
			iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		}
		// 红球数达到最低要求

		return iTempString;
	}

	/**
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * 
	 * @return long 注数
	 */
	private long getTwentyZXZhuShu(int aRedBalls, int iProgressBeishu) {
		long ssqZhuShu = 0L;
		if (aRedBalls > 0) {
			ssqZhuShu += (PublicMethod.zuhe(5, aRedBalls) * iProgressBeishu);
		}
		return ssqZhuShu;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_22_5);
		codeInfo.setTouZhuType("zhixuan");
	}

}
