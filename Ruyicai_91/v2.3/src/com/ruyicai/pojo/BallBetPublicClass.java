package com.ruyicai.pojo;

public class BallBetPublicClass {
	public class BallHolder {
		public BallGroup mBallGroup = new BallGroup();
		public BallGroup DanShi = new BallGroup();
		public BallGroup FuShi;
		public BallGroup DanTuo;
		public int topButtonGroup;
		public int flag = 0;
	}

	/**
	 * @author WangYanling 记录每个页面及是否切换横纵屏
	 */
	public class BallHolderFc3d {
		// 福彩3D直选
		public  BallGroupFc3d ZhixuanBallGroup = new BallGroupFc3d();
		// 福彩3D组3
		public BallGroupFc3d Zu3BallGroup = new BallGroupFc3d();
		// 福彩组6
		public  BallGroupFc3d Zu6BallGroup = new BallGroupFc3d();
		// 福彩胆拖
		public BallGroupFc3d DantuoBallGroup = new BallGroupFc3d();
		// 和值
		public BallGroupFc3d HezhiZhixuanBallGroup = new BallGroupFc3d();
		public BallGroupFc3d HezhiZu3BallGroup = new BallGroupFc3d();
		public BallGroupFc3d HezhiZu6BallGroup = new BallGroupFc3d();

		// 顶部标签RadioGroup
		public int topButtonGroup;
		// 是否切换横纵屏 1为是，0为否
		public int flag = 0;

	}

	public class BallGroup {
		// 记录被选中的红色球的在ballTabale中的index
		public int[] iRedBallStatus = new int[35];
		public int[] iBlueBallStatus = new int[16];
		public int[] iFuShiRedBallStatus = new int[35];
		public int[] iFuShiBlueBallStatus = new int[16];
		public int[] iDTDanMaRedBallStatus = new int[35];
		public int[] iTuoRedBallStatus = new int[35];
		public int[] iTuoBlueBallStatus = new int[35];
		public int[] iDTBlueBallStatus = new int[16];
		public int iBeiShu;
		public int iQiShu;
		public boolean bCheckBox;
	}

	/**
	 * @author WangYanling
	 * @category 记录当前页面控件的各个状态以及被选中的小球在ballTable中的index
	 */
	public class BallGroupFc3d {
		// 福彩3D直选
		public int[] iZhixuanBaiweiBallStatus = new int[10];
		public int[] iZhixuanShiweiBallStatus = new int[10];
		public int[] iZhixuanGeweiBallStatus = new int[10];
		// 福彩3D组3
		public int[] iZu3A1BallStatus = new int[10];
		public int[] iZu3A2BallStatus = new int[10];
		public int[] iZu3BBallStatus = new int[10];
		public int[] iZu3FushiBallStatus = new int[10];
		public boolean bRadioBtnDanshi;
		public boolean bRadioBtnFushi;
		// 福彩组6
		public int[] iZu6BallStatus = new int[10];
		// 福彩胆拖
		public int[] iDantuoDanmaBallStatus = new int[10];
		public int[] iDantuoTuomaBallStatus = new int[10];
		// 福彩和值直选
		public int[] iHezhiZhixuanBallStatus = new int[28];
		public int[] iHezhiZu3BallStatus = new int[26];
		public int[] iHezhiZu6BallStatus = new int[22];
		// 倍数
		public int iBeiShu;
		// 期数
		public int iQiShu;
		// 机选复选框
		public boolean bCheckBox;
	}
}
