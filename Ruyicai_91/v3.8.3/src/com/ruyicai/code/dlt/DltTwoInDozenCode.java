package com.ruyicai.code.dlt;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class DltTwoInDozenCode extends CodeInterface {

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {

		StringBuffer twoInDozenCode = new StringBuffer();
		int code[] = areaNums[0].table.getHighlightBallNOs();
		for (int i = 0; i < code.length; i++) {
			if (i == code.length - 1) {
				twoInDozenCode.append(formatInteger(code[i]));
			} else {
				twoInDozenCode.append(formatInteger(code[i])).append(" ");
			}
		}

		return twoInDozenCode.toString();
	}

}
