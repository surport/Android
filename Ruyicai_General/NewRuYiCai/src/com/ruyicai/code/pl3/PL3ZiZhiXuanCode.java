/**
 * 
 */
package com.ruyicai.code.pl3;

import java.util.List;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

/**
 * @author Administrator
 * 
 */
public class PL3ZiZhiXuanCode extends CodeInterface {
	static String zhixuan = "1|";// ֱѡ

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		String strZM = "";
		int[] zhuma_baiwei = areaNums[0].table.getHighlightBallNOs();
		int[] zhuma_shiwei = areaNums[1].table.getHighlightBallNOs();
		int[] zhuma_gewei = areaNums[2].table.getHighlightBallNOs();

		strZM = zhixuan;
		if (zhuma_baiwei.length > 0 && zhuma_shiwei.length > 0
				&& zhuma_gewei.length > 0) {
			for (int i = 0; i < zhuma_baiwei.length; i++) {
				strZM += (zhuma_baiwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_shiwei.length; i++) {
				strZM += (zhuma_shiwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_gewei.length; i++) {
				strZM += (zhuma_gewei[i]) + "";
			}

		}
		return strZM;

	}

	public static String simulateZhuma(List<Integer> hundreds, List<Integer> decades,
			List<Integer> units) {
		// TODO Auto-generated method stub
		String strZM = "";
		strZM = zhixuan;
		if (hundreds.size() > 0 && decades.size() > 0 && units.size() > 0) {
			for (int i = 0; i < hundreds.size(); i++) {
				strZM += (hundreds.get(i)) + "";
			}
			strZM += ",";
			for (int i = 0; i < decades.size(); i++) {
				strZM += (decades.get(i)) + "";
			}
			strZM += ",";
			for (int i = 0; i < units.size(); i++) {
				strZM += (units.get(i)) + "";
			}

		}
		return strZM;
	}
}
