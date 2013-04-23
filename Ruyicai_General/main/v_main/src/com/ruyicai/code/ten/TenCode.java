package com.ruyicai.code.ten;

import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class TenCode extends CodeInterface {
	private static final String DAN_TYPE = "S";
	private static final String P_TYPE = "P";
	private static final String FU_TYPE = "M";

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String zhuma(AreaNum[] areaNums, String type) {
		String str = "";
		if (type.equals("S1")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 1) {// 复试
				str += FU_TYPE + "|" + type + "|";
				str += getRstring(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}

		} else if (type.equals("H1")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 1) {// 复试
				str += DAN_TYPE + "|" + type + "|";
				str += getRstringTwo(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}
		} else if (type.equals("R2")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 2) {// 复试
				str += FU_TYPE + "|" + type + "|";
				str += getRstring(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}
		} else if (type.equals("R3")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 3) {// 复试
				str += FU_TYPE + "|" + type + "|";
				str += getRstring(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}
		} else if (type.equals("R4")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 4) {// 复试
				str += FU_TYPE + "|" + type + "|";
				str += getRstring(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}
		} else if (type.equals("R5")) {
			int[] R = null;
			if (isZHmiss()) {
				R = toInt(getIsZHcode().split("\\,"));
			} else {
				R = areaNums[0].table.getHighlightBallNOs();
			}
			if (R.length > 5) {// 复试
				str += FU_TYPE + "|" + type + "|";
				str += getRstring(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}
		} else if (type.equals("Q2")) {
			int[] q2w = areaNums[0].table.getHighlightBallNOs();
			int[] q2q = areaNums[1].table.getHighlightBallNOs();
			if (q2w.length > 1 || q2q.length > 1) {// 复试
				str += P_TYPE + "|" + type + "|";
				str += getRstring(q2w) + "-" + getRstring(q2q);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(q2w) + getRstring(q2q);
			}

		} else if (type.equals("Q3")) {
			int[] q3w = null;
			int[] q3q = null;
			int[] q3b = null;
			if (isZHmiss()) {
				int[] R = toInt(getIsZHcode().split("\\,"));
				q3w = new int[1];
				q3w[0] = R[0];
				q3q = new int[1];
				q3q[0] = R[1];
				q3b = new int[1];
				q3b[0] = R[2];
			} else {
				q3w = areaNums[0].table.getHighlightBallNOs();
				q3q = areaNums[1].table.getHighlightBallNOs();
				q3b = areaNums[2].table.getHighlightBallNOs();
			}
			if (q3w.length > 1 || q3q.length > 1 || q3b.length > 1) {// 复试
				str += P_TYPE + "|" + type + "|";
				str += getRstring(q3w) + "-" + getRstring(q3q) + "-"
						+ getRstring(q3b);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(q3w) + getRstring(q3q) + getRstring(q3b);
			}
		} else if (type.equals("Z2")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 2) {// 复试
				str += FU_TYPE + "|" + type + "|";
				str += getRstring(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}
		} else if (type.equals("Z3")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 3) {// 复试
				str += FU_TYPE + "|" + type + "|";
				str += getRstring(R);
			} else {
				str += DAN_TYPE + "|" + type + "|";
				str += getRstring(R);
			}
		}
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

	public static String getRstringTwo(int[] num) {
		String str = "";
		for (int i = 0; i < num.length; i++) {
			if (num[i] < 10) {
				str += "0" + num[i];
			} else {
				str += num[i];
			}
			if (i != num.length - 1) {
				str += "^";
			}
		}
		return str;
	}
}
