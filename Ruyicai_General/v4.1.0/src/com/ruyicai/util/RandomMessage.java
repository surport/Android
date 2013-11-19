package com.ruyicai.util;

import java.util.Random;

import android.R.integer;
import android.util.Log;

public class RandomMessage {

	/**
	 * 随机获取等待提示语
	 * 
	 * @return
	 */
	public String getWaitingMessage() {
		Random r = new Random();
		String numTemp = String.valueOf(r.nextFloat());
		int num = Integer.parseInt(numTemp.substring(5, 6));
		if (num == 0) {
			return "看灰机,灰过去了耶...";
		} else if (num == 1) {
			return "不管你信不信,反正我是信了,等待不是漫长滴...";
		} else if (num == 2) {
			return "使用客户端还可以给您的好友赠送彩票...";
		} else if (num == 3) {
			return "是等呢,还是等呢,还是等呢...";
		} else if (num == 4) {
			return "中大奖后可以把部分奖金捐赠给需要的人...";
		} else if (num == 5) {
			return "参与合买可以提高您的中奖几率...";
		} else if (num == 6) {
			return "研究一下彩票资讯,里面有惊喜哦...";
		} else if (num == 7) {
			return "如意彩助您中大奖...500万从此不是梦...";
		} else if (num == 8) {
			return "如意彩是您手中的购彩专家...";
		} else if (num == 9) {
			return "如意彩助您中大奖...500万从此不是梦...";
		} else if (num == 10) {
			return "如意彩助您中大奖...500万从此不是梦...";
		}

		return "";
	}

}
