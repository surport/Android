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
public class PL3Zu3Balls extends Balls {
	public PL3Zu3Balls() {
		init();
	}

	/**
	 * 工厂方法
	 * 
	 * @return
	 */
	public Balls createBalls() {
		return new PL3Zu3Balls();

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		int[] num = PublicMethod.getRandomsWithoutCollision(2, 0, 9);
		int[] baiNum = new int[1];
		int[] shiNum = new int[1];
		int[] geNum = new int[1];
		baiNum[0] = shiNum[0] = num[0];
		geNum[0] = num[1];
		add(baiNum, null);
		add(shiNum, null);
		add(geNum, null);
	}

	String zuxuan = "6|";// 组选单式（包括组三和组六）

	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		String strZM = "";
		strZM += zuxuan;
		int bai = getVZhuma().get(0)[0];
		int shi = getVZhuma().get(1)[0];
		int ge = getVZhuma().get(2)[0];
		strZM += ge + "," + shi + "," + bai;
		return strZM;
	}
}
