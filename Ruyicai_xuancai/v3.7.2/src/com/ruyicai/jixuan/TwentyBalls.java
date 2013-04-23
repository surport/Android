package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.util.PublicMethod;

public class TwentyBalls extends Balls {

	public TwentyBalls() {
		// TODO Auto-generated constructor stub
		init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		int redNum[] = PublicMethod.getRandomsWithoutCollision(5, 1, 22);
		redNum = PublicMethod.orderby(redNum, "abc");
		add(redNum, null);
	}

	@Override
	public Balls createBalls() {

		return new TwentyBalls();
	}

	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		StringBuffer t_str = new StringBuffer();
		int zhuma[] = null;
		t_str.append("0@");
		zhuma = getVZhuma().get(0);
		for (int i = 0; i < zhuma.length; i++) {
			int zhuRed = zhuma[i];
			if (zhuRed >= 10) {
				t_str.append(zhuRed);
			} else if (zhuma[i] < 10) {
				t_str.append("0" + zhuRed);
			}
		}
		t_str.append("^");
		return t_str.toString();
	}

}
