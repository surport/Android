package com.ruyicai.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		Intent prizeService = new Intent(context,
				PrizeNotificationService.class);
		context.startService(prizeService);
	}

}
