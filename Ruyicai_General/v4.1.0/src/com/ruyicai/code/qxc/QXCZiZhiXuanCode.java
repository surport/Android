package com.ruyicai.code.qxc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class QXCZiZhiXuanCode extends CodeInterface {

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {

		String strZM = "";

		int[] zhuma_firstwei = areaNums[0].table.getHighlightBallNOs();
		int[] zhuma_secondwei = areaNums[1].table.getHighlightBallNOs();
		int[] zhuma_thirdwei = areaNums[2].table.getHighlightBallNOs();
		int[] zhuma_fourthwei = areaNums[3].table.getHighlightBallNOs();
		int[] zhuma_fifthwei = areaNums[4].table.getHighlightBallNOs();
		int[] zhuma_sixthwei = areaNums[5].table.getHighlightBallNOs();
		int[] zhuma_seventhwei = areaNums[6].table.getHighlightBallNOs();

		if (zhuma_firstwei.length > 0 && zhuma_secondwei.length > 0
				&& zhuma_thirdwei.length > 0 && zhuma_fourthwei.length > 0
				&& zhuma_fifthwei.length > 0 && zhuma_sixthwei.length > 0
				&& zhuma_seventhwei.length > 0) {
			for (int i = 0; i < zhuma_firstwei.length; i++) {
				strZM += (zhuma_firstwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_secondwei.length; i++) {
				strZM += (zhuma_secondwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_thirdwei.length; i++) {
				strZM += (zhuma_thirdwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_fourthwei.length; i++) {
				strZM += (zhuma_fourthwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_fifthwei.length; i++) {
				strZM += (zhuma_fifthwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_sixthwei.length; i++) {
				strZM += (zhuma_sixthwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_seventhwei.length; i++) {
				strZM += (zhuma_seventhwei[i]) + "";
			}

		}
		return strZM;

	}

}
