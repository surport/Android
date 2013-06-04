package com.third.sharetorenren;

import android.app.Activity;

import com.third.sharetorenren.status.StatusSetRequestParam;


public class StatusDemo {
	
	
	public  void publishStatusOneClick(final Activity activity, final Renren renren) {
		renren.publishStatus(activity, new StatusSetRequestParam(""));
	}
}
