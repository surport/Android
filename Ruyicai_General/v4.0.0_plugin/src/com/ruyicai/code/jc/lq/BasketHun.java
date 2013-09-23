package com.ruyicai.code.jc.lq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

public class BasketHun {
	// 胜负 //让分胜负
	private static final String SF_WIN = "3";
	private static final String SF_FAIL = "0";

	// 大小分
	private static final String DX_D = "1";
	private static final String DX_X = "2";
	// 胜分差
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
	/**modify by pengcx 20130801 start*/
	public static String bqcType[] = { SF_FAIL,SF_WIN,SF_FAIL, "",SF_WIN,
			DX_D, "", DX_X, BFC_01, BFC_02, BFC_03, BFC_04, BFC_05, BFC_06,
			BFC_11, BFC_12, BFC_13, BFC_14, BFC_15, BFC_16 };
	public static String titleStrs[] = { "客胜", "主胜", "客胜", "", "主胜", "大", "",
			"小", "主胜1-5分", "主胜6-10分", "主胜11-15分", "主胜16-20分", "主胜21-25分",
			"主胜26分以上", "主负1-5分", "主负6-10分", "主负11-15分", "主负16-20分", "主负21-25分",
			"主负26分以上" };
	/**modify by pengcx 20130801 end*/
	JcType jcType;

	public BasketHun(Context context) {
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
				code += getPlayCode(info, Constants.LOTNO_JCLQ, 0, 1);// 胜负
				code += getPlayCode(info, Constants.LOTNO_JCLQ_RF, 2, 4);// 让分胜负
				code += getPlayCode(info, Constants.LOTNO_JCLQ_DXF, 5, 7);// 大小分
				code += getPlayCode(info, Constants.LOTNO_JCLQ_SFC, 8, 19);// 胜分差
			}

		}
		codeStr = codeType + "@" + code;
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
					double[] aa = new double[4];
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
		if (0 <= index && index <= 1) {
			if (odds[0] < odd) {
				odds[0] = odd;
			}
		} else if (2 <= index && index <= 4) {
			if (odds[1] < odd) {
				odds[1] = odd;
			}
		} else if (5 <= index && index <= 7) {
			if (odds[2] < odd) {
				odds[2] = odd;
			}
		} else if (8 <= index && index <= 19) {
			if (odds[3] < odd) {
				odds[3] = odd;
			}
		}
	}
}
