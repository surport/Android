package com.ruyicai.code.jc.lq;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.ruyicai.activity.buy.jc.JcMainView.Info;
import com.ruyicai.activity.buy.jc.lq.view.SfView;
import com.ruyicai.code.jc.JcType;
import com.ruyicai.util.PublicMethod;

/**
 * 胜负
 * 
 * @author Administrator
 * 
 */
public class BasketSF {
	JcType jcType;

	public BasketSF(Context context) {
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
		codeStr = codeType + "@" + danCode + code;
		return codeStr;
	}

	/**
	 * 获取赔率List
	 */

	public List<double[]> getOddsList(List<Info> listInfo, int type) {
		List<double[]> oddsList = new ArrayList<double[]>();/* 赛事选择后的赔率列表 */
		for (int i = 0; i < listInfo.size(); i++) {
			Info info = (Info) listInfo.get(i);
			if (info.onclikNum > 0) {
				try {
					double[] aa = new double[2];
					if (info.isWin()) {
						if (type == SfView.B_RF) {
							aa[0] = Float.parseFloat(info.getLetWin());
						} else {
							aa[0] = Float.parseFloat(info.getWin());
						}
					}
					if (info.isFail()) {
						if (type == SfView.B_RF) {
							aa[1] = Float.parseFloat(info.getLetFail());
						} else {
							aa[1] = Float.parseFloat(info.getFail());
						}
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
