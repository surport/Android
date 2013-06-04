/**
 * 
 */
package com.ruyicai.code.ssq;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

/**
 * 双色球自选直选注码类
 * @author Administrator
 *
 */
public class SsqZiZhiXuanCode extends CodeInterface{

	@Override
	public String zhuma(AreaNum areaNums[],int beishu,int code) {
		int zhuma[] = null;
		int iRedHighlights = areaNums[0].table.getHighlightBallNums();
		int iBlueHighlights =areaNums[1].table.getHighlightBallNums();
		zhuma =  areaNums[0].table.getHighlightBallNOs();
		int zhumablue[] = areaNums[1].table.getHighlightBallNOs();
		String t_str = "";
		if (iRedHighlights > 6 && iBlueHighlights == 1) {
			t_str += "10";
		} else if (iBlueHighlights > 1 && iRedHighlights == 6) {
			t_str += "20";
		} else if (iRedHighlights > 6 && iBlueHighlights > 1) {
			t_str += "30";
		} else if(iBlueHighlights == 1 && iRedHighlights == 6){
			t_str += "00";
		}
		t_str += "01";
		if(iBlueHighlights != 1 || iRedHighlights != 6){
			t_str += "*";
		}
		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}

		if (zhumablue[0] >= 10) {
			t_str += "~" + zhumablue[0];
		} else if (zhumablue[0] < 10) {
			t_str += "~0" + zhumablue[0];
		}
		for (int j = 1; j < zhumablue.length; j++) {
			if (zhumablue[j] >= 10) {
				t_str += +zhumablue[j];
			} else if (zhumablue[j] < 10) {
				t_str += "0" + zhumablue[j];
			}
		}
		t_str += "^";

		return t_str;
		
	}


}
