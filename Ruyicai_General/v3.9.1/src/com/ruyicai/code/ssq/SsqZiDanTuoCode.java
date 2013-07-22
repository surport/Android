/**
 * 
 */
package com.ruyicai.code.ssq;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

/**
 * 双色球自选胆拖注码类
 * 
 * @author Administrator
 * 
 */
public class SsqZiDanTuoCode extends CodeInterface {

	public String zhuma(AreaNum[] areaNums, int beishu, int code) {
		int iBlueHighlights = areaNums[2].table.getHighlightBallNums();
		int zhuma[] = areaNums[0].table.getHighlightBallNOs();
		int[] tuozhuma = areaNums[1].table.getHighlightBallNOs();
		int zhumablue[] = areaNums[2].table.getHighlightBallNOs();
		// String t_str = "1512-F47104-";
		String t_str = "";
		if (iBlueHighlights == 1) {
			// t_str += "40-01-40";
			t_str += "40";
		} else if (iBlueHighlights > 1) {
			// t_str += "50-01-50";
			t_str += "50";
		}
		t_str += "01";
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
		t_str += "~";
		for (int k = 0; k < zhumablue.length; k++) {
			if (zhumablue[k] >= 10) {
				t_str += +zhumablue[k];
			} else if (zhumablue[k] < 10) {
				t_str += "0" + zhumablue[k];
			}
			// t_str+="^";移到外面 陈晨 20100714
		}
		t_str += "^";
		return t_str;
	}

}
