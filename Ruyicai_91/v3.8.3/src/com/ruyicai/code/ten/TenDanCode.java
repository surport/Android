package com.ruyicai.code.ten;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class TenDanCode extends CodeInterface {
	private static final String TUO_TYPE = "D";

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String zhuma(AreaNum[] areaNums, String type) {
		String str = "";
		int[] dan = areaNums[0].table.getHighlightBallNOs();
		int[] tuo = areaNums[1].table.getHighlightBallNOs();
		str += TUO_TYPE + "|" + type + "|";
		str += getRstring(dan) + "-";
		str += getRstring(tuo);
		return str;
	}

	public static String getRstring(int[] num) {
		String str = "";
		for (int i = 0; i < num.length; i++) {
			if (num[i] < 10) {
				str += "0" + num[i];
			} else {
				str += num[i];
			}
		}
		return str;
	}
}
