package com.ruyicai.code.jc.zq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.util.PublicMethod;

public class FootSpf {
	JcType jcType;

	public FootSpf(Context context) {
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
				if (info.isWin()) {
					everyCode += "3";
				}
				if (info.isLevel()) {
					everyCode += "1";
				}
				if (info.isFail()) {
					everyCode += "0";
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
		codeStr = codeType + "@" + danCode + code;// 设胆在前
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
					double[] aa = new double[3];
					if (info.isWin()) {
						aa[0] = Float.parseFloat(info.getWin());
					}
					if (info.isLevel()) {
						aa[1] = Float.parseFloat(info.getLevel());
					}
					if (info.isFail()) {
						aa[2] = Float.parseFloat(info.getFail());
					}
					double[] insertdouble = PublicMethod
							.getDoubleArrayNoZero(aa);
					oddsList.add(insertdouble);
				} catch (NumberFormatException e) {
					double[] insertdouble = { 1 };
					oddsList.add(insertdouble);
				}

			}

		}
		return oddsList;
	}
	
	/**
	 * 获取赔率List
	 */

	public List<double[]> getOddsList(List<Info> listInfo, boolean flag) {
		List<double[]> oddsList = new ArrayList<double[]>();/* 赛事选择后的赔率列表 */
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				try {
					double[] aa = new double[3];
					if (info.isWin()) {
						aa[0] = Float.parseFloat(info.getLetV3Win());
					}
					if (info.isLevel()) {
						aa[1] = Float.parseFloat(info.getLetV1Level());
					}
					if (info.isFail()) {
						aa[2] = Float.parseFloat(info.getLetV0Fail());
					}
					double[] insertdouble = PublicMethod
							.getDoubleArrayNoZero(aa);
					oddsList.add(insertdouble);
				} catch (NumberFormatException e) {
					double[] insertdouble = { 1 };
					oddsList.add(insertdouble);
				}

			}

		}
		return oddsList;
	}
}
