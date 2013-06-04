package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.util.PublicMethod;

public class QXCJXBalls extends Balls{
	
	public QXCJXBalls(){
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
		
		for (int i = 0; i < balls.size(); i++) {
			int first = balls.get(i).getVZhuma().get(0)[0] +1;
			int second = balls.get(i).getVZhuma().get(1)[0] + 1;
			int  third = balls.get(i).getVZhuma().get(2)[0] + 1;
			int fourth = balls.get(i).getVZhuma().get(3)[0] + 1;
			int fifth = balls.get(i).getVZhuma().get(4)[0] + 1;
			int sixth = balls.get(i).getVZhuma().get(5)[0] + 1;
			int seventh = balls.get(i).getVZhuma().get(6)[0] + 1;

			strZM += first + "," + second + "," + third + "," + fourth + "," + fifth +","+ sixth +","+seventh;
			if (i != balls.size() - 1) {
				strZM += ";";
			}
		}
		return strZM;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		int[] firstNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] secondNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] thirdNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] fourthNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] fifthNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] sixthNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] seventhNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		add(firstNum,null);
		add(secondNum,null);
		add(thirdNum,null);
		add(fourthNum,null);
		add(fifthNum,null);
		add(sixthNum,null);
		add(seventhNum,null);
	}
}
