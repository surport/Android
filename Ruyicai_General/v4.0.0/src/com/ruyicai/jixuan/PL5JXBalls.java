package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.util.PublicMethod;

public class PL5JXBalls extends Balls {

	public PL5JXBalls() {
		init();
	}

	@Override
	public Balls createBalls() {
		// TODO Auto-generated method stub
		return new PL5JXBalls();
	}

	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		String strZM = "";

		int wan = getVZhuma().get(0)[0];
		int qian = getVZhuma().get(1)[0];
		int bai = getVZhuma().get(2)[0];
		int shi = getVZhuma().get(3)[0];
		int ge = getVZhuma().get(4)[0];
		strZM += wan + "," + qian + "," + bai + "," + shi + "," + ge;
		return strZM;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		int[] wanNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] qianNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] baiNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] shiNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] geNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		add(wanNum, null);
		add(qianNum, null);
		add(baiNum, null);
		add(shiNum, null);
		add(geNum, null);
	}
}
