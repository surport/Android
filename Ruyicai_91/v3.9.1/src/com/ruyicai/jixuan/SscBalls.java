package com.ruyicai.jixuan;

import java.util.Vector;

import android.util.Log;

import com.ruyicai.util.PublicMethod;

public class SscBalls extends Balls {
	public boolean isbigsmall = false;
	int i = 0;

	public SscBalls(int i) {
		this.i = i;
		init();
	}

	public SscBalls() {
		this.i = 2;
		isbigsmall = true;
		init();
	}

	@Override
	public Balls createBalls() {
		// TODO Auto-generated method stub
		if (isbigsmall) {
			return new SscBalls();
		} else {
			return new SscBalls(i);
		}
	}

	@Override
	public void init() {
		if (isbigsmall) {
			int onebig[] = new int[1];
			// TODO Auto-generated method stub
			for (int j = 0; j < 2; j++) {
				onebig = PublicMethod.getRandomsWithoutCollision(1, 0, 3);
				add(onebig, null);
			}
		} else {
			int onenum[] = new int[1];
			// TODO Auto-generated method stub
			for (int j = 0; j < i; j++) {
				onenum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
				add(onenum, null);
			}
		}
	}

	public String getZhuma(int index) {
		int num[] = getBalls(index);
		String str = "";
		for (int i = 0; i < num.length; i++) {
			if (i != num.length - 1)
				str += PublicMethod.getZhuMa(num[i]) + ",";
			else
				str += PublicMethod.getZhuMa(num[i]);
		}
		return str;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.jixuan.Balls#getZhuma(java.util.Vector, int)
	 */
	public String getZhuma(Vector<Balls> balls, int beishu) {
		String str = "";
		switch (beishu) {
		case 1:
			str += getstrjixuanone(balls);
			break;
		case 2:
			str += getstrjixuantwo(balls);
			break;
		case 3:
			str += getstrjixuanthree(balls);
			break;
		case 4:
			str += getstrjixuanfive(balls);
			break;
		case 5:
			str += getStrjixuanbigsmall(balls);
		}

		return str;
	}

	public int[] getnum(Vector<int[]> nums) {
		int num[] = new int[nums.size()];
		for (int i = 0; i < nums.size(); i++) {
			num[i] = nums.get(i)[0];
		}
		return num;
	}

	public String getstrjixuanone(Vector<Balls> balls) {
		String str = "1D|-," + "-," + "-," + "-";

		int ge[] = getnum(getVZhuma());
		for (int j = 0; j < ge.length; j++) {
			str += "," + ge[j];
		}

		return str;

	}

	public String getstrjixuantwo(Vector<Balls> balls) {
		String str = "2D|-," + "-," + "-";
		int ge[] = getnum(getVZhuma());
		for (int j = 0; j < ge.length; j++) {
			str += "," + ge[j];
		}
		return str;

	}

	public String getstrjixuanthree(Vector<Balls> balls) {
		String str = "3D|-," + "-";

		int ge[] = getnum(getVZhuma());
		for (int j = 0; j < ge.length; j++) {
			str += "," + ge[j];
		}
		return str;
	}

	public String getstrjixuanfive(Vector<Balls> balls) {
		String str = "5D|";

		int ge[] = getnum(getVZhuma());
		int j;
		for (j = 0; j < ge.length; j++) {
			if (j != ge.length - 1) {
				str += ge[j] + ",";
			} else {
				str += ge[j];
			}
		}

		return str;
	}

	public String getStrjixuanbigsmall(Vector<Balls> balls) {
		String str = "DD|";
		int ball[] = getnum(getVZhuma());
		for (int j = 0; j < ball.length; j++) {
			switch (ball[j] + 1) {
			case 1:
				str += "2";// 大
				break;
			case 2:
				str += "1";// 小
				break;
			case 3:
				str += "5";// 单
				break;
			case 4:
				str += "4";// 双
				break;
			}
		}
		return str;
	}
}