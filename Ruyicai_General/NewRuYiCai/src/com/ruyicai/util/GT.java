package com.ruyicai.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;

public class GT {
	static Random rnd = new Random();

	public static int getRandEx(int r1, int r2) {

		return (rnd.nextInt() >>> 1) % (r2 - r1 + 1) + r1;
	}

	/**
	 * 数组排序
	 * 
	 * @param t
	 * @return
	 */
	public static int[] sort(int t[]) {
		int t_s[] = t;
		int t_a = 0;
		for (int i = 0; i < t_s.length; i++) {
			for (int j = i + 1; j < t_s.length; j++) {
				if (t_s[i] > t_s[j]) {
					t_a = t_s[i];
					t_s[i] = t_s[j];
					t_s[j] = t_a;
				}
			}
		}
		return t_s;
	}

	/**
	 * 获得随机注码
	 * 
	 * @param type
	 *            彩种 0 —— 双色球 1 —— 福彩3D 2 —— 七乐彩
	 * @param iNote
	 *            产生随机注码的注数
	 * @return
	 */
	public static int[][] getBetCode(int type, int iNote) {
		int betCode[][] = new int[1][7];

		switch (type) {
		case 0:// 双色球
			betCode = new int[iNote][7];
			for (int i = 0; i < iNote; i++) {
				for (int j = 0; j < 6; j++) {
					betCode[i][j] = GT.getRandEx(1, 32);
					for (int m = 0; m < j; m++) {
						if (betCode[i][j] == betCode[i][m]) {
							j--;
							break;
						}
					}
				}
				betCode[i][6] = GT.getRandEx(1, 16);
				betCode[i] = sort(betCode[i]);
			}
			break;
		case 1:// 福彩3D
			betCode = new int[iNote][3];
			for (int i = 0; i < iNote; i++) {
				for (int j = 0; j < 3; j++) {
					betCode[i][j] = GT.getRandEx(0, 9);
					for (int m = 0; m < j; m++) {
						if (betCode[i][j] == betCode[i][m]) {
							j--;
							break;
						}
					}
				}
			}
			break;
		case 2:// 七乐彩
			betCode = new int[iNote][7];
			for (int i = 0; i < iNote; i++) {
				for (int j = 0; j < 7; j++) {
					betCode[i][j] = GT.getRandEx(1, 30);
					for (int m = 0; m < j; m++) {
						if (betCode[i][j] == betCode[i][m]) {
							j--;
							break;
						}
					}
				}
				betCode[i] = sort(betCode[i]);
			}
			break;
		}

		return betCode;
	}

	public static String strAreaCode = "1512";

