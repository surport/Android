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
public class Fc3dZhiXuanBalls extends Balls {
	String CityCode = "1512-";// 城市编号
	String DDD_falg = "F47103-";// 彩种编号
	String typeCode = "";// 玩法
	String staticCode = "-01-";
	String endCode = "^";// 结束标志
	String dateCode = "0";// 数据
	String sendCode = "";
	String zxfs = "20";// 单选复式

	public Fc3dZhiXuanBalls() {
		init();
	}

	/**
	 * 工厂方法
	 * 
	 * @return
	 */
	public Balls createBalls() {
		return new Fc3dZhiXuanBalls();

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

		int[] baiNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] shiNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		int[] geNum = PublicMethod.getRandomsWithoutCollision(1, 0, 9);
		add(baiNum, null);
		add(shiNum, null);
		add(geNum, null);
	}

	public String getZhuma(Vector<Balls> balls, int beishu) {
		typeCode = zxfs;
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
		t_str += "00" + beishu_;
		int bai = getVZhuma().get(0)[0];
		int shi = getVZhuma().get(1)[0];
		int ge = getVZhuma().get(2)[0];
		t_str += "0" + bai + "0" + shi + "0" + ge + "^";
		return t_str;
	}
}
