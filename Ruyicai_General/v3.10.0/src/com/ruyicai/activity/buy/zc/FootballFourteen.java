package com.ruyicai.activity.buy.zc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public abstract class FootballFourteen extends FootBallLotteryFather {
	String phonenum, sessionid, userno;
	boolean isOne = true;// 是否是第一次进入

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * <b>创建足彩胜负彩任选九BallTable</b>
	 * 
	 * @param LinearLayoutaParentView
	 *            上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	@SuppressLint("ResourceAsColor")
	public BallTable makeBallTable(LinearLayout aParentView, int aLayoutId,
			int[] aResId, int aIdStart, String text) {
		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		int aFieldWidth = iScreenWidth / 3;
		int iBallViewWidth = aFieldWidth / 3 - 2;
		/** 每一行的小球数量 */
		int viewNumPerLine = 3;
		/** 行的数量 */
		int lineNum = 1;

		int iBallViewNo = 0;
		//add by yejc 20130326
		String str[] = null;
		if (text != null && !text.equals("")) {
			str = text.split("\\|");
		}
		//end

		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				/** 设置显示的数字 */
				String iStrTemp = "" + (iBallViewNo + 1);
				if (iStrTemp.equals("1")) {
					iStrTemp = getString(str, 0, "胜"); //add by yejc 20130326
				} else if (iStrTemp.equals("2")) {
					iStrTemp = getString(str, 1, "平"); //add by yejc 20130326
				} else {
					iStrTemp = getString(str, 2, "负"); //add by yejc 20130326
				}

				/** 实例化一个BallView对象 */
				OneBallView tempBallView = new OneBallView(
						aParentView.getContext(), true);
				/** 为这个tempView设置一个Id */
				tempBallView.setId(aIdStart + iBallViewNo);
				/** 将这个小球初始化出来 */
				tempBallView.initBall(iBallViewWidth+22, iBallViewWidth, iStrTemp,
						aResId);

				/*** 将小球tempView添加到Table中 */
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				lp.gravity = Gravity.CENTER; //add by yejc 20130327
				tableRow.addView(tempBallView, lp);
				/** iBallViewNo自增，循环设置小球的属性 */
				iBallViewNo++;
			}
			/** 新建的TableRow添加到TableLayout */
			tableRow.setGravity(Gravity.CENTER); //add by yejc 20130327
			iBallTable.tableLayout
					.addView(tableRow, new TableLayout.LayoutParams(
							PublicConst.FP, PublicConst.WC));
		}

		return iBallTable;
	}
	
	public String getString(String[] str, int index, String text) {
		if (str != null && str.length > index) {
			return str[index];
		} else {
			return text;
		}
	}
}
