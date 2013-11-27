/**
 * 
 */
package com.ruyicai.code.fc3d;

import java.util.List;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

/**
 * @author Administrator
 * 
 */
public class Fc3dZiZhiXuanCode extends CodeInterface {
	String CityCode = "1512-";// 城市编号
	String DDD_falg = "F47103-";// 彩种编号
	static String typeCode = "";// 玩法
	String staticCode = "-01-";
	static String endCode = "^";// 结束标志
	String dateCode = "0";// 数据
	static String zxfs = "20";// 单选复式

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		String[] zhuma = null;
		String t_str = "";
		int iZhuShu = areaNums[0].table.getHighlightBallNums()
				* areaNums[1].table.getHighlightBallNums()
				* areaNums[2].table.getHighlightBallNums();
		int[] zhuma_baiwei = areaNums[0].table.getHighlightBallNOs();
		int[] zhuma_shiwei = areaNums[1].table.getHighlightBallNOs();
		int[] zhuma_gewei = areaNums[2].table.getHighlightBallNOs();
		if (zhuma_baiwei.length == 1 && zhuma_shiwei.length == 1
				&& zhuma_gewei.length == 1) {
			typeCode = zxfs;
			// 直选单式注码格式有变化 陈晨 2010/7/11
			String[] str = { "01", "0" + (zhuma_baiwei[0]), "^", "01",
					"0" + (zhuma_shiwei[0]), "^", "01", "0" + (zhuma_gewei[0]) };
			zhuma = str;
		} else {
			// 3D直选复式注码玩法 2010/7/4 陈晨
			if (zhuma_baiwei.length != 0 && zhuma_shiwei.length != 0
					&& zhuma_gewei.length != 0) {
				typeCode = zxfs;
				String[] str = new String[zhuma_baiwei.length
						+ zhuma_shiwei.length + zhuma_gewei.length + 5];
				if (zhuma_baiwei.length < 10) {
					str[0] = "0" + zhuma_baiwei.length;
				} else {
					str[0] = zhuma_baiwei.length + "";
				}
				for (int i = 0; i < zhuma_baiwei.length; i++) {
					str[i + 1] = "0" + (zhuma_baiwei[i]);
				}
				str[zhuma_baiwei.length + 1] = "^";

				if (zhuma_shiwei.length < 10) {
					str[zhuma_baiwei.length + 2] = "0" + zhuma_shiwei.length;
				} else {
					str[zhuma_baiwei.length + 2] = zhuma_shiwei.length + "";
				}
				for (int i = 0; i < zhuma_shiwei.length; i++) {
					str[zhuma_baiwei.length + 3 + i] = "0" + (zhuma_shiwei[i]);
				}
				str[zhuma_baiwei.length + zhuma_shiwei.length + 3] = "^";
				if (zhuma_gewei.length < 10) {
					str[zhuma_baiwei.length + zhuma_shiwei.length + 4] = "0"
							+ zhuma_gewei.length;
				} else {
					str[zhuma_baiwei.length + zhuma_shiwei.length + 4] = zhuma_gewei.length
							+ "";
				}
				for (int i = 0; i < zhuma_gewei.length; i++) {
					str[zhuma_baiwei.length + zhuma_shiwei.length + 5 + i] = "0"
							+ (zhuma_gewei[i]);
				}
				zhuma = str;
			}

		}
		String zhushu = "";
		if (iZhuShu < 10) {
			zhushu += "0" + iZhuShu;
		} else if (iZhuShu >= 10) {
			zhushu += "" + iZhuShu;
		}

		String beishu_ = "01";
		// if (beishu < 10) {
		// beishu_ += "0" + beishu;
		// } else if (beishu >= 10) {
		// beishu_ += "" + beishu;
		// }
		// t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-" +
		// typeCode + beishu_;
		t_str += typeCode + beishu_;
		for (int i = 0; i < zhuma.length; i++) {
			t_str += zhuma[i];
		}
		t_str += endCode;

		return t_str;
	}

	public static String simulateZhuma(List<Integer> hundreds,
			List<Integer> decades, List<Integer> units) {
		
		String[] zhuma = null;
		String t_str = "";
		int iZhuShu = hundreds.size()* decades.size()* units.size();
		if (hundreds.size() == 1 && decades.size() == 1
				&& units.size() == 1) {
			typeCode = zxfs;
			String[] str = { "01", "0" + (hundreds.get(0)), "^", "01",
					"0" + (decades.get(0)), "^", "01", "0" + (units.get(0)) };
			zhuma = str;
		} else {
			// 3D直选复式注码玩法 2010/7/4 陈晨
			if (hundreds.size() != 0 && decades.size() != 0
					&& units.size() != 0) {
				typeCode = zxfs;
				String[] str = new String[hundreds.size()
						+ decades.size() + units.size() + 5];
				if (hundreds.size() < 10) {
					str[0] = "0" + hundreds.size();
				} else {
					str[0] = hundreds.size() + "";
				}
				for (int i = 0; i < hundreds.size(); i++) {
					str[i + 1] = "0" + (hundreds.get(i));
				}
				str[hundreds.size() + 1] = "^";

				if (decades.size() < 10) {
					str[hundreds.size() + 2] = "0" + decades.size();
				} else {
					str[hundreds.size() + 2] = decades.size() + "";
				}
				for (int i = 0; i < decades.size(); i++) {
					str[hundreds.size() + 3 + i] = "0" + (decades.get(i));
				}
				str[hundreds.size() + decades.size() + 3] = "^";
				if (units.size() < 10) {
					str[hundreds.size() + decades.size() + 4] = "0"
							+ units.size();
				} else {
					str[hundreds.size() + decades.size() + 4] = units.size()
							+ "";
				}
				for (int i = 0; i < units.size(); i++) {
					str[hundreds.size() + decades.size() + 5 + i] = "0"
							+ (units.get(i));
				}
				zhuma = str;
			}

		}
		String zhushu = "";
		if (iZhuShu < 10) {
			zhushu += "0" + iZhuShu;
		} else if (iZhuShu >= 10) {
			zhushu += "" + iZhuShu;
		}

		String beishu_ = "01";
		// if (beishu < 10) {
		// beishu_ += "0" + beishu;
		// } else if (beishu >= 10) {
		// beishu_ += "" + beishu;
		// }
		// t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-" +
		// typeCode + beishu_;
		t_str += typeCode + beishu_;
		for (int i = 0; i < zhuma.length; i++) {
			t_str += zhuma[i];
		}
		t_str += endCode;

		return t_str;
	}

}
