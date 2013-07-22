/**
 * 
 */
package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 * 
 */
public class SsqDSBalls extends Balls {

	public SsqDSBalls() {
		init();
	}

	/**
	 * 工厂方法
	 * 
	 * @return
	 */
	public Balls createBalls() {
		return new SsqDSBalls();

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		int redNum[] = PublicMethod.getRandomsWithoutCollision(6, 1, 33);
		redNum = PublicMethod.orderby(redNum, "abc");
		int blueNum[] = PublicMethod.getRandomsWithoutCollision(1, 1, 16);
		add(redNum, null);
		add(blueNum, "blue");
	}

	/**
	 * 投注注码
	 * 
	 * @param balls
	 * @param beishu
	 */
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		int zhumablue[] = null;
		int zhuma[] = null;
		// String t_str = "1512-F47104-";
		// t_str += "00-";
		// int zhushu = balls.size();
		// if (zhushu < 10) {
		// t_str += "0"+zhushu;
		// }
		// if (zhushu >= 10) {
		// t_str += zhushu;
		// }
		// t_str+="-";
		String t_str = "";
		// for (int j = 0; j < balls.size(); j++) {
		t_str += "00";
		if (beishu < 10) {
			t_str += "0" + beishu;
		} else {
			t_str += "" + beishu;
		}
		zhuma = getVZhuma().get(0);
		zhumablue = getVZhuma().get(1);
		for (int i = 0; i < zhuma.length; i++) {
			int zhuRed = zhuma[i];
			if (zhuRed >= 10) {
				t_str += zhuRed;
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuRed;
			}
		}
		int zhuBlue = zhumablue[0];
		if (zhuBlue >= 10) {
			t_str += "~" + zhuBlue;
		} else if (zhumablue[0] < 10) {
			t_str += "~0" + zhuBlue;
		}
		t_str += "^";
		// }
		return t_str;
	}

}
