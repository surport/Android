package com.ruyicai.activity.buy.dlt;

import android.os.Bundle;
import android.widget.ToggleButton;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.DltSingleBalls;
import com.ruyicai.pojo.AreaNum;

public class DltRandomChose extends DanshiJiXuan implements BuyImplement {

	private ToggleButton zhuijiatouzhu;
	private int singleLotteryValue = 2;

	public DltSingleBalls dltSingleBalls = new DltSingleBalls();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createView(dltSingleBalls, this, true);
	}

	@Override
	public void isTouzhu() {

	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		return null;
	}

	@Override
	public void touzhuNet() {
		betAndGift.setSellway("1");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_DLT);
		betAndGift.setZhui(true);

	}

}