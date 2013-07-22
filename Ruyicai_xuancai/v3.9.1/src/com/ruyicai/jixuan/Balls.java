/**
 * 
 */
package com.ruyicai.jixuan;

import java.util.Vector;

import android.util.Log;

import com.ruyicai.util.PublicMethod;

/**
 * 机选小球的抽象类
 * 
 * @author Administrator
 * 
 */
public abstract class Balls {

	Vector num = new Vector<int[]>();// 注码数组
	Vector colors = new Vector<String>();// 颜色数组默认为红色

	/**
	 * 赋值
	 * 
	 */
	public abstract void init();

	/**
	 * 工厂方法
	 * 
	 * @return
	 */
	public abstract Balls createBalls();

	/**
	 * 获得投注注码
	 * 
	 * @param balls
	 * @param beishu
	 * @return
	 */
	public abstract String getZhuma(Vector<Balls> balls, int beishu);

	/**
	 * 返回数组
	 * 
	 * @param index
	 * @return
	 */
	public int[] getBalls(int index) {
		return (int[]) num.get(index);

	}

	/**
	 * 返回向量数组
	 * 
	 * @return
	 */
	public Vector<int[]> getVZhuma() {
		return num;
	}

	/**
	 * 返回向量数组
	 * 
	 * @return
	 */
	public Vector<String> getVColor() {
		return colors;
	}

	public void add(int[] num, String color) {
		this.num.add(num);
		if (color == null) {
			this.colors.add("red");
		} else {
			this.colors.add(color);
		}

	}

	/**
	 * 注码格式，加零的,用于大乐透、双色球、七乐彩机选
	 */
	public String getZhuma(int index) {
		int num[] = getBalls(index);
		String str = "";
		for (int i = 0; i < num.length; i++) {
			if (i != num.length - 1)
				str += PublicMethod.getZhuMa(num[i] + 1) + ",";
			else
				str += PublicMethod.getZhuMa(num[i] + 1);
		}
		return str;

	}

	/**
	 * 投注提示的注码格式，加零的
	 */
	public String getTenShowZhuma(int index) {
		int num[] = getBalls(index);
		String str = "";
		for (int i = 0; i < num.length; i++) {
			str += PublicMethod.isTen(num[i]);
			if (i != num.length - 1)
				str += ",";
		}
		return str;
	}

	/**
	 * 投注提示的注码格式，不需加零的
	 */
	public String getShowZhuma(int index) {
		int num[] = getBalls(index);
		String str = "";
		for (int i = 0; i < num.length; i++) {
			str += (num[i]) + "";
			if (i != num.length - 1)
				str += ",";
		}
		return str;
	}

	/**
	 * 投注提示的注码格式，不需加零的,用于时时彩机选
	 */
	public String getSpecialShowZhuma(int index) {
		int num[] = getBalls(index);
		String str = "";
		for (int i = 0; i < num.length; i++) {
			str += num[i];
			if (i != num.length - 1)
				str += ",";
		}
		return str;
	}

	/**
	 * 投注提示的注码格式，加零的,用于11-5机选彩机选
	 */
	public String getTenSpecialShowZhuma(int index) {
		int num[] = getBalls(index);
		String str = "";
		for (int i = 0; i < num.length; i++) {
			str += PublicMethod.getZhuMa(num[i]);
			if (i != num.length - 1)
				str += ",";
		}
		return str;
	}

}
