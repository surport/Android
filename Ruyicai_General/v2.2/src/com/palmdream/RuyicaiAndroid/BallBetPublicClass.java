package com.palmdream.RuyicaiAndroid;

public class BallBetPublicClass {
	public class BallHolder {
		BallGroup mBallGroup = new BallGroup();
		BallGroup DanShi = new BallGroup();
		BallGroup FuShi;
		BallGroup DanTuo;
		int topButtonGroup;
		int flag = 0;
	}

	/**
	 * @author WangYanling 记录每个页面及是否切换横纵屏
	 */
	public class BallHolderFc3d {
		// 福彩3D直选
		BallGroupFc3d ZhixuanBallGroup = new BallGroupFc3d();
		// 福彩3D组3
		BallGroupFc3d Zu3BallGroup = new BallGroupFc3d();
		// 福彩组6
		BallGroupFc3d Zu6BallGroup = new BallGroupFc3d();
		// 福彩胆拖
		BallGroupFc3d DantuoBallGroup = new BallGroupFc3d();
		// 和值
		BallGroupFc3d HezhiZhixuanBallGroup = new BallGroupFc3d();
		BallGroupFc3d HezhiZu3BallGroup = new BallGroupFc3d();
		BallGroupFc3d HezhiZu6BallGroup = new BallGroupFc3d();

		// 顶部标签RadioGroup
		int topButtonGroup;
		// 是否切换横纵屏 1为是，0为否
		int flag = 0;

	}

	public class BallGroup {
		// 记录被选中的红色球的在ballTabale中的index
		int[] iRedBallStatus = new int[35];
		int[] iBlueBallStatus = new int[16];
		int[] iFuShiRedBallStatus = new int[35];
		int[] iFuShiBlueBallStatus = new int[16];
		int[] iDTDanMaRedBallStatus = new int[35];
		int[] iTuoRedBallStatus = new int[35];
		int[] iTuoBlueBallStatus = new int[35];
		int[] iDTBlueBallStatus = new int[16];
		int iBeiShu;
		int iQiShu;
		boolean bCheckBox;
	}

	/**
	 * @author WangYanling
	 * @category 记录当前页面控件的各个状态以及被选中的小球在ballTable中的index
	 */
	public class BallGroupFc3d {
		// 福彩3D直选
		int[] iZhixuanBaiweiBallStatus = new int[10];
		int[] iZhixuanShiweiBallStatus = new int[10];
		int[] iZhixuanGeweiBallStatus = new int[10];
		// 福彩3D组3
		int[] iZu3A1BallStatus = new int[10];
		int[] iZu3A2BallStatus = new int[10];
		int[] iZu3BBallStatus = new int[10];
		int[] iZu3FushiBallStatus = new int[10];
		boolean bRadioBtnDanshi;
		boolean bRadioBtnFushi;
		// 福彩组6
		int[] iZu6BallStatus = new int[10];
		// 福彩胆拖
		int[] iDantuoDanmaBallStatus = new int[10];
		int[] iDantuoTuomaBallStatus = new int[10];
		// 福彩和值直选
		int[] iHezhiZhixuanBallStatus = new int[28];
		int[] iHezhiZu3BallStatus = new int[26];
		int[] iHezhiZu6BallStatus = new int[22];
		// 倍数
		int iBeiShu;
		// 期数
		int iQiShu;
		// 机选复选框
		boolean bCheckBox;
	}
}
