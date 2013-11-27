package com.ruyicai.activity.buy.qxc;

import android.os.Bundle;

import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.QXCJXBalls;
import com.ruyicai.pojo.AreaNum;

public class QXCJX extends DanshiJiXuan implements BuyImplement {

	public QXCJXBalls jixuanBalls = new QXCJXBalls();

	public void onCreate(Bundle savedInstanceState) {
		setAddView(((QXC)getParent()).addView);
		super.onCreate(savedInstanceState);
		createZhiXuan();
	}

	/**
	 * 直选
	 */
	private void createZhiXuan() {
		createView(jixuanBalls, this, false);
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
		betAndGift.setSellway("1");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_QXC);
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_QXC);
		codeInfo.setTouZhuType("dantuo");
	}

}
