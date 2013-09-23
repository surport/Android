package com.ruyicai.code.jc.zq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.util.PublicMethod;

public class FootBF {
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
	public static String bqcType[] = { BF_10, BF_20, BF_21, BF_30,
			BF_31, BF_32, BF_40, BF_41, BF_42, BF_50, BF_51, BF_52, BF_90,
			BF_00, BF_11, BF_22, BF_33,BF_99, BF_01, BF_02, BF_12, BF_03,
			BF_13, BF_23, BF_04, BF_14, BF_24, BF_05, BF_15, BF_25, BF_09 };
	public static String titleStrs[] = { "1:0", "2:0", "2:1", "3:0",
			"3:1", "3:2", "4:0", "4:1", "4:2", "5:0", "5:1", "5:2", "胜其它", 
			"0:0", "1:1", "2:2", "3:3", "平其它", "0:1", "0:2", "1:2", "0:3",
			"1:3", "2:3", "0:4", "1:4", "2:4", "0:5", "1:5", "2:5","负其它" };
	JcType jcType;

	public FootBF(Context context) {
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
		String danCode = "";
		String codeStr;
		int isDanNum = 0;
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				String everyCode = info.getDay() + "|" + info.getWeek() + "|"
						+ info.getTeamId() + "|";
				for (int j = 0; j < info.check.length; j++) {
					if (info.check[j].isChecked()) {
						everyCode += bqcType[j];
					}
				}
				if (info.isDan()) {
					isDanNum++;
					danCode += everyCode + "^";
				} else {
					code += everyCode + "^";
				}
			}

		}
		if (isDanNum > 0) {
			codeType = (Integer.parseInt(codeType) + jcType.getDanType()) + "";
			danCode += "$";
		}
		codeStr = codeType + "@" + danCode + code;// 设胆的在前面
		return codeStr;
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
}
