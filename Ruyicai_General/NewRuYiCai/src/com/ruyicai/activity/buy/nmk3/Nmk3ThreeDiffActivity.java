package com.ruyicai.activity.buy.nmk3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.Nmk3MissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * Nmk3DiffActivity:内蒙古快三三不同号
 * 
 * @author PengCX
 * 
 */
public class Nmk3ThreeDiffActivity extends ZixuanAndJiXuan {
	//选择的三不同单选号码小球的个数
	int threeDiffBallNums;
	//选择的三连号通选的小球的个数
	int threeLinkBallNums;
	
	int threeDiffZhuShu;
	int threeLinkZhuShu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Nmk3Activity) getParent()).addView);
		super.onCreate(savedInstanceState);
		//设置彩种信息
		lotno = Constants.LOTNO_NMK3;
		highttype = "NMK3-DIFFER-THREE";
		lotnoStr=Constants.LOTNO_NMK3;
		BallResId[0] = R.drawable.changbtn_normal;
		BallResId[1] = R.drawable.changbtn_click;
		//设置单选按钮
		childtype = new String[] { "直选" };
		init();
		childtypes.setVisibility(View.GONE);
		//设置背景图片
		zixuanLayout.setBackgroundResource(R.color.transparent);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		//页面启动之后，由于RadioGroup自动调用监听方法，进行页面的初始化显示
		 onCheckAction(checkedId);
		 ((BuyActivityGroup) getParent()).showBetInfo(textSumMoney(areaNums, iProgressBeishu));
	}

	@Override
	protected void onResume() {
		super.onResume();
//		sensor.stopAction();
//		baseSensor.stopAction();
		editZhuma.setText(textSumMoney(areaNums, iProgressBeishu));
		editZhuma.setTextColor(Color.BLACK);
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int zhuShu = getZhuShu();
//		if (threeDiffBallNums < 3) {
//			return "还需要选择" + (3 - threeDiffBallNums) + "个球";
//		}
		return "您已选择了" + zhuShu + "注，共" + zhuShu * 2 + "元";
	}
	/**
	 * 设置投注金额提示
	 */
	public void showEditText(){
		editZhuma.setText(textSumMoney(areaNums, iProgressBeishu));
		showEditTitle(NULL);
	}
	@Override
	public String isTouzhu() {
		if (getZhuShu() == 0) {
			return "请至少选择一注";
		} else if (getZhuShu() > 10000) {
			return "false";
		} else {
			return "true";
		}
	}

	@Override
	public int getZhuShu() {
		// 获取三不同号单选择的小球个数
		threeDiffBallNums = areaNums[0].table.getHighlightBallNums();
		// 获取三连号通选选择小球的个数
		threeLinkBallNums = areaNums[1].table.getHighlightBallNums();
		
		threeDiffZhuShu = 0;
		// 计算三不同号的注数
		if (threeDiffBallNums >= 3) {
			threeDiffZhuShu = zuHe(threeDiffBallNums, 3);
		}

		threeLinkZhuShu = 0;
		// 计算三连号通选的注数
		if (threeLinkBallNums > 0) {
			threeLinkZhuShu = 1;
		}

		// 返回注数总和
		return threeDiffZhuShu + threeLinkZhuShu;
	}
	
	int getThreeLinkZhuShu() {
		return threeLinkZhuShu;
	}

	int getThreeDiffZhuShu() {
		return threeDiffZhuShu;
	}

	@Override
	public String getZhuma() {
		// 拼接投注的注码格式，用户投注与后台使用
		String zhuMa = "";

		// 获取注码的各个部分
		String playMethodPart = getPlayMethodPart();
		String mutiplePart = getMutiplePart();
		String numberNumsPart = getNumberNumsPart();
		String numbersPart = getNumbersPart();
		String endFlagPart = "^";

		if (radioId == 0 && getThreeDiffZhuShu() == 1) {
			zhuMa = playMethodPart + mutiplePart + numbersPart + endFlagPart;
		} else {
			zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
					+ endFlagPart;
		}

		return zhuMa;
	}
	
	String getZhuma2() {
		// 拼接投注的注码格式，用户投注与后台使用
		String zhuMa = "";

		// 获取注码的各个部分
		String playMethodPart = getPlayMethodPart2();
		String mutiplePart = getMutiplePart2();
		String numberNumsPart = getNumberNumsPart2();
		String numbersPart = getNumbersPart2();
		String endFlagPart = "^";

		// 拼接注码
		zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
				+ endFlagPart;

		return zhuMa;
	}

	private String getNumbersPart2() {
		return "";
	}

	private String getNumberNumsPart2() {
		return "";
	}

	private String getMutiplePart2() {
		return "0001";
	}

	private String getPlayMethodPart2() {
		return "50";
	}

	private String getNumbersPart() {
		StringBuffer numbersPart = new StringBuffer();
		int[] areaNumbers = areaNums[0].table.getHighlightBallNOs();

		for (int number_i = 0; number_i < areaNumbers.length; number_i++) {
			String numberString = PublicMethod.getZhuMa(areaNumbers[number_i]);
			numbersPart.append(numberString);
		}

		return numbersPart.toString();
	}

	private String getNumberNumsPart() {
		return PublicMethod
				.getZhuMa(areaNums[0].table.getHighlightBallNOs().length);
	}

	private String getMutiplePart() {
		return "0001";
	}

	private String getPlayMethodPart() {
		String playMethod = "";
		if (getThreeDiffZhuShu() > 1) {
			playMethod = "63";
		} else {
			playMethod = "00";
		}

		return playMethod;
	}

	@Override
	public String getZhuma(Balls ball) {
		return null;
	}

	@Override
	public void touzhuNet() {
		// 设置投注信息彩种，注码，金额和期号等投注信息
		betAndGift.setLotno(Constants.LOTNO_NMK3);
		betAndGift.setBet_code(getZhuma());
		int zhuShu = getZhuShu();
		betAndGift.setAmount("" + zhuShu * 200);
		betAndGift.setBatchcode(Nmk3Activity.batchCode);
	}

	@Override
	public void onCheckAction(int checkedId) {
		//根据单选按钮的id初始化页面
		switch (checkedId) {
		case 0:
			// 创建页面内的选号面板对象
			initArea(checkedId);
			// 根据创建页面的选号面板对象，创建页面的视图
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_DIFF_THREE,
					true, checkedId, true);
			// 获取遗漏值
			isMissNet(new Nmk3MissJson(), MissConstant.NMK3_THREE_TWO + ";" + MissConstant.NMK3_THREE_LINK_TONG, false);
			break;
		}
	}

	public AreaNum[] initArea(int checkedId) {
		//创建页面额选号面板
		areaNums = new AreaNum[2];
		switch (checkedId) {
		case 0:
			areaNums[0] = new AreaNum(6, 4, 1, 6, BallResId, 0, 1, Color.RED,
					"三不同号单选：猜开奖的3个号码，奖金40元！", false, true);
			areaNums[1] = new AreaNum(1, 1, 1, 1, BallResId, 0, 1, Color.RED, "三连号通选：123,234,345,456任一开出即中10元！",
					false, true);
			break;
		}

		return areaNums;
	}

	/**
	 * 求a取b的组合数
	 */
	private int zuHe(int a, int b) {
		int up = 1;
		for (int up_i = 0; up_i < b; up_i++) {
			up = up * a;
			a--;
		}

		int down = jieCheng(b);

		return up / down;
	}

	/**
	 * 求b的阶乘
	 */
	private int jieCheng(int b) {
		int result = 0;

		if (b == 1 || b == 0) {
			result = b;
		} else {
			result = b * jieCheng(b - 1);
		}

		return result;
	}

	/*
	 * 设置投注信息类的彩种编号和投注类型
	 */
	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		codeInfo.setTouZhuType("different_three");
	}
	
	void setLotoNoAndType2(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		codeInfo.setTouZhuType("threelink");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeMediaPlayer();
		NmkAnimation.flag = true;
	}
}