	/**
	 * 将投注码生成字符串
	 * 
	 * @param caipiaoIdIndex
	 *            : 彩票类型 0 —— 双色球; 1 —— 福彩3D; 2 —— 七乐彩
	 * @param caipiaoWanfaIndex
	 *            : 高频彩的玩法类型
	 * @param maxNum
	 *            : 最大投注数
	 * @param playType
	 *            : 彩票玩法
	 * @param mul
	 *            : 整型投注码的倍数
	 * @param betCode
	 *            : 整型投注码
	 * @return
	 */
	public static String betCodeToString(int caipiaoIdIndex,
			int caipiaoWanfaIndex, int maxNum, String playType, int mul,
			int[] betCode) {

		StringBuilder strBetCode = new StringBuilder();
		switch (caipiaoIdIndex) {

		// 双色球
		case 0:
			if (playType.equals("00")) {

				strBetCode.append(playType);

				if (mul < 10) {
					strBetCode.append("0" + mul);
				} else {
					strBetCode.append(mul);
				}

				for (int j = 0; j < 6; j++) {
					if (betCode[j] < 10) {
						strBetCode.append("0" + betCode[j]);
					} else {
						strBetCode.append(betCode[j]);
					}
				}
				strBetCode.append("~");
				if (betCode[6] < 10) {
					strBetCode.append("0" + betCode[6]);
				} else {
					strBetCode.append(betCode[6]);
				}
				strBetCode.append("^");

			}

			break;

		// 大乐透
		case 1:

			for (int j = 0; j < betCode.length - 3; j++) {
				if (betCode[j] < 10) {
					strBetCode.append("0" + betCode[j] + " ");
				} else {
					strBetCode.append(betCode[j] + " ");
				}
			}
			if (betCode[betCode.length - 3] < 10) {
				strBetCode.append("0" + betCode[betCode.length - 3] + "-");
			} else {
				strBetCode.append(betCode[betCode.length - 3] + "-");
			}
			if (betCode[betCode.length - 2] < 10) {
				strBetCode.append("0" + betCode[betCode.length - 2] + " ");
			} else {
				strBetCode.append(betCode[betCode.length - 2] + " ");
			}
			if (betCode[betCode.length - 1] < 10) {
				strBetCode.append("0" + betCode[betCode.length - 1]);
			} else {
				strBetCode.append(betCode[betCode.length - 1]);
			}

			break;
		// 福彩3D
		case 2:
			if (playType.equals("00")) {

				strBetCode.append(playType);

				if (mul < 10) {
					strBetCode.append("0" + mul);
				} else {
					strBetCode.append(mul);
				}

				for (int j = 0; j < 3; j++) {
					if (betCode[j] < 10) {
						strBetCode.append("0" + betCode[j]);
					} else {
						strBetCode.append(betCode[j]);
					}
				}

				strBetCode.append("^");

			}
			break;

		// 时时彩
		case 3:

			strBetCode.append(playType);

			// 大小玩法要转值
			if (caipiaoWanfaIndex == 4) {
				int[] keyValue = { 2, 1, 5, 4 };
				strBetCode.append(keyValue[betCode[0]]).append(
						keyValue[betCode[1]]);

				// 其它玩法
			} else {
				for (int i = 0; i < betCode.length - 1; i++) {
					strBetCode.append(betCode[i] + ",");
				}

				strBetCode.append(betCode[betCode.length - 1]);
			}

			break;

		// 江西11选5
		case 4:

			strBetCode.append(playType);

			// 球之间的间隔符: 选前2, 选前3用逗号，其它的用空格
			String gap;
			switch (caipiaoWanfaIndex) {
			// 选前2
			case 8:

				// 选前3
			case 9:
				gap = ",";
				break;

			// 其它玩法
			default:
				gap = " ";
				break;
			}

			for (int i = 0; i < betCode.length - 1; i++) {
				strBetCode.append(get2bit(betCode[i]) + gap);
			}

			strBetCode.append(get2bit(betCode[betCode.length - 1]));

			break;

		// 11运夺金
		case 5:

			strBetCode.append(playType);

			for (int i = 0; i < betCode.length; i++) {
				strBetCode.append(get2bit(betCode[i]));
			}
			strBetCode.append("^");

			break;

		// 广东11选5
		case 6:

			strBetCode.append(playType);

			for (int i = 0; i < betCode.length; i++) {
				strBetCode.append(get2bit(betCode[i]));
			}

			break;

		// 广东快乐十分
		case 7:

			strBetCode.append(playType);

			for (int i = 0; i < betCode.length; i++) {
				strBetCode.append(get2bit(betCode[i]));
			}

			break;

		default:
			break;
		}

		return strBetCode.toString();
	}

	/**
	 * 将投注码生成字符串
	 * 
	 * @param type
	 *            彩票类型 0 —— 双色球; 1 —— 福彩3D; 2 —— 七乐彩
	 * @param maxNum
	 *            最大投注数
	 * @param playType
	 *            彩票玩法
	 * @param mul
	 *            整型投注码的倍数
	 * @param betCode
	 *            整型投注码
	 */
	public static String betCodeToString(int type, int maxNum, String playType,
			int mul, int[][] betCode) {

		// 默认 caipiaoWanfaId 为 0.
		return betCodeToString(type, 0, maxNum, playType, mul, betCode[0]);
	}

	/**
	 * 两位数，不足补 0
	 */
	public static String get2bit(int i) {
		if (i < 10) {
			return "0" + i;
		}

		return "" + i;
	}

	/**
	 * 将输入的字符转化成*
	 * 
	 * @param str
	 * @return
	 */
	public static String returnPassword(String str) {
		String pass = "";
		int length = str.length();
		StringBuffer login_Pas_num = new StringBuffer("");

		for (int i = 0; i < length; i++) {
			pass = login_Pas_num.append('*').toString();
		}

		return pass;

	}

