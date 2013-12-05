package com.ruyicai.activity.buy.nmk3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.Nmk3MissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * Nmk3TwoSameActivity: 内蒙古快三二同号
 * 
 * @author PengCX
 * 
 */
public class Nmk3TwoSameActivty extends ZixuanAndJiXuan {
	int sameNum;
	int diffNum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Nmk3Activity) getParent()).addView);
		super.onCreate(savedInstanceState);
		lotno = Constants.LOTNO_NMK3;
		childtype = new String[] { "复选", "单选" };
		BallResId[0] = R.drawable.nmk3_hezhi_normal;
		BallResId[1] = R.drawable.nmk3_hezhi_click;
		init();
		//来自2013-10-16徐培松 start
		zixuanLayout.setBackgroundResource(R.color.transparent);
		//。。。end
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		radioId = checkedId;
		if(scrollView != null){
			scrollView.smoothScrollBy(0, 0);
		}
		
		onCheckAction(checkedId);
		((Nmk3Activity) getParent()).showBetInfo(textSumMoney(areaNums,
				iProgressBeishu));
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
		if (zhuShu == 0) {
			return "请选择投注号码";
		} else {
			return "您已选择了" + zhuShu + "注，共" + zhuShu * 2 + "元";
		}
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
		int zhuShu = 0;

		if (highttype.equals("NMK3-TWOSAME-FU")) {
			zhuShu = areaNums[0].table.getHighlightBallNums();
		} else {
			sameNum = areaNums[0].table.getHighlightBallNums();
			diffNum = areaNums[1].table.getHighlightBallNums();
			zhuShu = sameNum * diffNum;
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

		// 拼接注码
		if (radioId == 0) {
			zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
					+ endFlagPart;
		} else {
			zhuMa = playMethodPart + mutiplePart + numbersPart + endFlagPart;
		}

		return zhuMa;
	}

	private String getNumberNumsPart() {
		return PublicMethod
				.getZhuMa(areaNums[0].table.getHighlightBallNOs().length);
	}

	private String getNumbersPart() {
		StringBuffer numbersPart = new StringBuffer();
		// 如果是复选
		if (radioId == 0) {
			// 获取高亮小球号码数组
			int[] numbers = areaNums[0].table.getHighlightBallNOs();
			for (int number_i = 0; number_i < numbers.length; number_i++) {
				String numberPart = PublicMethod
						.getZhuMa(numbers[number_i] % 10);
				numbersPart.append(numberPart);
			}
		}
		// 如果是单选
		else if (radioId == 1) {
			// 如果是复式
			if (getZhuShu() > 1) {
				for (int aear_i = 0; aear_i < areaNums.length; aear_i++) {
					int[] aearnumbers = areaNums[aear_i].table
							.getHighlightBallNOs();
					StringBuffer areanumberPart = new StringBuffer();
					for (int number_j = 0; number_j < aearnumbers.length; number_j++) {
						String numberPart = "";
						if (aear_i == 0) {
							numberPart = PublicMethod
									.getZhuMa(aearnumbers[number_j] % 10);

						} else {
							numberPart = PublicMethod
									.getZhuMa(aearnumbers[number_j]);
						}
						areanumberPart.append(numberPart);

					}
					numbersPart.append(areanumberPart);

					if (aear_i != areaNums.length - 1) {
						numbersPart.append("*");
					}

				}
			}
			// 如果是单式
			else if (getZhuShu() == 1) {
				// 分别获取两个选号面板的号码
				int[] aearnumbers0 = areaNums[0].table.getHighlightBallNOs();
				int[] aearnumbers1 = areaNums[1].table.getHighlightBallNOs();
				String numberPart = "";
				// 判断面板号码的大小
				if ((aearnumbers0[0] % 10) > aearnumbers1[0]) {
					montageSmallNumber(numbersPart, aearnumbers1);
					montageBigNumber(numbersPart, aearnumbers0);
				} else {
					montageBigNumber(numbersPart, aearnumbers0);
					montageSmallNumber(numbersPart, aearnumbers1);
				}
			}
		}

		return numbersPart.toString();
	}

	private void montageBigNumber(StringBuffer numbersPart, int[] aearnumbers0) {
		String numberPart;
		// 在拼接前面大的号码
		String numbers = String.valueOf(aearnumbers0[0]);
		for (int number_j = 0; number_j < numbers.length(); number_j++) {
			numberPart = PublicMethod.getZhuMa(Integer.valueOf((String) numbers
					.subSequence(number_j, number_j + 1)));
			numbersPart.append(numberPart);
		}
	}

	private void montageSmallNumber(StringBuffer numbersPart, int[] aearnumbers1) {
		String numberPart;
		// 拼接后面小的号码
		numberPart = PublicMethod.getZhuMa(aearnumbers1[0]);
		numbersPart.append(numberPart);
	}

	private String getMutiplePart() {
		return "0001";
	}

	private String getPlayMethodPart() {
		String playMethodString = "";

		if (radioId == 0) {
			playMethodString = "30";
		} else {
			if (getZhuShu() > 1) {
				playMethodString = "71";
			} else {
				playMethodString = "01";
			}
		}

		return playMethodString;
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
		lotnoStr=Constants.LOTNO_NMK3;
		switch (checkedId) {
		case 0:
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_TWOSAME_FU,
					true, checkedId, true);
			 isMissNet(new Nmk3MissJson(), MissConstant.NMK3_TWOSAME_FU, false);// 获取遗漏值
			break;
		case 1:
			highttype="NMK3-TWOSAME-DAN";
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_TWOSAME_DAN,
					true, checkedId, true);
			 isMissNet(new Nmk3MissJson(), MissConstant.NMK3_TWO_DAN, false);// 获取遗漏值
			 editZhuma.setText(R.string.please_choose_number);
			editZhuma.setTextColor(Color.BLACK);
			break;
		}
		zixuanLayout.setBackgroundResource(R.color.transparent);
	}

	public AreaNum[] initArea(int checkedId) {
		switch (checkedId) {
		case 0:
			highttype = "NMK3-TWOSAME-FU";
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(6, 4, 1, 6, BallResId, 0, 1, Color.RED,
					" 复选：猜开奖中两个指定的相同号码，奖金15元！", false, true);
			break;
		case 1:
			highttype = "NMK3-TWOSAME-DAN";
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(6, 4, 1, 6, BallResId, 0, 1, Color.RED,
					"单选：选择同号和不同号的组合，奖金80元！\n同号:", false, true,true);
			areaNums[1] = new AreaNum(6, 4, 1, 6, BallResId, 0, 1, Color.RED,
					"不同号：", false, true,true);
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
			codeInfo.setTouZhuType("twosame_fu");
		} else if (radioId == 1) {
			codeInfo.setTouZhuType("twosame_dan");
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeMediaPlayer();
		NmkAnimation.flag = true;
	}
}
