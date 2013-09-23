package com.ruyicai.util;

/**
 * 本类的功能是设置如意彩通知的相应彩种的注码格式
 * 
 * @author miao
 * 
 */
public class FormatZhuma {

	public static String formatZhuma(int i, String betCode) {
		switch (i) {
		case 0:// 双色球
			return formatSSQAndQLCZhuma(betCode);
		case 1:// 福彩3d
			return formatFC3DZhuma(betCode);
		case 2:// 七乐彩
			return formatSSQAndQLCZhuma(betCode);
		case 3:// 大乐透
			return formatDltZhuma(betCode);
		case 4:// 七星彩
			return formatTCZhuma(betCode);
		case 5:// 排列三
			return formatTCZhuma(betCode);
		case 6:// 排列五
			return formatTCZhuma(betCode);
		case 7:// 22选5
			return format225Zhuma(betCode);
		}
		return "";
	}

	/**
	 * 按需求格式化双色球和七乐彩的注码
	 * 
	 * @param betCode
	 * @return
	 */
	private static String formatSSQAndQLCZhuma(String betCode) {

		StringBuffer formatCode = new StringBuffer();
		for (int j = 0; j < betCode.length() / 2; j++) {

			if (j < betCode.length() / 2 - 2) {
				formatCode.append(betCode.substring(2 * j, 2 * j + 2)).append(
						",");
			} else if (j == betCode.length() / 2 - 2) {
				formatCode.append(betCode.substring(2 * j, 2 * j + 2)).append(
						"|");
			} else {
				formatCode.append(betCode.substring(2 * j, 2 * j + 2));
			}

		}
		return formatCode.toString();
	}

	/**
	 * 按需求格式化大乐透的注码
	 * 
	 * @param betCode
	 * @return
	 */
	private static String formatDltZhuma(String betCode) {
		String bbbbbb = betCode.replace(" ", ",");
		String c = bbbbbb.replace("+", "|");
		return c;
	}

	/**
	 * 按需求格式化排列三，排列五，七星彩
	 * 
	 * @param betCode
	 * @return
	 */
	private static String formatTCZhuma(String betCode) {
		StringBuffer formatCode = new StringBuffer();
		for (int j = 0; j < betCode.length(); j++) {

			if (j < betCode.length() - 1) {
				formatCode.append(betCode.substring(j, j + 1)).append(",");
			} else {
				formatCode.append(betCode.substring(j, j + 1));
			}

		}
		return formatCode.toString();
	}

	/**
	 * 按需求格式化22选5的注码
	 * 
	 * @param betCode
	 * @return
	 */
	private static String format225Zhuma(String betCode) {
		StringBuffer formatCode = new StringBuffer();
		for (int j = 0; j < betCode.length() / 2; j++) {

			if (j < betCode.length() / 2 - 1) {
				formatCode.append(betCode.substring(2 * j, 2 * j + 2)).append(
						",");
			} else {
				formatCode.append(betCode.substring(2 * j, 2 * j + 2));
			}

		}
		return formatCode.toString();
	}

	/**
	 * 按需求格式化福彩3D的注码
	 * 
	 * @param betCode
	 * @return
	 */
	private static String formatFC3DZhuma(String betCode) {
		StringBuffer formatCode = new StringBuffer();
		for (int j = 0; j < betCode.length() / 2; j++) {

			if (j < betCode.length() / 2 - 1) {
				formatCode.append(betCode.substring(2 * j + 1, 2 * j + 2))
						.append(",");
			} else {
				formatCode.append(betCode.substring(2 * j + 1, 2 * j + 2));
			}

		}
		return formatCode.toString();
	}

}
