package com.palmdream.RuyicaiAndroid;

/**
 * @Title 封装BallTable
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */

import java.util.Vector;

import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.palmdream.RuyicaiAndroid.BallBetPublicClass.BallHolder;
import com.palmdream.RuyicaiAndroid.FC3DTest.BallHolderFc3d;
import com.palmdream.RuyicaiAndroid.PL3.BallHolderPL3;

public class BallTable {
	LinearLayout a;
	public TableLayout tableLayout;
	public Vector<OneBallView> ballViewVector = null;
	public int startId;

	/*
	 * 构造函数
	 * 
	 * @param LinearLayout aV 上一级Layout
	 * 
	 * @param int aLayoutId 当前的LayoutId
	 * 
	 * @param int aStartId 小球Id起始数值
	 * 
	 * @return void
	 */
	public BallTable(LinearLayout aV, int aLayoutId, int aStartId) {
		a = aV;
		ballViewVector = new Vector<OneBallView>();

		tableLayout = (TableLayout) a.findViewById(aLayoutId);
		tableLayout.setStretchAllColumns(true);

		startId = aStartId;
	}

	/*
	 * 添加小球View
	 * 
	 * @param OneBallView aBall 要添加的小球实例
	 * 
	 * @return void
	 */
	public void addBallView(OneBallView aBall) {
		ballViewVector.add(aBall);
	}

	/*
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
			// ((OneBallView)ballViewVector.elementAt(i)).getShowId();
		}

		int[] iBallNOs = new int[iHighlightSum];
		int iCurrent = 0;
		for (i = 0; i < iSum; i++) {
			if (getOneBallStatue(i) == 1) {
				iBallNOs[iCurrent] = i + 1;
				iCurrent++;
			}
		}
		return iBallNOs;
	}

	/*
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

	/*
	 * 去掉小球高亮
	 * 
	 * @param int aBallId 小球Id
	 */
	public void clearOnBallHighlight(int aBallId) {
		if (getOneBallStatue(aBallId) > 0) {
			((OneBallView) ballViewVector.elementAt(aBallId)).showNextId();
		}
	}

	/*
	 * 获取某个小球的状态
	 * 
	 * @param int aBallId 小球Id
	 * 
	 * @return int 某个小球状态
	 */
	public int getOneBallStatue(int aBallId) {
		return ((OneBallView) ballViewVector.elementAt(aBallId)).getShowId();
	}

	/*
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

	/*
	 * 改变某个小球的状态，并返回是否修改小球状态
	 * 
	 * @param int aMaxHighlight 当前最大高亮小球的个数
	 * 
	 * @param int aBallId 要改变的小球的Id
	 * 
	 * @return int 0:未改变 1:
	 */
	// public void changeBallState(int aMaxHighlight,int aBallId){
	public int changeBallState(int aMaxHighlight, int aBallId) {
		int iChosenBallSum = getHighlightBallNums();
		int iCurrentBallStatue = getOneBallStatue(aBallId);

		if (iCurrentBallStatue > 0) {
			ballViewVector.elementAt(aBallId).showNextId();
			return PublicConst.BALL_HIGHLIGHT_TO_NOT;
		} else {
			if (iChosenBallSum >= aMaxHighlight) {
				// 提示 去复式 进行投注 可用对话框
				return 0;
			} else {
				ballViewVector.elementAt(aBallId).showNextId();
				return PublicConst.BALL_TO_HIGHLIGHT;
			}
		}
	}

	public void changeBallStateConfigChange(int iBallId[]) {
		for (int i = 0; i < iBallId.length; i++) {
			if (iBallId[i] == 1) {
				ballViewVector.elementAt(i).showNextId();
			}
		}
	}

	/*
	 * 小球全部不高亮
	 * 
	 * @return void
	 */
	public void clearAllHighlights() {
		int i;
		// turn off all ball
		for (i = 0; i < ballViewVector.size(); i++) {
			changeBallState(0, i);
		}
	}

