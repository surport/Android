package com.ruyicai.code.jc.zq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.util.PublicMethod;

public class FootBQC {
	private static final String BQC_00 = "00";// 负负
	private static final String BQC_01 = "01";// 负平
	private static final String BQC_03 = "03";// 负胜
	private static final String BQC_10 = "10";// 平负
	private static final String BQC_11 = "11";// 平平
	private static final String BQC_13 = "13";// 平胜
	private static final String BQC_30 = "30";// 胜负
	private static final String BQC_31 = "31";// 胜平
	private static final String BQC_33 = "33";// 胜胜
	public static String bqcType[] = { BQC_33, BQC_31, BQC_30, BQC_13, BQC_11,
			BQC_10, BQC_03, BQC_01, BQC_00 };
	public static String titleStrs[] = { "胜胜", "胜平", "胜负", "平胜", "平平", "平负",
			"负胜", "负平", "负负" };
	JcType jcType;

	public FootBQC(Context context) {
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
		codeStr = codeType + "@" + danCode + code;
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
