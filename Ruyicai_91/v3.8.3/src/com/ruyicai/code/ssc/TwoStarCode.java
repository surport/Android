package com.ruyicai.code.ssc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.constant.Constants;
import com.ruyicai.pojo.AreaNum;

public class TwoStarCode extends CodeInterface {

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		String str = "";
		switch (type) {
		case Constants.SSC_TWOSTAR_ZHIXUAN:
			if (isZHmiss()) {
				str = "2D|";
				str += "-," + "-," + "-," + getIsZHcode();
			} else {
				str = "2D|";
				// TODO Auto-generated method stub
				int shi[] = areaNums[0].table.getHighlightBallNOs();
				int ge[] = areaNums[1].table.getHighlightBallNOs();
				str += "-," + "-," + "-," + getStr(shi) + "," + getStr(ge);
			}
			break;
		case Constants.SSC_TWOSTAR_ZUXUAN:
			if (isZHmiss()) {
				str = "F2|";
				String codeStr = getIsZHcode();
				String codeStrs[] = codeStr.split("\\,");
				for (String codestr : codeStrs) {
					str += codestr;
				}
			} else {
				int[] one = areaNums[0].table.getHighlightBallNOs();
				str = "F2|";
				str += getStr(one);
			}
			break;
		case Constants.SSC_TWOSTAR_HEZHI:
			int[] he = areaNums[0].table.getHighlightBallNOs();
			str = "S2|";
			str += getStrZhuMa(he);
			break;
		}
		return str;
	}

	public String getStr(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
		}
		return str;

	}

	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

	}

}