	/*
	 * 随机高亮小球
	 * 
	 * @param int aRandomNums 随机高亮小球的数量
	 * 
	 * @return void
	 */
	public void randomChoose(int aRandomNums) {
		int i;
		// turn off all ball
		// for(i=0;i<ballViewVector.size();i++){
		// changeBallState(0,i);
		// }
		clearAllHighlights();
		int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
				aRandomNums, 0, ballViewVector.size() - 1);
		for (i = 0; i < iHighlightBallId.length; i++) {
			// PublicMethod.myOutput("-----"+i+"   "+iHighlightBallId[i]);
			changeBallState(aRandomNums, iHighlightBallId[i]);
		}
		/*
		 * if(aBallColor.equalsIgnoreCase("red")){ // 这里参数为起始小球的id，如26个球，则为0、25
		 * int[] iHighlightBallId=PublicMethod.getRandomsWithoutCollision(6, 0,
		 * ballViewVector.size()-1); for(i=0;i<iHighlightBallId.length;i++){
		 * //PublicMethod.myOutput("-----"+i+"   "+iHighlightBallId[i]);
		 * changeBallState(6, iHighlightBallId[i]); } } else{ // blue int[]
		 * iHighlightBallId=PublicMethod.getRandomsWithoutCollision(1, 0,
		 * ballViewVector.size()-1); for(i=0;i<iHighlightBallId.length;i++){
		 * //PublicMethod.myOutput("-----"+i+"   "+iHighlightBallId[i]);
		 * changeBallState(1, iHighlightBallId[i]); } }
		 */
	}

	public BallHolder randomChooseConfigChange(int aRandomNums,
			BallHolder mBallHolder, int whichGroupBall) {
		clearAllHighlights();
		if (whichGroupBall == 0) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				// PublicMethod.myOutput("-----"+i+"   "+iHighlightBallId[i]);
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				PublicMethod.myOutput("****isHighLight " + isHighLight
						+ "PublicConst.BALL_TO_HIGHLIGHT "
						+ PublicConst.BALL_TO_HIGHLIGHT);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.DanShi.iRedBallStatus[iHighlightBallId[i]] = 1;
					PublicMethod
							.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
									+ iHighlightBallId[i]);
				} else
					mBallHolder.DanShi.iRedBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		if (whichGroupBall == 1) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				// PublicMethod.myOutput("-----"+i+"   "+iHighlightBallId[i]);
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				PublicMethod.myOutput("****isHighLight " + isHighLight
						+ "PublicConst.BALL_TO_HIGHLIGHT "
						+ PublicConst.BALL_TO_HIGHLIGHT);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.DanShi.iBlueBallStatus[iHighlightBallId[i]] = 1;
					PublicMethod
							.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
									+ iHighlightBallId[i]);
				} else
					mBallHolder.DanShi.iBlueBallStatus[iHighlightBallId[i]] = 0;
			}
		}

		return mBallHolder;
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
	public BallHolderFc3d randomChooseConfigChangeFc3d(int aRandomNums,
			BallHolderFc3d mBallHolder, int whichGroupBall, int[] randomNumbers) {
		clearAllHighlights();
		// 直选百位
		if (whichGroupBall == 7) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 直选十位
		if (whichGroupBall == 8) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 直选个位
		if (whichGroupBall == 9) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 组3单式
		if (whichGroupBall == 0) {
			int isHighLight = changeBallState(1, randomNumbers[0]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[randomNumbers[0]] = 1;
			} else
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[randomNumbers[0]] = 0;

		}
		if (whichGroupBall == 1) {
			int isHighLight = changeBallState(1, randomNumbers[0]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[randomNumbers[0]] = 1;
			} else
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[randomNumbers[0]] = 0;

		}
		if (whichGroupBall == 2) {
			int isHighLight = changeBallState(1, randomNumbers[1]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[randomNumbers[1]] = 1;
			} else
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[randomNumbers[1]] = 0;

		}
		// 组3复式
		if (whichGroupBall == 3) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.Zu3BallGroup.iZu3FushiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.Zu3BallGroup.iZu3FushiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 组6
		if (whichGroupBall == 4) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.Zu6BallGroup.iZu6BallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.Zu6BallGroup.iZu6BallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 胆拖胆码
		if (whichGroupBall == 5) {
			for (int i = 0; i < aRandomNums; i++) {
				int isHighLight = changeBallState(aRandomNums, randomNumbers[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.DantuoBallGroup.iDantuoDanmaBallStatus[randomNumbers[i]] = 1;
				} else
					mBallHolder.DantuoBallGroup.iDantuoDanmaBallStatus[randomNumbers[i]] = 0;
			}
		}
		// 胆拖拖码
		if (whichGroupBall == 6) {
			for (int i = randomNumbers.length - aRandomNums; i < randomNumbers.length; i++) {
				int isHighLight = changeBallState(aRandomNums, randomNumbers[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.DantuoBallGroup.iDantuoTuomaBallStatus[randomNumbers[i]] = 1;
				} else
					mBallHolder.DantuoBallGroup.iDantuoTuomaBallStatus[randomNumbers[i]] = 0;
			}
		}
		// 和值直选
		if (whichGroupBall == 10) {
			int isHighLight = changeBallState(aRandomNums, randomNumbers[0]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[randomNumbers[0]] = 1;
			} else
				mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[randomNumbers[0]] = 0;
		}
		// 和值组3
		if (whichGroupBall == 11) {
			int isHighLight = changeBallState(aRandomNums, randomNumbers[0]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.HezhiZu3BallGroup.iHezhiZu3BallStatus[randomNumbers[0]] = 1;
			} else
				mBallHolder.HezhiZu3BallGroup.iHezhiZu3BallStatus[randomNumbers[0]] = 0;
		}
		// 和值组6
		if (whichGroupBall == 12) {
			int isHighLight = changeBallState(aRandomNums, randomNumbers[0]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.HezhiZu6BallGroup.iHezhiZu6BallStatus[randomNumbers[0]] = 1;
			} else
				mBallHolder.HezhiZu6BallGroup.iHezhiZu6BallStatus[randomNumbers[0]] = 0;
		}
		return mBallHolder;
	}

	// wangyl 8.9 排列三
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
	public BallHolderPL3 randomChooseConfigChangePL3(int aRandomNums,
			BallHolderPL3 mBallHolder, int whichGroupBall, int[] randomNumbers) {
		clearAllHighlights();
		// 直选百位
		if (whichGroupBall == 7) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 直选十位
		if (whichGroupBall == 8) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 直选个位
		if (whichGroupBall == 9) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 组3单式
		if (whichGroupBall == 0) {
			int isHighLight = changeBallState(1, randomNumbers[0]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[randomNumbers[0]] = 1;
			} else
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[randomNumbers[0]] = 0;

		}
		if (whichGroupBall == 1) {
			int isHighLight = changeBallState(1, randomNumbers[0]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[randomNumbers[0]] = 1;
			} else
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[randomNumbers[0]] = 0;

		}
		if (whichGroupBall == 2) {
			int isHighLight = changeBallState(1, randomNumbers[1]);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[randomNumbers[1]] = 1;
			} else
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[randomNumbers[1]] = 0;

		}
		// 组3复式
		if (whichGroupBall == 3) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.Zu3BallGroup.iZu3FushiBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.Zu3BallGroup.iZu3FushiBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 组6
		if (whichGroupBall == 4) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, ballViewVector.size() - 1);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.Zu6BallGroup.iZu6BallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.Zu6BallGroup.iZu6BallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 和值直选
		if (whichGroupBall == 10) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, 27);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 和值组3
		if (whichGroupBall == 11) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, 25);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.HezhiZu3BallGroup.iHezhiZu3BallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.HezhiZu3BallGroup.iHezhiZu3BallStatus[iHighlightBallId[i]] = 0;
			}
		}
		// 和值组6
		if (whichGroupBall == 12) {
			int[] iHighlightBallId = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 0, 21);
			for (int i = 0; i < iHighlightBallId.length; i++) {
				int isHighLight = changeBallState(aRandomNums,
						iHighlightBallId[i]);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.HezhiZu6BallGroup.iHezhiZu6BallStatus[iHighlightBallId[i]] = 1;
				} else
					mBallHolder.HezhiZu6BallGroup.iHezhiZu6BallStatus[iHighlightBallId[i]] = 0;
			}
		}
		return mBallHolder;
	}

	/*
	 * // 单式 机选 public void randomChoose(String aBallColor){ int i; // turn off
	 * all ball for(i=0;i<ballViewVector.size();i++){ changeBallState(0,i); }
	 * 
	 * if(aBallColor.equalsIgnoreCase("red")){ // 这里参数为起始小球的id，如26个球，则为0、25
	 * int[] iHighlightBallId=PublicMethod.getRandomsWithoutCollision(6, 0,
	 * ballViewVector.size()-1); for(i=0;i<iHighlightBallId.length;i++){
	 * //PublicMethod.myOutput("-----"+i+"   "+iHighlightBallId[i]);
	 * changeBallState(6, iHighlightBallId[i]); } } else{ // blue int[]
	 * iHighlightBallId=PublicMethod.getRandomsWithoutCollision(1, 0,
	 * ballViewVector.size()-1); for(i=0;i<iHighlightBallId.length;i++){
	 * //PublicMethod.myOutput("-----"+i+"   "+iHighlightBallId[i]);
	 * changeBallState(1, iHighlightBallId[i]); } } }
	 */

	// 设置高亮小球的最大数值
	public void setMaxHighLight(int aNum) {

	}
}