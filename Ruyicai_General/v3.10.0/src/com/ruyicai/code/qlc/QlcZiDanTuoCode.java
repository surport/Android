/**
 * 
 */
package com.ruyicai.code.qlc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 * 
 */
public class QlcZiDanTuoCode extends CodeInterface {

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		int[] zhuma = areaNums[0].table.getHighlightBallNOs();
		int[] zhumablue = areaNums[1].table.getHighlightBallNOs();
		int danBallNums = areaNums[0].table.getHighlightBallNums();
		int tuoBallNums = areaNums[1].table.getHighlightBallNums();
		long sendzhushu = getQLCDTZhuShu(danBallNums, tuoBallNums);
		String t_str = "";
		t_str += "20";
		t_str += "01";

		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}
		t_str += "*";
		for (int i = 0; i < zhumablue.length; i++) {
			if (zhumablue[i] >= 10) {
				t_str += zhumablue[i];
			} else if (zhumablue[i] < 10) {
				t_str += "0" + zhumablue[i];
			}
		}
		t_str += "^";
		return t_str;

	}

	/*
	 * 胆拖玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @param int aRedTuoBalls 红球拖码个数
	 * 
	 * @param int aBlueBalls 篮球个数
	 * 
	 * @return long 注数
	 */
	private long getQLCDTZhuShu(int aRedBalls, int aRedTuoBalls) {// 得到双色球胆拖的注数
		long qlcZhuShu = 0L;
		if (aRedBalls > 0 && aRedTuoBalls > 0) {
			qlcZhuShu += (PublicMethod.zuhe(7 - aRedBalls, aRedTuoBalls));
		}
		return qlcZhuShu;
	}

}
