package com.ruyicai.dialog;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RuyicaiActivityManager;

/**
 * 专家荐号框
 * 
 * @author Administrator
 * 
 */
public class ExpertDialog extends BaseDialog {
	String msg;
	String toPhone;
	Handler handler;

	public ExpertDialog(Activity activity, String title, String message,
			String msg, String toPhone, Handler handler) {
		super(activity, title, message);
		// TODO Auto-generated constructor stub
		this.msg = msg;
		this.toPhone = toPhone;
		this.handler = handler;
	}

	@Override
	public void onOkButton() {
		// TODO Auto-generated method stub
		sendSMS();
	}

	@Override
	public void onCancelButton() {
		// TODO Auto-generated method stub

	}

	/**
	 * 发短信方法
	 * 
	 */
	private void sendSMS() {
		new Thread() {
			public void run() {
				boolean sendOk = PublicMethod.sendSMS(toPhone, msg);// (String)iNumbers.elementAt(i));//
				if (sendOk) {
					handler.post(new Runnable() {
						public void run() {
							Toast.makeText(activity, "发送短信成功！",
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							Toast.makeText(activity, "发送短信失败！",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}.start();
	}
}