	public static String[] splitBetCode(String pns) {
		String[] tmp = null;
		try {
			Vector vector = new Vector();
			int sIndex = 0;
			int eIndex = 0;

			String tempS = null;
			boolean flag = false;
			for (int i = 0; i < pns.length(); i++) {
				if (pns.charAt(i) == '^') {
					flag = true;
					eIndex = i;
					tempS = pns.substring(sIndex, eIndex);
					if (!tempS.equals("")) {
						vector.addElement(tempS);
					}
					sIndex = eIndex + 1;
				}
				if (flag)
					if (i == pns.length() - 1) {
						tempS = pns.substring(sIndex, i + 1);
						if (!tempS.equals("")) {
							vector.addElement(tempS);
						}
					}
			}
			if (!flag) {
				tempS = pns;
				if (!tempS.equals("")) {
					vector.addElement(tempS);
				}
			}
			tmp = new String[vector.size()];
			for (int i = 0; i < vector.size(); i++) {
				tmp[i] = (String) vector.elementAt(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

	public static String[] splitBetCodeTC(String pns) {
		String[] tmp = null;
		try {
			Vector vector = new Vector();
			int sIndex = 0;
			int eIndex = 0;

			String tempS = null;
			boolean flag = false;
			for (int i = 0; i < pns.length(); i++) {
				if (pns.charAt(i) == ';') {
					flag = true;
					eIndex = i;
					tempS = pns.substring(sIndex, eIndex);
					if (!tempS.equals("")) {
						vector.addElement(tempS);
					}
					sIndex = eIndex + 1;
				}
				if (flag)
					if (i == pns.length() - 1) {
						tempS = pns.substring(sIndex, i + 1);
						if (!tempS.equals("")) {
							vector.addElement(tempS);
						}
					}
			}
			if (!flag) {
				tempS = pns;
				if (!tempS.equals("")) {
					vector.addElement(tempS);
				}
			}
			tmp = new String[vector.size()];
			for (int i = 0; i < vector.size(); i++) {
				tmp[i] = (String) vector.elementAt(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

	public static String makeString(String strLotteryType, int wayCode,
			String str) {
		String tmp = "";
		if (strLotteryType.equals("F47104")) {
			if (wayCode == 00) {// 单式
				for (int i = 0; i < str.length();) {
					if (str.charAt(i) == '~') {
						i++;
					}
					if (i < str.length() - 2) {
						tmp += str.substring(i, i + 2) + " ";
					} else if (i == str.length() - 2) {
						tmp += " | " + str.substring(i, i + 2);
					}
					i += 2;
				}
			} else if (wayCode == 40 || wayCode == 50) {// 胆拖
				for (int i = 0; i < str.length();) {
					if (i < str.length() - 2) {
						tmp += str.substring(i, i + 2) + ",";
					} else if (i == str.length() - 2) {
						tmp += str.substring(i, i + 2);
					}

					i += 2;

				}
			} else {
				for (int i = 0; i < str.length();) {
					if (i < str.length() - 2) {
						tmp += str.substring(i, i + 2) + ",";
					} else if (i == str.length() - 2) {
						tmp += str.substring(i, i + 2);
					}
					i += 2;
				}
			}

		} else if (strLotteryType.equals("F47103")) {
			if (wayCode == 32 || wayCode == 31) {
				str = str.substring(2);
			}
			for (int i = 0; i < str.length();) {
				if (i < str.length() - 2) {
					tmp += str.substring(i, i + 2);
				} else if (i == str.length() - 2) {
					tmp += str.substring(i, i + 2);
				}
				i += 2;
			}
		} else if (strLotteryType.equals("F47102")) {

			for (int i = 0; i < str.length();) {
				if (i < str.length() - 2) {
					tmp += str.substring(i, i + 2) + ",";
				} else if (i == str.length() - 2) {
					tmp += str.substring(i, i + 2);
				}

				i += 2;

			}
		} else if (strLotteryType.equals("T01001")) {
			int iStr = 0;
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '-') {
					iStr = i;
				}

			}
			tmp = str.substring(0, iStr) + "|" + str.substring(iStr + 1);

		} else if (strLotteryType.equals("T01002")) {
			tmp = str.substring(2);
		}

		return tmp;
	}

	public static String encodingString(String oldstring, String oldEncoding,
			String newEncoding) throws UnsupportedEncodingException {

		OutputStreamWriter outputStreamWriter = null;
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				oldstring.getBytes(oldEncoding));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		InputStreamReader inputStreamRead = null;

		char cbuf[] = new char[1024];
		int retVal = 0;
		try {
			inputStreamRead = new InputStreamReader(byteArrayInputStream,
					oldEncoding);
			outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream,
					newEncoding);
			while ((retVal = inputStreamRead.read(cbuf)) != -1) {
				outputStreamWriter.write(cbuf, 0, retVal);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStreamRead.close();
				outputStreamWriter.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			;
		}

		String temp = null;
		try {
			temp = new String(byteArrayOutputStream.toByteArray(), newEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 排列三、超级大乐透幸运选号投注格式
	 * 
	 * @param type
	 * @param betCode
	 * @return
	 */
	public static String betCodeToStringTC(int type, int[][] betCode) {
		String strBetCode = "";
		switch (type) {
		case 3:// 排列三

			for (int i = 0; i < betCode.length - 1; i++) {
				strBetCode += "1|";
				for (int j = 0; j < betCode[i].length - 1; j++) {
					strBetCode += betCode[i][j] + ",";
				}
				strBetCode += betCode[i][betCode[i].length - 1];
				strBetCode += ";";
			}
			strBetCode += "1|";
			for (int j = 0; j < betCode[betCode.length - 1].length - 1; j++) {
				strBetCode += betCode[betCode.length - 1][j] + ",";
			}
			strBetCode += betCode[betCode.length - 1][betCode[betCode.length - 1].length - 1];
			break;
		case 4:// 大乐透
			for (int i = 0; i < betCode.length - 1; i++) {
				for (int j = 0; j < betCode[i].length - 3; j++) {
					if (betCode[i][j] < 10) {
						strBetCode += "0" + betCode[i][j] + " ";
					} else {
						strBetCode += betCode[i][j] + " ";
					}
				}
				if (betCode[i][betCode[i].length - 3] < 10) {
					strBetCode += "0" + betCode[i][betCode[i].length - 3] + "-";
				} else {
					strBetCode += betCode[i][betCode[i].length - 3] + "-";
				}
				if (betCode[i][betCode[i].length - 2] < 10) {
					strBetCode += "0" + betCode[i][betCode[i].length - 2] + " ";
				} else {
					strBetCode += betCode[i][betCode[i].length - 2] + " ";
				}
				if (betCode[i][betCode[i].length - 1] < 10) {
					strBetCode += "0" + betCode[i][betCode[i].length - 1];
				} else {
					strBetCode += betCode[i][betCode[i].length - 1];
				}
				strBetCode += ";";
			}
			for (int j = 0; j < betCode[betCode.length - 1].length - 3; j++) {
				if (betCode[betCode.length - 1][j] < 10) {
					strBetCode += "0" + betCode[betCode.length - 1][j] + " ";
				} else {
					strBetCode += betCode[betCode.length - 1][j] + " ";
				}
			}
			if (betCode[betCode.length - 1][betCode[betCode.length - 1].length - 3] < 10) {
				strBetCode += "0"
						+ betCode[betCode.length - 1][betCode[betCode.length - 1].length - 3]
						+ "-";
			} else {
				strBetCode += betCode[betCode.length - 1][betCode[betCode.length - 1].length - 3]
						+ "-";
			}
			if (betCode[betCode.length - 1][betCode[betCode.length - 1].length - 2] < 10) {
				strBetCode += "0"
						+ betCode[betCode.length - 1][betCode[betCode.length - 1].length - 2]
						+ " ";
			} else {
				strBetCode += betCode[betCode.length - 1][betCode[betCode.length - 1].length - 2]
						+ " ";
			}
			if (betCode[betCode.length - 1][betCode[betCode.length - 1].length - 1] < 10) {
				strBetCode += "0"
						+ betCode[betCode.length - 1][betCode[betCode.length - 1].length - 1];
			} else {
				strBetCode += betCode[betCode.length - 1][betCode[betCode.length - 1].length - 1];
			}
			break;
		}
		return strBetCode;
	}

}
