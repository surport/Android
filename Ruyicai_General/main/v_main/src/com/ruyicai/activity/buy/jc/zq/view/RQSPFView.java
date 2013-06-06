package com.ruyicai.activity.buy.jc.zq.view;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

public class RQSPFView extends SPfView {

	public RQSPFView(Context context, BetAndGiftPojo betAndGift,
			Handler handler, LinearLayout layout, String type,
			boolean isdanguan, List<String> checkTeam) {
		super(context, betAndGift, handler, layout, type, isdanguan, checkTeam);
	}

	@Override
	public String getLotno() {
		return Constants.LOTNO_JCZQ_RQSPF;
	}
}
