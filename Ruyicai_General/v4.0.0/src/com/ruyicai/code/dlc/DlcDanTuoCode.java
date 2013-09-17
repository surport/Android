package com.ruyicai.code.dlc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class DlcDanTuoCode extends CodeInterface {

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String zhuma(AreaNum[] areaNums, String type) {
		String str = "";
		if (type.equals("R2")) {
			str += "R2|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("R3")) {
			str += "R3|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("R4")) {
			str += "R4|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("R5")) {
			str += "R5|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("R6")) {
			str += "R6|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("R7")) {
			str += "R7|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("R8")) {
			str += "R8|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("Z2")) {
			str += "Z2|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		} else if (type.equals("Z3")) {
			str += "Z3|";
			int[] dan = areaNums[0].table.getHighlightBallNOs();
			int[] tuo = areaNums[1].table.getHighlightBallNOs();
			str += getRstring(dan);
			str += "$";
			str += getRstring(tuo);
		}
		return str;
	}

	public static String getRstring(int[] num) {
		String str = "";
		for (int i = 0; i < num.length; i++) {
			if (i != num.length - 1) {
				if (num[i] < 10) {
					str += "0" + num[i] + " ";
				} else {
					str += num[i] + " ";
				}
			} else {
				if (num[i] < 10) {
					str += "0" + num[i];
				} else {
					str += num[i];
				}
			}
		}
		return str;
	}
}
