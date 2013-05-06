package com.ruyicai.dialog;

import com.ruyicai.activity.usercenter.BindIDActivity;
import com.ruyicai.activity.usercenter.NewUserCenter;

import android.app.Activity;
import android.content.Intent;

public class MessageDialog extends BaseDialog {
	/**
	 * 首页活动提示框
	 * 
	 * @param activity
	 * @param title
	 * @param message
	 */
	/*add by yejc 20130506 start*/
	Activity activity;
	public static final String JUMP_FLAG = "flag";
	/*add by yejc 20130506 end*/
	public MessageDialog(Activity activity, String title, String message) {
		super(activity, title, message);
		this.activity = activity; //add by yejc 20130506
	}

	@Override
	public void onOkButton() {
		/*add by yejc 20130506 start*/
		if (activity instanceof NewUserCenter) {
			Intent intent = new Intent(activity,
					BindIDActivity.class);
			intent.putExtra(JUMP_FLAG, JUMP_FLAG);
			activity.startActivity(intent);
		}
		/*add by yejc 20130506 end*/
		dialog.cancel();
	}

	@Override
	public void onCancelButton() {
		dialog.cancel();
	}

}
