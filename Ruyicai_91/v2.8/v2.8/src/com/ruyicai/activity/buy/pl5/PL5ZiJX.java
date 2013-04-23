package com.ruyicai.activity.buy.pl5;

import android.os.Bundle;

import com.ruyicai.activity.buy.DanshiJiXuan;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.PL5JXBalls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.Constants;

public class PL5ZiJX extends DanshiJiXuan implements BuyImplement{

	public PL5JXBalls zhixuanBalls = new  PL5JXBalls();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createZhiXuan();
	}
	/**
	 * 直选
	 */
	private void createZhiXuan(){
		createView(zhixuanBalls,this);
	}
	@Override
	public void isTouzhu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub
		int iZhuShu = balls.size()* iProgressBeishu;
		betAndGift.setSellway("1");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_PL5);
		betAndGift.setAmount(""+iZhuShu*200);
	}

}
