package com.ruyicai.activity.buy.nmk3;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zc.FootballChooseNineLottery;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * Nmk3DiffActivity:内蒙古快三三不同号
 * 
 * @author PengCX
 * 
 */
public class Nmk3DiffActivity extends ZixuanAndJiXuan {
	int num;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		childtype = new String[] { "三不同号", "二不同号" };
		BallResId[0] = R.drawable.nmk3_normal;
		BallResId[1] = R.drawable.nmk3_click;
		setContentView(R.layout.sscbuyview);
		init();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		radioId = checkedId;
		onCheckAction(checkedId);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensor.stopAction();
		baseSensor.stopAction();
		editZhuma.setText(R.string.please_choose_number);
	}


	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int zhuShu = getZhuShu();

		if (highttype.equals("NMK3-DIFFER-THREE")) {
			if (num < 3) {
				return "还需要选择" + (3 - num) + "个球";
			}
		} else {
			if (num < 2) {
				return "还需要选择" + (2 - num) + "个球";
			}
		}

		return "共" + zhuShu + "注，共" + zhuShu * 2 + "元";
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
		num = areaNums[0].table.getHighlightBallNums();
		int zhuShu = 0;

		if (highttype.equals("NMK3-DIFFER-THREE")) {
			if (num >= 3) {
				zhuShu = zuHe(num, 3);
			}
		} else {
			if (num >= 2) {
				zhuShu = zuHe(num, 2);
			}
		}

		return zhuShu;
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

		if (radioId == 0 && getZhuShu() == 1) {
			zhuMa = playMethodPart + mutiplePart + numbersPart + endFlagPart;
		} else {
			zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
					+ endFlagPart;
		}

		return zhuMa;
	}

	private String getNumbersPart() {
		StringBuffer numbersPart = new StringBuffer();
		int[] areaNumbers = areaNums[0].table.getHighlightBallNOs();

		if (radioId == 0) {
			for (int number_i = 0; number_i < areaNumbers.length; number_i++) {
				String numberString = PublicMethod
						.getZhuMa(areaNumbers[number_i]);
				numbersPart.append(numberString);
			}
		} else {
			for (int number_i = 0; number_i < areaNumbers.length; number_i++) {
				String numberString = "";
				if (getZhuShu() > 1) {
					numberString = PublicMethod.getZhuMa(areaNumbers[number_i]);
				} else {
					numberString = String.valueOf(areaNumbers[number_i]);
				}

				numbersPart.append(numberString);
			}
		}

		return numbersPart.toString();
	}

	private String getNumberNumsPart() {
		if (radioId == 1 && getZhuShu() == 1) {
			return "01";
		} else {
			return PublicMethod.getZhuMa(areaNums[0].table
					.getHighlightBallNOs().length);
		}

	}

	private String getMutiplePart() {
		return "0001";
	}

	private String getPlayMethodPart() {
		String playMethod = "";
		if (radioId == 0) {
			if (getZhuShu() > 1) {
				playMethod = "63";
			} else {
				playMethod = "00";
			}
		} else {
			if (getZhuShu() > 1) {
				playMethod = "21";
			}else {
				playMethod = "20";
			}
			
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
		initArea(checkedId);

		switch (checkedId) {
		case 0:
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_DIFF_THREE,
					true, checkedId, false);
			break;
		case 1:
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_DIFF_TWO, true,
					checkedId, false);
			break;
		}
	}

	public AreaNum[] initArea(int checkedId) {
		areaNums = new AreaNum[1];

		switch (checkedId) {
		case 0:
			highttype = "NMK3-DIFFER-THREE";
			areaNums[0] = new AreaNum(6, 4, 1, 6, BallResId, 0, 1, Color.RED,
					"", false, true);
			break;
		case 1:
			highttype = "NMK3-DIFFER-TWO";
			areaNums[0] = new AreaNum(6, 4, 1, 6, BallResId, 0, 1, Color.RED,
					"", false, true);
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
		if (radioId == 0) {
			codeInfo.setTouZhuType("different_three");
		} else if (radioId == 1) {
			codeInfo.setTouZhuType("different_two");
		}
	}

}
