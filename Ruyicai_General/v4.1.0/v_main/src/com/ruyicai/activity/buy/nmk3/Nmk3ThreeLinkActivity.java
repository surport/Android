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

/**
 * Nmk3ThreeLinkActivity:内蒙古快三三连号
 * 
 * @author PengCX
 * 
 */
public class Nmk3ThreeLinkActivity extends ZixuanAndJiXuan {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Nmk3Activity) getParent()).addView);
		super.onCreate(savedInstanceState);
		lotno = Constants.LOTNO_NMK3;
		childtype = new String[] { "直选" };
		setContentView(R.layout.sscbuyview);
		BallResId[0] = R.drawable.nmk3_normal;
		BallResId[1] = R.drawable.nmk3_click;
		highttype = "NMK3-THREE-LINK";
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
		sensor.stopAction();
		baseSensor.stopAction();
		editZhuma.setText(R.string.please_choose_number);
		editZhuma.setTextColor(Color.BLACK);
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

		// 拼接注码
		zhuMa = playMethodPart + mutiplePart + numberNumsPart + numbersPart
				+ endFlagPart;

		return zhuMa;
	}

	private String getNumbersPart() {
		return "";
	}

	private String getNumberNumsPart() {
		return "";
	}

	private String getMutiplePart() {
		return "0001";
	}

	private String getPlayMethodPart() {
		return "50";
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
		lotnoStr=Constants.LOTNO_NMK3;
		sellWay = MissConstant.NMK3_THREE_LINK_TONG;
		switch (checkedId) {
		case 0:
			initArea();
			createView(areaNums, sscCode, ZixuanAndJiXuan.NMK3_THREE_LINK,
					true, checkedId, true);
			 isMissNet(new Nmk3MissJson(), sellWay, false);// 获取遗漏值
			break;
		}
	}

	public AreaNum[] initArea() {
		areaNums = new AreaNum[1];
		areaNums[0] = new AreaNum(1, 1, 1, 1, BallResId, 0, 1, Color.RED, "",
				false, true);
		return areaNums;
	}

	/*
	 * 设置投注信息类的彩种编号和投注类型
	 */
	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_NMK3);
		codeInfo.setTouZhuType("threelink");
	}
}
