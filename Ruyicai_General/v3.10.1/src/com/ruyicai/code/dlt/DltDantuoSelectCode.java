package com.ruyicai.code.dlt;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class DltDantuoSelectCode extends CodeInterface {

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {

		// TODO Auto-generated method stub
		StringBuffer dantuoCode = new StringBuffer();
		int frontdan[] = areaNums[0].table.getHighlightBallNOs();
		if (frontdan != null) {
			for (int i = 0; i < frontdan.length; i++) {
				if (i == frontdan.length - 1) {
					dantuoCode.append(formatInteger(frontdan[i])).append("$");
				} else {
					dantuoCode.append(formatInteger(frontdan[i])).append(" ");
				}
			}
		}
		int fronttuo[] = areaNums[1].table.getHighlightBallNOs();
		if (fronttuo != null) {
			for (int i = 0; i < fronttuo.length; i++) {
				if (i == fronttuo.length - 1) {
					dantuoCode.append(formatInteger(fronttuo[i])).append("-");
				} else {
					dantuoCode.append(formatInteger(fronttuo[i])).append(" ");
				}
			}
		}
		int reardan[] = areaNums[2].table.getHighlightBallNOs();
		if (reardan != null) {
			for (int i = 0; i < reardan.length; i++) {
				if (i == reardan.length - 1) {
					dantuoCode.append(formatInteger(reardan[i])).append("$");
				} else {
					dantuoCode.append(formatInteger(reardan[i])).append(" ");
				}
			}
		}
		int reartuo[] = areaNums[3].table.getHighlightBallNOs();
		if (reartuo != null) {
			for (int i = 0; i < reartuo.length; i++) {
				if (i == reartuo.length - 1) {
					dantuoCode.append(formatInteger(reartuo[i]));
				} else {
					dantuoCode.append(formatInteger(reartuo[i])).append(" ");
				}
			}
		}
		return dantuoCode.toString();
	}

}
