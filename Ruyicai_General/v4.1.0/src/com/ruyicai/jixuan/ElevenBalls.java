/**
 * 
 */
package com.ruyicai.jixuan;

import java.util.Vector;

import android.util.Log;

import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 * 
 */
public class ElevenBalls extends Balls {
	public final static String R1 = "101@*";
	public final static String R2 = "111@";
	public final static String R3 = "112@";
	public final static String R4 = "113@";
	public final static String R5 = "114@";
	public final static String R6 = "115@";
	public final static String R7 = "116@";
	public final static String R8 = "117@";
	public final static String Q2 = "141@";
	public final static String Q3 = "161@";
	public final static String Z2 = "131@";
	public final static String Z3 = "151@";
	int i = 0;

	public ElevenBalls(int i) {
		this.i = i;
		init();
	}

	@Override
	public Balls createBalls() {
		// TODO Auto-generated method stub
		return new ElevenBalls(i);
	}

	@Override
	public void init() {
		int onenum[] = new int[i];
		// TODO Auto-generated method stub
		for (int j = 0; j < i; j++) {
			onenum = PublicMethod.getRandomsWithoutCollision(i, 1, 11);
		}
		onenum = PublicMethod.orderby(onenum, "abc");
		add(onenum, null);
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
	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getZhuma(Balls balls, String type) {
		String str = "";
		if (type.equals("R1")) {
			str += getstrjixuan(balls, R1);
		} else if (type.equals("R2")) {
			str += getstrjixuan(balls, R2);
		} else if (type.equals("R3")) {
			str += getstrjixuan(balls, R3);
		} else if (type.equals("R4")) {
			str += getstrjixuan(balls, R4);
		} else if (type.equals("R5")) {
			str += getstrjixuan(balls, R5);
		} else if (type.equals("R6")) {
			str += getstrjixuan(balls, R6);
		} else if (type.equals("R7")) {
			str += getstrjixuan(balls, R7);
		} else if (type.equals("R8")) {
			str += getstrjixuan(balls, R8);
		} else if (type.equals("Q2")) {
			str += getstrjixuan(balls, Q2);
		} else if (type.equals("Q3")) {
			str += getstrjixuan(balls, Q3);
		} else if (type.equals("Z2")) {
			str += getstrjixuan(balls, Z2);
		} else if (type.equals("Z3")) {
			str += getstrjixuan(balls, Z3);
		}
		return str;

	}

	public static String getstrjixuan(Balls balls, String type) {
		String str = type;
		int ge[] = balls.getVZhuma().get(0);
		for (int j = 0; j < ge.length; j++) {
			if (ge[j] < 10) {
				str += "0" + ge[j];
			} else {
				str += ge[j];
			}
		}
		str += "^";
		return str;
	}

}