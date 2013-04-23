/**
 * 
 */
package com.ruyicai.code.fc3d;

import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicConst;

/**
 * @author Administrator
 * 
 */
public class F3dZiHeZhiCode extends CodeInterface {
	String CityCode = "1512-";// 城市编号
	String DDD_falg = "F47103-";// 彩种编号
	String typeCode = "";// 玩法
	String staticCode = "-01-";
	String endCode = "^";// 结束标志
	String dateCode = "0";// 数据

	String zxHHH = "10";// 直和值
	String z3HHH = "11";// 组3值
	String z6HHH = "12";// 组6值
	private int iCurrentButton;

	public int getiCurrentButton() {
		return iCurrentButton;
	}

	public void setiCurrentButton(int iCurrentButton) {
		this.iCurrentButton = iCurrentButton;
	}

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {

		String[] zhuma = null;
		String t_str = "";
		int iZhuShu = 0;
		// 判断是和值直选 还是和值组3 还是和值组6
		if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI_ZHIXUAN) {
			typeCode = zxHHH;
			iZhuShu = getFc3dZhixuanHezhiZhushu(areaNums[0].table);
			// 判断是一位数还是两位 陈晨 20100714
			int[] zhuma_zhixuanhezhi = areaNums[0].table.getHighlightBallNOs();
			if (zhuma_zhixuanhezhi[0] < 10) {
				String[] str = { "0" + (zhuma_zhixuanhezhi[0]) };
				zhuma = str;
			} else {
				String[] str = { "" + (zhuma_zhixuanhezhi[0]) };

				zhuma = str;
			}
		} else if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI_ZU3) {
			typeCode = z3HHH;
			iZhuShu = getFc3dZu3HezhiZhushu(areaNums[0].table);
			int[] zhuma_zu3hezhi = areaNums[0].table.getHighlightBallNOs();
			// 判断是一位数还是两位 陈晨 20100714
			if (zhuma_zu3hezhi[0] < 10) {
				String[] str = { "0" + zhuma_zu3hezhi[0] };
				zhuma = str;
			} else {
				String[] str = { "" + zhuma_zu3hezhi[0] };
				zhuma = str;
			}
		} else if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI_ZU6) {
			typeCode = z6HHH;
			iZhuShu = getFc3dZu6HezhiZhushu(areaNums[0].table);
			int[] zhuma_zu6hezhi = areaNums[0].table.getHighlightBallNOs();
			// 判断是一位数还是两位 陈晨 20100714
			if (zhuma_zu6hezhi[0] < 10) {
				String[] str = { "0" + (zhuma_zu6hezhi[0]) };
				zhuma = str;
			} else {
				String[] str = { "" + (zhuma_zu6hezhi[0]) };
				zhuma = str;
			}
		}
		// 加上注数 2010/7/11/ 陈晨
		String zhushu = "";
		if (iZhuShu < 10) {
			zhushu += "0" + iZhuShu;
		} else if (iZhuShu >= 10) {
			zhushu += "" + iZhuShu;
		}
		String beishu_ = "01";
		// if (beishu < 10) {
		// beishu_ += "0" + beishu;
		// } else if (beishu >= 10) {
		// beishu_ += "" + beishu;
		// }
		t_str += typeCode + beishu_;
		for (int i = 0; i < zhuma.length; i++) {
			t_str += zhuma[i];
		}
		t_str += endCode;
		return t_str;
	}

	/**
	 * 获得福彩3D直选和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZhixuanHezhiZhushu(BallTable table) {
		int iZhuShu = 0;
		int[] BallNos = table.getHighlightBallNOs();// 被选择小球的号码（点击1，获得1）
		int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69,
				73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 };// 0~27

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i]) {// 因为数组是从0开始的，小球号码从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值直选，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得福彩3D组3和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZu3HezhiZhushu(BallTable table) {
		int iZhuShu = 0;
		int[] BallNos = table.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5, 5, 4, 5,
				5, 4, 5, 4, 3, 3, 3, 1, 2, 1 };// 1~26

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值组3，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得福彩3D组6和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZu6HezhiZhushu(BallTable table) {
		int iZhuShu = 0;
		int[] BallNos = table.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9,
				8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 3) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值组6，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

}
