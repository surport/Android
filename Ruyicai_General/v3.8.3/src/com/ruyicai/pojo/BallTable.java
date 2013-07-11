package com.ruyicai.pojo;

/**
 * @Title 封装BallTable
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class BallTable {
	public Context context;
	LinearLayout a;
	public TableLayout tableLayout;
	public Vector<OneBallView> ballViewVector = null;
	public int startId;
	public int lineNum;
	public int lieNum;
	public float textHeight;
	public TextView textView;
	public List<TextView> textList = new ArrayList<TextView>();

	/**
	 * 构造函数
	 * 
	 * @param LinearLayout
	 *            aV 上一级Layout
	 * @param int aLayoutId 当前的LayoutId
	 * @param int aStartId 小球Id起始数值
	 * @return void
	 */
	public BallTable(LinearLayout aV, int aLayoutId, int aStartId) {
		a = aV;
		ballViewVector = new Vector<OneBallView>();

		tableLayout = (TableLayout) a.findViewById(aLayoutId);
		tableLayout.setStretchAllColumns(true);

		startId = aStartId;
	}

	/**
	 * 构造函数
	 * 
	 * @param int aStartId 小球Id起始数值
	 * @return void
	 */
	public BallTable(int aStartId, Context context) {
		ballViewVector = new Vector<OneBallView>();
		startId = aStartId;
		this.context = context;
	}

	/**
	 * 添加小球View
	 * 
	 * @param OneBallView
	 *            aBall 要添加的小球实例
	 * @return void
	 */
	public void addBallView(OneBallView aBall) {
		ballViewVector.add(aBall);
	}

	/**
	 * 获取高亮小球号码 —— 为实际号码，从1开始计数
	 * 
	 * @return int[] 小球号码
	 */
	public int[] getHighlightBallNOs() {
		int i = 0;
		int iSum = ballViewVector.size();
		int iHighlightSum = 0;

		for (i = 0; i < iSum; i++) {
			iHighlightSum += getOneBallStatue(i);
		}

		int[] iBallNOs = new int[iHighlightSum];
		int iCurrent = 0;
		for (i = 0; i < iSum; i++) {
			if (getOneBallStatue(i) == 1) {
				String num = ((OneBallView) ballViewVector.elementAt(i))
						.getNum();
				//add by yejc 20130326
				if (num != null) {
					if (num.contains("胜")) {
						num = "3";
					} else if (num.contains("平")) {
						num = "1";
					} else if (num.contains("负")) {
						num = "0";
					}
				}
				//end
				if (!num.equals("胆") && !num.equals("三同号通选")
						&& !num.equals("三连号通选")) {
					if (num.contains("*")) {
						num = num.substring(0, 2);
					}
					iBallNOs[iCurrent] = i + 1;
					iBallNOs[iCurrent] = Integer.parseInt(num);
					iCurrent++;
				} else {
					iBallNOs[iCurrent] = -1;
				}
			}
		}
		return iBallNOs;
	}

	/**
	 * 获取高亮小球号码 —— 为实际号码，从1开始计数
	 * 
	 * @return int[] 小球号码
	 */
	public String[] getHighlightStr() {
		int i = 0;
		int iSum = ballViewVector.size();
		int iHighlightSum = 0;

		for (i = 0; i < iSum; i++) {
			iHighlightSum += getOneBallStatue(i);
			// ((OneBallView)ballViewVector.elementAt(i)).getShowId();
		}

		String[] iBallNOs = new String[iHighlightSum];
		int iCurrent = 0;
		for (i = 0; i < iSum; i++) {
			if (getOneBallStatue(i) == 1) {
				iBallNOs[iCurrent] = "" + i + 1;
				iBallNOs[iCurrent] = ((OneBallView) ballViewVector.elementAt(i))
						.getNum();
				iCurrent++;
			}
		}
		return iBallNOs;
	}

	/**
	 * 获取高亮小球号码 —— 为实际号码，从1开始计数
	 * 
	 * @return int[] 小球号码大小双单专用
	 */
	public int[] getHighlightBallNOsbigsmall() {
		int i = 0;
		int iSum = ballViewVector.size();
		int iHighlightSum = 0;

		for (i = 0; i < iSum; i++) {
			iHighlightSum += getOneBallStatue(i);
			// ((OneBallView)ballViewVector.elementAt(i)).getShowId();
		}

		int[] iBallNOs = new int[iHighlightSum];
		int iCurrent = 0;
		for (i = 0; i < iSum; i++) {
			if (getOneBallStatue(i) == 1) {
				iBallNOs[iCurrent] = i + 1;
				// Log.v("iBallNOs", iBallNOs[iCurrent]+"");
				iCurrent++;
			}
		}
		return iBallNOs;
	}

	/**
	 * 返回小球View
	 */
	public Vector getBallViews() {
		return ballViewVector;
	}

	/**
	 * 获取全部小球状态
	 * 
	 * @return int[] 全部小球状态的数组
	 */
	public int[] getBallStatus() {
		int i = 0;
		int iSum = ballViewVector.size();
		int[] iBallStatus = new int[iSum];
		// iBallStatus = new int [ballViewVector.size()];

		for (i = 0; i < iSum; i++) {
			iBallStatus[i] = getOneBallStatue(i);
			// ((OneBallView)ballViewVector.elementAt(i)).getShowId();
		}
		return iBallStatus;
	}

	/**
	 * 去掉小球高亮
	 * 
	 * @param int aBallId 小球Id
	 */
	public void clearOnBallHighlight(int aBallId) {
		if (getOneBallStatue(aBallId) > 0) {
			((OneBallView) ballViewVector.elementAt(aBallId)).changeBallColor();
		}
	}

	/**
	 * 获取某个小球的状态
	 * 
	 * @param int aBallId 小球Id
	 * @return int 某个小球状态
	 */
	public int getOneBallStatue(int aBallId) {
		return ((OneBallView) ballViewVector.elementAt(aBallId)).getShowId();
	}

	/**
	 * 获取高亮小球个数
	 * 
	 * @return int 高亮小球个数
	 */
	public int getHighlightBallNums() {
		int[] ballStates = getBallStatus();
		int iChosenBallSum = 0;
		for (int i = 0; i < ballStates.length; i++) {
			iChosenBallSum += ballStates[i];
		}

		return iChosenBallSum;
	}

	/**
	 * 改变某个小球的状态，并返回是否修改小球状态
	 * 
	 * @param int aMaxHighlight 当前最大高亮小球的个数
	 * @param int aBallId 要改变的小球的Id
	 * @return int 0:未改变 1:
	 */
	public int changeBallState(int aMaxHighlight, int aBallId) {
		int iChosenBallSum = getHighlightBallNums();
		int iCurrentBallStatue = getOneBallStatue(aBallId);
		if (iCurrentBallStatue > 0) {
			ballViewVector.elementAt(aBallId).changeBallColor();
			return PublicConst.BALL_HIGHLIGHT_TO_NOT;
		} else {
			if (iChosenBallSum >= aMaxHighlight) {
				return 0;
			} else {
				ballViewVector.elementAt(aBallId).changeBallColor();
				return PublicConst.BALL_TO_HIGHLIGHT;
			}
		}
	}

	/**
	 * 把高亮小球变成不高亮的小球
	 * 
	 * @param int aBallId 要改变的小球的Id
	 * @return int 0:未改变 1:
	 */
	public void changeBallState(int aBallId) {
		int iCurrentBallStatue = getOneBallStatue(aBallId);
		if (iCurrentBallStatue > 0) {
			ballViewVector.elementAt(aBallId).changeBallColor();
		}
	}

	public void changeBallStateConfigChange(int iBallId[]) {
		for (int i = 0; i < iBallId.length; i++) {
			if (iBallId[i] == 1) {
				ballViewVector.elementAt(i).changeBallColor();
			}
		}
	}

	/**
	 * 小球全部不高亮
	 * 
	 * @return void
	 */
	public void clearAllHighlights() {

		for (int i = 0; i < ballViewVector.size(); i++) {
			changeBallState(i);
		}
	}

	/**
	 * 随机高亮小球
	 * 
	 * @param int aRandomNums 随机高亮小球的数量
	 * @return void
	 */
	public void randomChoose(int aRandomNums) {
		int i;
		clearAllHighlights();
		int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
				aRandomNums, 0, ballViewVector.size() - 1);
		for (i = 0; i < iHighlightBallId.length; i++) {
			changeBallState(aRandomNums, iHighlightBallId[i]);
		}

	}

	public void randomChooseConfigChange(int aRandomNums, int whichGroupBall) {
		clearAllHighlights();
		if (whichGroupBall == 0) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		if (whichGroupBall == 1) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}

	}

	public void sscRandomChooseConfigChange(int aRandomNums) {
		clearAllHighlights();

		int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
				aRandomNums, 0, ballViewVector.size() - 1);
		for (int i = 0; i < iHighlightBallId.length; i++) {
			int isHighLight = changeBallState(aRandomNums, iHighlightBallId[i]);

		}

	}

	/**
	 * 用于福彩3D横纵屏切换
	 * 
	 * @param aRandomNums
	 *            允许高亮小球的个数
	 * @param mBallHolder
	 *            记录当前页面各个控件状态的累
	 * @param whichGroupBall
	 *            是哪个BallTable
	 * @param randomNumbers
	 *            传的随机数（为组三单式玩法、胆拖玩法、和值玩法准备）
	 * @return
	 */
	public void randomChooseConfigChangeFc3d(int aRandomNums,
			int whichGroupBall, int[] randomNumbers) {
		clearAllHighlights();
		// 直选百位
		if (whichGroupBall == 7) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 直选十位
		if (whichGroupBall == 8) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 直选个位
		if (whichGroupBall == 9) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 组3单式
		if (whichGroupBall == 0) {
			int isHighLight = changeBallState(1, randomNumbers[0]);

		}
		if (whichGroupBall == 1) {
			int isHighLight = changeBallState(1, randomNumbers[0]);

		}
		if (whichGroupBall == 2) {
			int isHighLight = changeBallState(1, randomNumbers[1]);

		}
		// 组3复式
		if (whichGroupBall == 3) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 组6
		if (whichGroupBall == 4) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 胆拖胆码
		if (whichGroupBall == 5) {
			for (int i = 0; i < aRandomNums; i++) {
				int isHighLight = changeBallState(aRandomNums, randomNumbers[i]);

			}
		}
		// 胆拖拖码
		if (whichGroupBall == 6) {
			for (int i = randomNumbers.length - aRandomNums; i < randomNumbers.length; i++) {
				int isHighLight = changeBallState(aRandomNums, randomNumbers[i]);

			}
		}
		// 和值直选
		if (whichGroupBall == 10) {
			int isHighLight = changeBallState(aRandomNums, randomNumbers[0]);

		}
		// 和值组3
		if (whichGroupBall == 11) {
			int isHighLight = changeBallState(aRandomNums, randomNumbers[0]);

		}
		// 和值组6
		if (whichGroupBall == 12) {
			int isHighLight = changeBallState(aRandomNums, randomNumbers[0]);

		}

	}

	/**
	 * 用于排列三横纵屏切换
	 * 
	 * @param aRandomNums
	 *            允许高亮小球的个数
	 * @param mBallHolder
	 *            记录当前页面各个控件状态的累
	 * @param whichGroupBall
	 *            是哪个BallTable
	 * @param randomNumbers
	 *            传的随机数（为组三单式玩法、胆拖玩法、和值玩法准备）
	 * @return
	 */
	public void randomChooseConfigChangePL3(int aRandomNums,
			int whichGroupBall, int[] randomNumbers) {
		clearAllHighlights();
		// 直选百位
		if (whichGroupBall == 7) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 直选十位
		if (whichGroupBall == 8) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 直选个位
		if (whichGroupBall == 9) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 组3单式
		if (whichGroupBall == 0) {
			int isHighLight = changeBallState(1, randomNumbers[0]);

		}
		if (whichGroupBall == 1) {
			int isHighLight = changeBallState(1, randomNumbers[0]);

		}
		if (whichGroupBall == 2) {
			int isHighLight = changeBallState(1, randomNumbers[1]);

		}
		// 组3复式
		if (whichGroupBall == 3) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 组6
		if (whichGroupBall == 4) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 和值直选
		if (whichGroupBall == 10) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, 27);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 和值组3
		if (whichGroupBall == 11) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, 25);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
		// 和值组6
		if (whichGroupBall == 12) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, 21);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);

			}
		}
	}

	// 设置高亮小球的最大数值
	public void setMaxHighLight(int aNum) {

	}

	/**
	 * 随机高亮小球 返回id
	 * 
	 * @param int aRandomNums 随机高亮小球的数量
	 * @return void
	 */
	public int[] randomChooseId(int aRandomNums) {
		int i;
		clearAllHighlights();
		int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
				aRandomNums, 0, ballViewVector.size() - 1);
		int[] iBallId = new int[iHighlightBallId.length];
		for (i = 0; i < iHighlightBallId.length; i++) {
			iBallId[i] = ballViewVector.elementAt(iHighlightBallId[i]).getId();
		}
		return iBallId;
	}

	/**
	 * 随机高亮小球 返回id
	 * 
	 * @param int aRandomNums 随机高亮小球的数量
	 * @return void
	 */
	public int getRandomChooseId(int iHighlightBallId) {
		// clearAllHighlights();
		int iBallId = ballViewVector.elementAt(iHighlightBallId).getId();
		return iBallId;
	}

	/**
	 * 获取某个小球的号码
	 * 
	 * @param int aBallId 小球Id
	 * @return int 某个小球状态
	 */
	public String getOneBallNum(int aBallId) {
		return ((OneBallView) ballViewVector.elementAt(aBallId)).getNum();
	}

	public int getStartId() {

		return startId;

	}

	public void clearAllBall() {
		ballViewVector = new Vector<OneBallView>();
	}

}