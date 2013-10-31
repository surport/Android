package com.ruyicai.activity.buy.nmk3;

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

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Nmk3ThreeSameActivity:内蒙古快三三同号
 * 
 * @author PengCX
 * 
 */
public class Nmk3ThreeSameActivty extends ZixuanAndJiXuan {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Nmk3Activity) getParent()).addView);
		super.onCreate(savedInstanceState);
		lotno = Constants.LOTNO_NMK3;
		childtype = new String[] { "通选", "单选" };
//		BallResId[0] = R.drawable.nmk3_normal;
//		BallResId[1] = R.drawable.nmk3_click;
		BallResId[0] = R.drawable.nmk3_hezhi_normal;
		BallResId[1] = R.drawable.nmk3_hezhi_click;
		setContentView(R.layout.sscbuyview);
		init();
		//来自2013-10-16徐培松 start
		zixuanLayout.setBackgroundResource(R.color.transparent);
		//。。。end
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		onCheckAction(checkedId);
		((BuyActivityGroup) getParent()).showBetInfo(textSumMoney(areaNums, iProgressBeishu));
		 isMissNet(new Nmk3MissJson(), sellWay, false);// 获取遗漏值
	}

	@Override
	protected void onResume() {
		super.onResume();
//		sensor.stopAction();
//		baseSensor.stopAction();
		editZhuma.setText(R.string.please_choose_number);
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int zhuShu = getZhuShu();

		if (zhuShu == 0) {
			return "请选择投注号码";
		} else {
			return "共" + zhuShu + "注，共" + zhuShu * 2 + "元";
		}
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
		return areaNums[0].table.getHighlightBallNums();
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

		// 如果是通选
		if (radioId == 0) {
			zhuMa = playMethodPart + mutiplePart + endFlagPart;
		}
		// 如果是单选
		else if (radioId == 1) {
			if (getZhuShu() > 1) {
				zhuMa = playMethodPart + mutiplePart + numberNumsPart
						+ numbersPart + endFlagPart;
			} else {
				zhuMa = playMethodPart + mutiplePart + numbersPart
						+ endFlagPart;
			}
		}
		return zhuMa.toString();
	}

	private String getNumberNumsPart() {
		return PublicMethod
				.getZhuMa(areaNums[0].table.getHighlightBallNOs().length);
	}

	private String getNumbersPart() {
		StringBuffer numbersPart = new StringBuffer();

		// 如果是通选
		if (radioId == 0) {
			numbersPart.append("");
		}
		// 如果是单选
		else if (radioId == 1) {
			// 获取高亮小球号码数组
			int[] numbers = areaNums[0].table.getHighlightBallNOs();

			// 如果是单选复试
			if (numbers.length > 1) {
				for (int number_i = 0; number_i < numbers.length; number_i++) {
					String numberPart = PublicMethod
							.getZhuMa(numbers[number_i] % 10);
					numbersPart.append(numberPart);
				}
			}
			// 如果是单选单式
			else if (numbers.length == 1) {
				String numberString = String.valueOf(numbers[0]);
				for (int number_i = 0; number_i < numberString.length(); number_i++) {
					numbersPart.append(PublicMethod.getZhuMa(Integer
							.valueOf(numberString.substring(number_i,
									number_i + 1))));
				}
			}
		}

		return numbersPart.toString();
	}

	private String getMutiplePart() {
		return "0001";
	}

	private String getPlayMethodPart() {
		String playMethodPart = "";

		if (radioId == 0) {
			playMethodPart = "40";
		} else if (radioId == 1) {
			if (getZhuShu() > 1) {
				playMethodPart = "81";
			} else {
				playMethodPart = "02";
			}
		}

		return playMethodPart;
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
		radioId = checkedId;
		initArea(checkedId);
		lotnoStr=Constants.LOTNO_NMK3;
		switch (checkedId) {
		case 0:
			sellWay = MissConstant.NMK3_THREESAME_TONG;
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_THREESAME_TONG,
					false, checkedId, true);
			break;
		case 1:
			sellWay = MissConstant.NMK3_THREE_DAN_FU;
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_THREESAME_DAN,
					false, checkedId, true);
			break;
		}
		zixuanLayout.setBackgroundResource(R.color.transparent);
	}

	public AreaNum[] initArea(int checkedId) {
		areaNums = new AreaNum[1];
		switch (checkedId) {
		case 0:
			highttype = "NMK3-THREESAME-TONG";
			areaNums[0] = new AreaNum(1, 1, 1, 1, BallResId, 0, 1, Color.RED,
					"", false, true);
			break;
		case 1:
			highttype = "NMK3-THREESAME-DAN";
			areaNums[0] = new AreaNum(6, 4, 1, 6, BallResId, 0, 1, Color.RED,
					"", false, true);
			break;
		}

		return areaNums;
	}

	/*
	 * 设置投注信息类的彩种编号和投注类型
	 */
	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		if (radioId == 0) {
			codeInfo.setTouZhuType("threesame_tong");
		} else if (radioId == 1) {
			codeInfo.setTouZhuType("threesame_dan");
		}
	}
}
