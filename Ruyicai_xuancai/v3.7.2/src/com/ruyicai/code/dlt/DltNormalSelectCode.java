package com.ruyicai.code.dlt;

import java.util.List;

import android.util.Log;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class DltNormalSelectCode extends CodeInterface {

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		StringBuffer normalCode = new StringBuffer();
		int redNum[] = areaNums[0].table.getHighlightBallNOs();
		for (int i = 0; i < redNum.length; i++) {
			if (i == redNum.length - 1) {
				normalCode.append(formatInteger(redNum[i])).append("-");
			} else {
				normalCode.append(formatInteger(redNum[i])).append(" ");
			}
		}
		int blueNum[] = areaNums[1].table.getHighlightBallNOs();

		for (int j = 0; j < blueNum.length; j++) {
			if (j == blueNum.length - 1) {
				normalCode.append(formatInteger(blueNum[j]));
			} else {
				normalCode.append(formatInteger(blueNum[j])).append(" ");
			}
		}
		return normalCode.toString();
	}

	public static String simulateZhuma(List<Integer> reds, List<Integer> blues) {
		StringBuffer normalCode = new StringBuffer();
		for (int i = 0; i < reds.size(); i++) {
			if (i == reds.size() - 1) {
				normalCode.append(formatInteger(reds.get(i))).append("-");
			} else {
				normalCode.append(formatInteger(reds.get(i))).append(" ");
			}
		}

		for (int j = 0; j < blues.size(); j++) {
			if (j == blues.size() - 1) {
				normalCode.append(formatInteger(blues.get(j)));
			} else {
				normalCode.append(formatInteger(blues.get(j))).append(" ");
			}
		}
		return normalCode.toString();
	}

}
