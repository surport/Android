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
public class GdelevenRxBalls extends Balls {
	int i = 0;

	public GdelevenRxBalls(int i) {
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
			str += "S|R1|";
			str += getstrjixuan(balls, ";S|R1|");
		} else if (type.equals("R2")) {
			str += "S|R2|";
			str += getstrjixuan(balls, ";S|R2|");
		} else if (type.equals("R3")) {
			str += "S|R3|";
			str += getstrjixuan(balls, ";S|R3|");
		} else if (type.equals("R4")) {
			str += "S|R4|";
			str += getstrjixuan(balls, ";S|R4|");
		} else if (type.equals("R5")) {
			str += "S|R5|";
			str += getstrjixuan(balls, ";S|R5|");
		} else if (type.equals("R6")) {
			str += "S|R6|";
			str += getstrjixuan(balls, ";S|R6|");
		} else if (type.equals("R7")) {
			str += "S|R7|";
			str += getstrjixuan(balls, ";S|R7|");
		} else if (type.equals("R8")) {
			str += "S|R8|";
			str += getstrjixuan(balls, ";S|R8|");
		} else if (type.equals("Q2")) {
			str += "S|Q2|";
			str += getstrjixuan(balls, ";S|Q2|");
		} else if (type.equals("Q3")) {
			str += "S|Q3|";
			str += getstrjixuan(balls, ";S|Q3|");
		} else if (type.equals("Z2")) {
			str += "S|Z2|";
			str += getstrjixuan(balls, ";S|Z2|");
		} else if (type.equals("Z3")) {
			str += "S|Z3|";
			str += getstrjixuan(balls, ";S|Z3|");
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