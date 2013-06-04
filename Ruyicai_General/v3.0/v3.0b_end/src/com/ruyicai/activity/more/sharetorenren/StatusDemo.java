package com.ruyicai.activity.more.sharetorenren;

import android.app.Activity;
import com.ruyicai.activity.more.sharetorenren.status.StatusSetRequestParam;


public class StatusDemo {
	
	
	public static void publishStatusOneClick(final Activity activity, final Renren renren) {
		renren.publishStatus(activity, new StatusSetRequestParam(""));
	}
}
