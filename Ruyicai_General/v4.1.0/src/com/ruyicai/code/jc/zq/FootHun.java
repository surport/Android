package com.ruyicai.code.jc.zq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class FootHun {

	// 半全场
	private static final String BQC_00 = "00";// 负负
	private static final String BQC_01 = "01";// 负平
	private static final String BQC_03 = "03";// 负胜
	private static final String BQC_10 = "10";// 平负
	private static final String BQC_11 = "11";// 平平
	private static final String BQC_13 = "13";// 平胜
	private static final String BQC_30 = "30";// 胜负
	private static final String BQC_31 = "31";// 胜平
	private static final String BQC_33 = "33";// 胜胜
	// 总进球
	private static final String ZJQ_00 = "0";//
	private static final String ZJQ_01 = "1";//
	private static final String ZJQ_02 = "2";//
	private static final String ZJQ_03 = "3";//
	private static final String ZJQ_04 = "4";//
	private static final String ZJQ_05 = "5";//
	private static final String ZJQ_06 = "6";//
	private static final String ZJQ_07 = "7";//
	// 比分
	private static final String BF_90 = "90";// 胜其他
	private static final String BF_10 = "10";
	private static final String BF_20 = "20";
	private static final String BF_21 = "21";
	private static final String BF_30 = "30";
	private static final String BF_31 = "31";
	private static final String BF_32 = "32";
	private static final String BF_40 = "40";
	private static final String BF_41 = "41";
	private static final String BF_42 = "42";
	private static final String BF_50 = "50";
	private static final String BF_51 = "51";
	private static final String BF_52 = "52";
	private static final String BF_99 = "99";// 平其它
	private static final String BF_00 = "00";
	private static final String BF_11 = "11";
	private static final String BF_22 = "22";
	private static final String BF_33 = "33";
	private static final String BF_09 = "09";// 负其他
	private static final String BF_01 = "01";
	private static final String BF_02 = "02";
	private static final String BF_12 = "12";
	private static final String BF_03 = "03";
	private static final String BF_13 = "13";
	private static final String BF_23 = "23";
	private static final String BF_04 = "04";
	private static final String BF_14 = "14";
	private static final String BF_24 = "24";
	private static final String BF_05 = "05";
	private static final String BF_15 = "15";
	private static final String BF_25 = "25";
	public static String bqcType[] = {"3", "1", "0", "3", "1", "0", BQC_33, BQC_31, BQC_30,
			BQC_13, BQC_11, BQC_10, BQC_03, BQC_01, BQC_00, ZJQ_00, ZJQ_01,
			ZJQ_02, ZJQ_03, ZJQ_04, ZJQ_05, ZJQ_06, ZJQ_07, BF_10, BF_20,
			BF_21, BF_30, BF_31, BF_32, BF_40, BF_41, BF_42, BF_50, BF_51,
			BF_52, BF_90, BF_00, BF_11, BF_22, BF_33, BF_99, BF_01, BF_02,
			BF_12, BF_03, BF_13, BF_23, BF_04, BF_14, BF_24, BF_05, BF_15,
			BF_25, BF_09 };
	public static String titleStrs[] = {"胜", "平", "负", "胜", "平", "负", "胜胜", "胜平", "胜负", "平胜",
			"平平", "平负", "负胜", "负平", "负负", "0", "1", "2", "3", "4", "5", "6",
			"7+", "1:0", "2:0", "2:1", "3:0", "3:1", "3:2", "4:0", "4:1",
			"4:2", "5:0", "5:1", "5:2", "胜其它", "0:0", "1:1", "2:2", "3:3",
			"平其它", "0:1", "0:2", "1:2", "0:3", "1:3", "2:3", "0:4", "1:4",
			"2:4", "0:5", "1:5", "2:5", "负其它" };
	JcType jcType;

	public FootHun(Context context) {
		jcType = new JcType(context);
	}

	/**
	 * 
	 * 获取注码
	 * 
	 */
	public String getCode(String key, List<Info> listInfo) {
		String codeType = jcType.getValues(key);
		String code = "";
		String codeStr;
		int isDanNum = 0;
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				code += getPlayCode(info, Constants.LOTNO_JCZQ, 0, 2);// 胜平负
				code += getPlayCode(info, Constants.LOTNO_JCZQ_RQSPF, 3, 5);// 让球胜平负
				code += getPlayCode(info, Constants.LOTNO_JCZQ_BQC, 6, 14);// 半全场
				code += getPlayCode(info, Constants.LOTNO_JCZQ_ZQJ, 15, 22);// 总进球
				code += getPlayCode(info, Constants.LOTNO_JCZQ_BF, 23, 53);// 比分

			}

		}
		codeStr = codeType + "@" + code;// 设胆的在前面
		return codeStr;
	}

	private String getPlayCode(Info info, String lotno, int start, int end) {
		boolean isCheck = false;
		String everyCode = info.getDay() + "|" + info.getWeek() + "|"
				+ info.getTeamId() + "|" + lotno + "|";
		for (int j = start; j < end + 1; j++) {
			if (info.check[j].isChecked()) {
				isCheck = true;
				everyCode += bqcType[j];
			}
		}
		if (isCheck) {
			everyCode += "^";
		} else {
			everyCode = "";
		}
		return everyCode;
	}

	/**
	 * 获取最小奖金赔率
	 */

	public List<double[]> getMinOddsList(List<Info> listInfo) {
		List<double[]> oddsList = new ArrayList<double[]>();/* 赛事选择后的赔率列表 */
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				try {
					double[] aa = new double[info.check.length];
					for (int j = 0; j < info.check.length; j++) {
						if (info.check[j].isChecked()) {
							aa[j] = Double.parseDouble(info.getVStrs()[j]);
						}
					}
					double[] insertdouble = PublicMethod
							.getDoubleArrayNoZero(aa);
					oddsList.add(insertdouble);
				} catch (NumberFormatException e) {
					double[] result = { 1 };
					oddsList.add(result);
				}
			}

		}
		return oddsList;
	}

	/**
	 * 获取赔率List
	 */

	public List<double[]> getOddsList(List<Info> listInfo) {
		List<double[]> oddsList = new ArrayList<double[]>();/* 赛事选择后的赔率列表 */
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				try {
					double[] aa = new double[5];
					for (int j = 0; j < info.check.length; j++) {
						if (info.check[j].isChecked()) {
							getMaxOdd(j,
									Double.parseDouble(info.getVStrs()[j]), aa);
						}
					}
					double[] insertdouble = PublicMethod
							.getDoubleArrayNoZero(aa);
					oddsList.add(insertdouble);
				} catch (NumberFormatException e) {
					double[] result = { 1 };
					oddsList.add(result);
				}
			}

		}
		return oddsList;
	}

	private void getMaxOdd(int index, double odd, double[] odds) {
		if (6 <= index && index <= 14) {// 半全场
			if (odds[0] < odd) {
				odds[0] = odd;
			}
		} else if (23 <= index && index <= 53) {// 比分
			if (odds[1] < odd) {
				odds[1] = odd;
			}
		} else if (15 <= index && index <= 22) {// 总进球
			if (odds[2] < odd) {
				odds[2] = odd;
			}
		} else if (3 <= index && index <= 5) {// 胜平负
			if (odds[4] < odd) {
				odds[4] = odd;
			}
		} else if (0 <= index && index <= 2) {// 胜平负
			if (odds[3] < odd) {
				odds[3] = odd;
			}
		}
	}
}
