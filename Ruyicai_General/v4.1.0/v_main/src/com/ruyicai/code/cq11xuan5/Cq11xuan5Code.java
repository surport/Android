package com.ruyicai.code.cq11xuan5;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class Cq11xuan5Code extends CodeInterface {

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		return null;
	}

	public static String zhuma(AreaNum[] areaNums, String type) {
		String str = "";
		if (type.equals("PT_QZ1")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 1) {// 复试
				str += "101@*";
				str += getRstring(R) + "^";
			} else {
				str += "101@*";
				str += getRstring(R) + "^";
			}

		} else if (type.equals("PT_R2")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 2) {// 复试
				str += "102@*";
				str += getRstring(R) + "^";
			} else {
				str += "111@";
				str += getRstring(R) + "^";
			}
		} else if (type.equals("PT_R3")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 3) {// 复试
				str += "103@*";
				str += getRstring(R) + "^";
			} else {
				str += "112@";
				str += getRstring(R) + "^";
			}
		} else if (type.equals("PT_R4")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 4) {// 复试
				str += "104@*";
				str += getRstring(R) + "^";
			} else {
				str += "113@";
				str += getRstring(R) + "^";
			}
		} else if (type.equals("PT_R5")) {
			int[] R = null;
			if (isZHmiss()) {
				R = toInt(getIsZHcode().split("\\,"));
			} else {
				R = areaNums[0].table.getHighlightBallNOs();
			}

			if (R.length > 5) {// 复试
				str += "105@*";
				str += getRstring(R) + "^";
			} else {
				str += "114@";
				str += getRstring(R) + "^";
			}
		} else if (type.equals("PT_R6")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 6) {// 复试
				str += "106@*";
				str += getRstring(R) + "^";
			} else {
				str += "115@";
				str += getRstring(R) + "^";
			}
		} else if (type.equals("PT_R7")) {
			int[] R = null;
			if (isZHmiss()) {
				R = toInt(getIsZHcode().split("\\,"));
			} else {
				R = areaNums[0].table.getHighlightBallNOs();
			}
			if (R.length > 7) {// 复试
				str += "107@*";
				str += getRstring(R) + "^";
			} else {
				str += "116@";
				str += getRstring(R) + "^";
			}
		} else if (type.equals("PT_R8")) {
			int[] R = null;
			if (isZHmiss()) {
				R = toInt(getIsZHcode().split("\\,"));
			} else {
				R = areaNums[0].table.getHighlightBallNOs();
			}
			str += "117@";
			str += getRstring(R) + "^";
		} else if (type.equals("PT_QZ2")) {
			int[] q2w = areaNums[0].table.getHighlightBallNOs();
			int[] q2q = areaNums[1].table.getHighlightBallNOs();
			if (q2w.length > 1 || q2q.length > 1) {// 复试
				str += "142@";
				str += getRstring(q2w) + "*" + getRstring(q2q) + "^";
			} else {
				str += "141@";
				str += getRstring(q2w) + getRstring(q2q) + "^";
			}

		} else if (type.equals("PT_QZ3")) {
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
				str += "162@";
				str += getRstring(q3w) + "*" + getRstring(q3q) + "*"
						+ getRstring(q3b) + "^";
			} else {
				str += "161@";
				str += getRstring(q3w) + getRstring(q3q) + getRstring(q3b)
						+ "^";
			}
		} else if (type.equals("PT_ZU2")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 2) {// 复试
				str += "108@*";
				str += getRstring(R) + "^";
			} else {
				str += "131@";
				str += getRstring(R) + "^";
			}
		} else if (type.equals("PT_ZU3")) {
			int[] R = areaNums[0].table.getHighlightBallNOs();
			if (R.length > 3) {// 复试
				str += "109@*";
				str += getRstring(R) + "^";
			} else {
				str += "151@";
				str += getRstring(R) + "^";
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
}
