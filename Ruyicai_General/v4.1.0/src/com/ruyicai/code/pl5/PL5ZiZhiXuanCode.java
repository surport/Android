package com.ruyicai.code.pl5;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class PL5ZiZhiXuanCode extends CodeInterface {
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		String strZM = "";
		int[] zhuma_wanwei = areaNums[0].table.getHighlightBallNOs();
		int[] zhuma_qianwei = areaNums[1].table.getHighlightBallNOs();
		int[] zhuma_baiwei = areaNums[2].table.getHighlightBallNOs();
		int[] zhuma_shiwei = areaNums[3].table.getHighlightBallNOs();
		int[] zhuma_gewei = areaNums[4].table.getHighlightBallNOs();

		if (zhuma_wanwei.length > 0 && zhuma_qianwei.length > 0
				&& zhuma_baiwei.length > 0 && zhuma_shiwei.length > 0
				&& zhuma_gewei.length > 0) {
			for (int i = 0; i < zhuma_wanwei.length; i++) {
				strZM += (zhuma_wanwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_qianwei.length; i++) {
				strZM += (zhuma_qianwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_baiwei.length; i++) {
				strZM += (zhuma_baiwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_shiwei.length; i++) {
				strZM += (zhuma_shiwei[i]) + "";
			}
			strZM += ",";
			for (int i = 0; i < zhuma_gewei.length; i++) {
				strZM += (zhuma_gewei[i]) + "";
			}

		}
		return strZM;

	}
}
