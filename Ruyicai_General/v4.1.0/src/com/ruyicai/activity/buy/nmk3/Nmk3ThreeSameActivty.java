package com.ruyicai.activity.buy.nmk3;

import com.palmdream.RuyicaiAndroid.R;
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
	int threeSameBallZhuShu;
	int threeSameTongBallZhuShu;
	int[] BallResId1;
	int[] BallResId2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Nmk3Activity) getParent()).addView);
		super.onCreate(savedInstanceState);
		lotno = Constants.LOTNO_NMK3;
		highttype = "NMK3-SAME-THREE";
		childtype = new String[] { "直选" };
		BallResId1=new int[]{R.drawable.nmk3_hezhi_normal,R.drawable.nmk3_hezhi_click};
		BallResId2=new int[]{R.drawable.changbtn_normal,R.drawable.changbtn_click};
		init();
		//来自2013-10-16徐培松 start
		childtypes.setVisibility(View.GONE);
		zixuanLayout.setBackgroundResource(R.color.transparent);
		//。。。end
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		onCheckAction(checkedId);
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
			return "你已选择了" + zhuShu + "注，共" + zhuShu * 2 + "元";
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
		//获取三同号注数
		threeSameBallZhuShu = areaNums[0].table.getHighlightBallNums();
		//获取三同号通选注数
		threeSameTongBallZhuShu = areaNums[1].table.getHighlightBallNums();
		
		//返回总注数
		return threeSameBallZhuShu + threeSameTongBallZhuShu;
	}
	
	public int getThreeSameBallZhuShu(){
		return threeSameBallZhuShu;
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

		if (getThreeSameBallZhuShu() > 1) {
			zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
					+ endFlagPart;
		} else {
			zhuMa = playMethodPart + mutiplePart + numbersPart + endFlagPart;
		}
		return zhuMa.toString();
	}
	
	// 获取三同号通选注码
	public String getZhuma2() {
		// 拼接投注的注码格式，用户投注与后台使用
		String zhuMa = "";

		// 获取注码的各个部分
		String playMethodPart = getPlayMethodPart2();
		String mutiplePart = getMutiplePart2();
		String numberNumsPart = getNumberNumsPart2();
		String numbersPart = getNumbersPart2();
		String endFlagPart = "^";

		zhuMa = playMethodPart + mutiplePart + endFlagPart;
		
		return zhuMa.toString();
	}

	private String getNumbersPart2() {
		StringBuffer numbersPart = new StringBuffer();
		numbersPart.append("");
		return numbersPart.toString();
	}

	private String getNumberNumsPart2() {
		return PublicMethod
				.getZhuMa(areaNums[1].table.getHighlightBallNOs().length);
	}

	private String getMutiplePart2() {
		return "0001";
	}

	private String getPlayMethodPart2() {
		String playMethodPart = "";
		playMethodPart = "40";
		return playMethodPart;
	}

	private String getNumberNumsPart() {
		return PublicMethod
				.getZhuMa(areaNums[0].table.getHighlightBallNOs().length);
	}

	private String getNumbersPart() {
		StringBuffer numbersPart = new StringBuffer();

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
				numbersPart
						.append(PublicMethod.getZhuMa(Integer
								.valueOf(numberString.substring(number_i,
										number_i + 1))));
			}
		}

		return numbersPart.toString();
	}

	private String getMutiplePart() {
		return "0001";
	}

	private String getPlayMethodPart() {
		String playMethodPart = "";

		if (getThreeSameBallZhuShu() > 1) {
			playMethodPart = "81";
		} else {
			playMethodPart = "02";
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
		initArea(checkedId);
		lotnoStr=Constants.LOTNO_NMK3;
		
		switch (checkedId) {
		case 0:
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_THREESAME,
					false, checkedId, true);
			// 获取遗漏值
			isMissNet(new Nmk3MissJson(), MissConstant.NMK3_THREE_DAN_FU + ";" + MissConstant.NMK3_THREESAME_TONG, false);
			break;
		}
		zixuanLayout.setBackgroundResource(R.color.transparent);
	}

	public AreaNum[] initArea(int checkedId) {
		areaNums = new AreaNum[2];
		switch (checkedId) {
		case 0:
			areaNums[0] = new AreaNum(6, 4, 1, 6, BallResId1, 0, 1, Color.RED,
					"三同号单选：猜3个相同的号码，奖金240元！", false, true);
			areaNums[1] = new AreaNum(1, 1, 1, 1, BallResId2, 0, 1, Color.RED,
					"三同号通选：任意一个三同号开出，即中40元！", false, true);
			break;
		}

		return areaNums;
	}
	
	int getThreeLinkZhuShu() {
		return threeSameTongBallZhuShu;
	}

	int getThreeDiffZhuShu() {
		return threeSameBallZhuShu;
	}

	/*
	 * 设置投注信息类的彩种编号和投注类型
	 */
	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		codeInfo.setTouZhuType("threesame_dan");
	}

	void setLotoNoAndType2(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		codeInfo.setTouZhuType("threesame_tong");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeMediaPlayer();
		NmkAnimation.flag = true;
	}
}
