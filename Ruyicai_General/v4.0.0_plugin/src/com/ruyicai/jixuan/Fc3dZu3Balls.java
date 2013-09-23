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
public class Fc3dZu3Balls extends Balls {

	public Fc3dZu3Balls() {
		init();
	}

	/**
	 * 工厂方法
	 * 
	 * @return
	 */
	public Balls createBalls() {
		return new Fc3dZu3Balls();

	}

	String CityCode = "1512-";// 城市编号
	String DDD_falg = "F47103-";// 彩种编号
	String typeCode = "";// 玩法
	String staticCode = "-01-";
	String endCode = "^";// 结束标志
	String dateCode = "0";// 数据
	String sendCode = "";
	String z3ds = "01";// 组3

	@Override
	public void init() {
		// TODO Auto-generated method stub

		int[] num = PublicMethod.getRandomsWithoutCollision(2, 0, 9);
		int[] baiNum = new int[1];
		int[] shiNum = new int[1];
		int[] geNum = new int[1];
		baiNum[0] = shiNum[0] = num[0];
		geNum[0] = num[1];
		add(baiNum, null);
		add(shiNum, null);
		add(geNum, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.jixuan.Balls#getZhuma(java.util.Vector, int)
	 */
	@Override
	public String getZhuma(Vector<Balls> balls, int beishu) {
		// TODO Auto-generated method stub
		typeCode = z3ds;
		String zhushu = "";
		int iZhuShu = balls.size();
		if (iZhuShu < 10) {
			zhushu += "0" + iZhuShu;
		} else if (iZhuShu >= 10) {
			zhushu += "" + iZhuShu;
		}
		String beishu_ = "";
		if (beishu < 10) {
			beishu_ += "0" + beishu;
		} else if (beishu >= 10) {
			beishu_ += "" + beishu;
		}
		String t_str = "";
		// t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-";
		t_str += "01" + beishu_;
		int bai = getVZhuma().get(0)[0];
		int shi = getVZhuma().get(1)[0];
		int ge = getVZhuma().get(2)[0];
		// 需要排序
		if (bai < ge) {
			t_str += "0" + bai + "0" + shi + "0" + ge + "^";
		} else {
			t_str += "0" + ge + "0" + shi + "0" + bai + "^";
		}
		return t_str;
	}

}
