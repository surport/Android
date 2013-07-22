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
public class PL3ZhiXuanBalls extends Balls {
	public PL3ZhiXuanBalls() {
		init();
	}

	/**
	 * 工厂方法
	 * 
	 * @return
	 */
	public Balls createBalls() {
		return new PL3ZhiXuanBalls();

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		int[] baiNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] shiNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] geNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		add(baiNum, null);
		add(shiNum, null);
		add(geNum, null);
	}

	String zhixuan = "1|";// 直选

	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		String strZM = "";

		strZM += zhixuan;
		int bai = getVZhuma().get(0)[0];
		int shi = getVZhuma().get(1)[0];
		int ge = getVZhuma().get(2)[0];
		strZM += bai + "," + shi + "," + ge;
		return strZM;
	}

}
