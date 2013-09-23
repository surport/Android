package com.ruyicai.android.model.intenet.home.buylotteryhall;

import org.json.JSONObject;

import android.content.Context;

import com.ruyicai.android.model.intenet.BaseIntenetInterface;

/**
 * 历史开奖接口
 * 
 * @author Administrator
 * @since RYC1.0 2013-8-30
 */
public class HistoryOfLotteryInterface extends BaseIntenetInterface {

	public HistoryOfLotteryInterface(boolean aAddPhoneSIM, boolean aAddMac) {
		super(aAddPhoneSIM, aAddMac);
	}

	@Override
	public JSONObject setParticularParamerters(Context aContext,
			JSONObject aJsonObject) {
		return null;
	}

}
