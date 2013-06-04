package com.ruyicai.jixuan;

import java.util.Vector;

import com.ruyicai.util.PublicMethod;

public class PL5JXBalls extends Balls{
	
	public PL5JXBalls(){
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
		
		for (int i = 0; i < balls.size(); i++) {
			int wan = balls.get(i).getVZhuma().get(0)[0] +1;
			int qian = balls.get(i).getVZhuma().get(1)[0] + 1;
			int bai = balls.get(i).getVZhuma().get(2)[0] + 1;
			int shi = balls.get(i).getVZhuma().get(3)[0] + 1;
			int ge = balls.get(i).getVZhuma().get(4)[0] + 1;
			strZM += wan + "," + qian + "," + bai + "," + shi + "," + ge;
			if (i != balls.size() - 1) {
				strZM += ";";
			}
		}
		return strZM;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		int[] wanNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] qianNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] baiNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] shiNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		int[] geNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
		add(wanNum,null);
		add(qianNum,null);
		add(baiNum,null);
		add(shiNum,null);
		add(geNum,null);
	}
}
