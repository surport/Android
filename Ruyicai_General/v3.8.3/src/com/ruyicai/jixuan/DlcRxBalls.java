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
public class DlcRxBalls extends Balls {
	int i = 0;

	public DlcRxBalls(int i) {
		this.i = i;
		init();
	}

	@Override
	public Balls createBalls() {
		// TODO Auto-generated method stub
		return new DlcRxBalls(i);
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
			str += "R1|";
			str += getstrjixuan(balls, ";R1|");
		} else if (type.equals("R2")) {
			str += "R2|";
			str += getstrjixuan(balls, ";R2|");
		} else if (type.equals("R3")) {
			str += "R3|";
			str += getstrjixuan(balls, ";R3|");
		} else if (type.equals("R4")) {
			str += "R4|";
			str += getstrjixuan(balls, ";R4|");
		} else if (type.equals("R5")) {
			str += "R5|";
			str += getstrjixuan(balls, ";R5|");
		} else if (type.equals("R6")) {
			str += "R6|";
			str += getstrjixuan(balls, ";R6|");
		} else if (type.equals("R7")) {
			str += "R7|";
			str += getstrjixuan(balls, ";R7|");
		} else if (type.equals("R8")) {
			str += "R8|";
			str += getstrjixuan(balls, ";R8|");
		} else if (type.equals("Q2")) {
			str += "Q2|";
			str += DlcRxBalls.getstrjixuan(balls, ";Q2|");
		} else if (type.equals("Q3")) {
			str += "Q3|";
			str += DlcRxBalls.getstrjixuan(balls, ";Q3|");
		} else if (type.equals("Z2")) {
			str += "Z2|";
			str += DlcRxBalls.getstrjixuan(balls, ";Z2|");
		} else if (type.equals("Z3")) {
			str += "Z3|";
			str += DlcRxBalls.getstrjixuan(balls, ";Z3|");
		}
		return str;

	}

	public static String getstrjixuan(Balls balls, String typestr) {
		String str = "";
		int ge[] = balls.getVZhuma().get(0);
		int j;
		for (j = 0; j < ge.length; j++) {
			if (j != ge.length - 1) {
				if (ge[j] < 10) {
					str += "0" + ge[j];
				} else {
					str += ge[j];
				}
				if (typestr.equals(";Q2|") || typestr.equals(";Q3|")) {
					str += ",";
				} else {
					str += " ";
				}
			} else {
				if (ge[j] < 10) {
					str += "0" + ge[j];
				} else {
					str += ge[j];
				}
			}
		}
		return str;
	}

}