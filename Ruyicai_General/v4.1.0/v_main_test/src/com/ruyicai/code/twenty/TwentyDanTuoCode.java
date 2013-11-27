package com.ruyicai.code.twenty;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class TwentyDanTuoCode extends CodeInterface {

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub

		int zhuma[] = areaNums[0].table.getHighlightBallNOs();
		int[] tuozhuma = areaNums[1].table.getHighlightBallNOs();
		String t_str = "2@";
		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}
		t_str += "*";
		for (int j = 0; j < tuozhuma.length; j++) {
			if (tuozhuma[j] >= 10) {
				t_str += tuozhuma[j];
			} else if (tuozhuma[j] < 10) {
				t_str += "0" + tuozhuma[j];
			}
		}
		t_str += "^";
		return t_str;
	}

}