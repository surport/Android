package com.ruyicai.activity.buy.twentytwo;

import android.os.Bundle;

import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.SsqDSBalls;
import com.ruyicai.jixuan.TwentyBalls;
import com.ruyicai.pojo.AreaNum;

public class TwentyJiXuan extends DanshiJiXuan implements BuyImplement {

	public TwentyBalls Balls = new TwentyBalls();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createView(Balls, this, false);
	}

	public void isTouzhu() {
		// TODO Auto-generated method stub
	}

	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		return null;
	}

	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("1");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_22_5);
	}

}
