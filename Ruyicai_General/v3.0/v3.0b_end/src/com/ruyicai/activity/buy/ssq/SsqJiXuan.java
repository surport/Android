/**
 * 
 */
package com.ruyicai.activity.buy.ssq;

import android.os.Bundle;

import com.ruyicai.activity.buy.DanshiJiXuan;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.SsqDSBalls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.Constants;

/**
 * @author Administrator
 *
 */
public class SsqJiXuan extends DanshiJiXuan implements BuyImplement{
	public SsqDSBalls ssqBalls = new SsqDSBalls();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createView(ssqBalls,this,true);
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
		betAndGift.setSellway("1");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_SSQ);
	}
	
	

}
