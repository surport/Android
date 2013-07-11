package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.util.PublicMethod;

public class QXCJXBalls extends Balls {

	public QXCJXBalls() {
		init();
	}

	@Override
	public Balls createBalls() {
		// TODO Auto-generated method stub
		return new QXCJXBalls();
	}

	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		String strZM = "";

		int first = getVZhuma().get(0)[0];
		int second = getVZhuma().get(1)[0];
		int third = getVZhuma().get(2)[0];
		int fourth = getVZhuma().get(3)[0];
		int fifth = getVZhuma().get(4)[0];
		int sixth = getVZhuma().get(5)[0];
		int seventh = getVZhuma().get(6)[0];

		strZM += first + "," + second + "," + third + "," + fourth + ","
				+ fifth + "," + sixth + "," + seventh;
		return strZM;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		int[] firstNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] secondNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] thirdNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] fourthNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] fifthNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] sixthNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] seventhNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		add(firstNum, null);
		add(secondNum, null);
		add(thirdNum, null);
		add(fourthNum, null);
		add(fifthNum, null);
		add(sixthNum, null);
		add(seventhNum, null);
	}
}
