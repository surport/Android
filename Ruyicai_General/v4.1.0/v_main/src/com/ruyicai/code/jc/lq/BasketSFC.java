package com.ruyicai.code.jc.lq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.util.PublicMethod;

public class BasketSFC {
	private static final String BFC_01 = "01";
	private static final String BFC_02 = "02";
	private static final String BFC_03 = "03";
	private static final String BFC_04 = "04";
	private static final String BFC_05 = "05";
	private static final String BFC_06 = "06";
	private static final String BFC_11 = "11";
	private static final String BFC_12 = "12";
	private static final String BFC_13 = "13";
	private static final String BFC_14 = "14";
	private static final String BFC_15 = "15";
	private static final String BFC_16 = "16";
	public static String bqcType[] = { BFC_01, BFC_02, BFC_03, BFC_04, BFC_05,
			BFC_06, BFC_11, BFC_12, BFC_13, BFC_14, BFC_15, BFC_16 };
	public static String titleStrs[] = { "主胜1-5分", "主胜6-10分", "主胜11-15分",
			"主胜16-20分", "主胜21-25分", "主胜26分以上", "主负1-5分", "主负6-10分", "主负11-15分",
			"主负16-20分", "主负21-25分", "主负26分以上" };
	JcType jcType;

	public BasketSFC(Context context) {
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
