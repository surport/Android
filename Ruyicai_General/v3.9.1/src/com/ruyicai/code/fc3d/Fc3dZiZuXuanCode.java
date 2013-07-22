/**
 * 
 */
package com.ruyicai.code.fc3d;

import com.ruyicai.activity.buy.fc3d.FC3DZuLiu;
import com.ruyicai.activity.buy.fc3d.FC3DZuSan;
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 * 
 */
public class Fc3dZiZuXuanCode extends CodeInterface {
	String CityCode = "1512-";// 城市编号
	String DDD_falg = "F47103-";// 彩种编号
	String typeCode = "";// 玩法
	String staticCode = "-01-";
	String endCode = "^";// 结束标志
	String dateCode = "0";// 数据
	String sendCode = "";
	String z3fs = "31";// 组3复式
	String z6fs = "32";// 组6复式
	String z3ds = "01";// 组3
	String z6ds = "02";// 组6
	private int iCurrentButton;

	public int getiCurrentButton() {
		return iCurrentButton;
	}

	public void setiCurrentButton(int iCurrentButton) {
		this.iCurrentButton = iCurrentButton;
	}

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {

		// TODO Auto-generated method stub
		String[] zhuma = null;
		String t_str = "";
		int iZhuShu = getZhuShu(areaNums);
		if (iCurrentButton == PublicConst.BUY_FC3D_ZU3_DAN) {
			typeCode = z3ds;
			int[] zhuma_zu3danshi_A1 = areaNums[0].table.getHighlightBallNOs();
			int[] zhuma_zu3danshi_A2 = areaNums[1].table.getHighlightBallNOs();
			// 需要排序
			if (zhuma_zu3danshi_A1[0] < zhuma_zu3danshi_A2[0]) {
				String[] str = { "0" + (zhuma_zu3danshi_A1[0]),
						"0" + (zhuma_zu3danshi_A1[0]),
						"0" + (zhuma_zu3danshi_A2[0]) };
				zhuma = str;
			} else {
				String[] str = { "0" + (zhuma_zu3danshi_A2[0]),
						"0" + (zhuma_zu3danshi_A1[0]),
						"0" + (zhuma_zu3danshi_A1[0]) };
				zhuma = str;
			}
		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU3_FU) {

			typeCode = z3fs;
			int[] zhuma_zu3fushi = areaNums[0].table.getHighlightBallNOs();
			// 之前组3投注码格式有误 陈晨20100712
			int highlightballnum = areaNums[0].table.getHighlightBallNums();
			String[] str = new String[highlightballnum + 1];

			if (highlightballnum < 10) {
				str[0] = "0" + highlightballnum;
			} else {
				str[0] = "" + highlightballnum;
			}
			for (int i = 0; i < zhuma_zu3fushi.length; i++) {
				str[i + 1] = "0" + (zhuma_zu3fushi[i]);
			}
			zhuma = str;

		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {
			int num = areaNums[0].table.getHighlightBallNums();
			int[] zhuma_zu6danfushi = areaNums[0].table.getHighlightBallNOs();
			String[] str = new String[zhuma_zu6danfushi.length + 1];
			if (num < 4) {
				typeCode = z6ds;
				for (int i = 0; i < zhuma_zu6danfushi.length; i++) {
					str[0] = "";
					str[i + 1] = "0" + (zhuma_zu6danfushi[i]);
				}
			} else if (num > 3) {
				typeCode = z6fs;
				int highlightballnum = areaNums[0].table.getHighlightBallNums();
				if (highlightballnum < 10) {
					str[0] = "0" + highlightballnum;
				} else {
					str[0] = "" + highlightballnum;
				}
				for (int i = 0; i < zhuma_zu6danfushi.length; i++) {
					str[i + 1] = "0" + (zhuma_zu6danfushi[i]);
				}

			}

			zhuma = str;
		}
		String zhushu = "";
		if (iZhuShu < 10) {
			zhushu += "0" + iZhuShu;
		} else if (iZhuShu >= 10) {
			zhushu += "" + iZhuShu;
		}
		String beishu_ = "01";
		// t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-" +
		// typeCode + beishu_;
		t_str += typeCode + beishu_;
		for (int i = 0; i < zhuma.length; i++) {
			t_str += zhuma[i];
		}
		t_str += endCode;

		return t_str;
	}

	public int getZhuShu(AreaNum[] areaNums) {
		int iReturnValue = 0;
		switch (Fc3d.iCurrentButton) {
		// 福彩3D组3复式
		case PublicConst.BUY_FC3D_ZU3_DAN:
			iReturnValue = 1;
			break;
		case PublicConst.BUY_FC3D_ZU3_FU:
			int iZu3Balls = areaNums[0].table.getHighlightBallNums();
			iReturnValue = (int) getFc3dZu3FushiZhushu(iZu3Balls);
			break;
		// 福彩3D组6复式
		case PublicConst.BUY_FC3D_ZU6:
			int iZu6Balls = areaNums[0].table.getHighlightBallNums();
			iReturnValue = (int) getFc3dZu6FushiZhushu(iZu6Balls);
			break;
		}
		return iReturnValue;
	}

	/**
	 * 获得福彩3D组6复式注数
	 * 
	 * @param iZu6balls
	 *            选择小球个数
	 * @return 返回注数
	 */
	private long getFc3dZu6FushiZhushu(int iZu6balls) {
		long tempzhushu = 0l;
		if (iZu6balls > 0) {
			tempzhushu += PublicMethod.zuhe(3, iZu6balls);
		}
		return tempzhushu;

	}

	/**
	 * 获得福彩3D组3复式注数
	 * 
	 * @param iZu3balls
	 *            选择小球个数
	 * @return 返回注数
	 */
	private long getFc3dZu3FushiZhushu(int iZu3balls) {
		long tempzhushu = 0l;
		if (iZu3balls > 0) {
			tempzhushu += PublicMethod.zuhe(2, iZu3balls) * 2;
		}
		return tempzhushu;

	}

}
