/**
 * 
 */
package com.ruyicai.activity.buy.qlc;

import android.graphics.Color;
import android.os.Bundle;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.qlc.QlcZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

/**
 * 七乐彩自选直选
 * 
 * @author Administrator
 * 
 */
public class QlcZiZhiXuan extends ZixuanActivity {
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[1];// 1个选区
	private QlcZiZhiXuanCode qlcCode = new QlcZiZhiXuanCode();
	BallTable ballTable;

	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Qlc)getParent()).addView);
		super.onCreate(savedInstanceState);
		setCode(qlcCode);
		setIsTen(true);
		initViewItem();
		ballTable = itemViewArray.get(0).areaNums[0].table;
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		// TODO Auto-generated method stub
		BuyViewItem buyView = new BuyViewItem(this, initArea());
		itemViewArray.add(buyView);
		layoutView.addView(buyView.createView());
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[1];
		String redTitle = getResources().getString(R.string.xuanhaoqu)
				.toString();
		areaNums[0] = new AreaNum(30, 8, 7, 15, ballResId, 0, 1, Color.RED,
				redTitle, true, true);
		return areaNums;
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public int getZhuShu() {
		int iRedHighlights = ballTable.getHighlightBallNums();
		int iZhuShu = (int) getQLCFSZhuShu(iRedHighlights);
		return iZhuShu;
	}

	/**
	 * 投注提示框中的信息(注码)
	 * 
	 * @return
	 */
	public String getZhuma() {
		String red_zhuma_string = " ";
		int[] redZhuMa = ballTable.getHighlightBallNOs();
		for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
			if (i != ballTable.getHighlightBallNOs().length - 1)
				red_zhuma_string += ",";
		}
		return "注码：" + "\n" + red_zhuma_string;

	}

	/**
	 * 判断是否满足投注条件
	 */
	public String isTouzhu() {
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		if (ballTable.getHighlightBallNums() < 7
				|| ballTable.getHighlightBallNums() > 16) {
			isTouzhu = "请至少选择7~16个球";
		} else if (iZhuShu * 2 > 100000) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 点击小球提示金额
	 * 
	 * @param areaNum
	 * @param iProgressBeishu
	 * @return
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String iTempString = "";
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		// int iBlueHighlights = blueBallTable.getHighlightBallNums();

		// 红球数 不足//fqc edit 当个数满足个数的时候显示相应的提示
		if (iRedHighlights < 7) {
			int num = 7 - iRedHighlights;
			return "至少还需要" + num + "个小球";
		} else {
			int iZhuShu = (int) getQLCFSZhuShu(iRedHighlights);
			return iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		}
	}

	/*
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * 
	 * @return long 注数
	 */
	private long getQLCFSZhuShu(int aRedBalls) {
		long qlcZhuShu = 0L;
		if (aRedBalls > 0) {
			qlcZhuShu += (PublicMethod.zuhe(7, aRedBalls) * iProgressBeishu);
		}
		return qlcZhuShu;

	}

	/**
	 * 投注联网
	 */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_QLC);
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_QLC);
		codeInfo.setTouZhuType("zhixuan");
	}
}
