/**
 * 
 */
package com.ruyicai.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.account.Accoutdialog;
import com.ruyicai.activity.buy.InsufficientBalanceActivity;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameIndentActivity;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.miss.ZiXuanTouZhu;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

/**
 * 自定义handler
 * 
 * @author Administrator
 * 
 */
public class MyHandler extends Handler {
	public static final int RUYICAIHANDLER = 1;
	HandlerMsg msg;
	Context context;
	private BetAndGiftPojo betAndGift;

	public MyHandler(HandlerMsg msg) {
		this.msg = msg;
		context = this.msg.getContext();
	}

	public void setBetAndGift(BetAndGiftPojo betAndGift) {
		this.betAndGift = betAndGift;
	}

	public void handleMessage(Message msg) {

		String error_code = msg.getData().getString("error_code");
		String message = msg.getData().getString("message");
		switch (msg.what) {
		case RUYICAIHANDLER:
			if (error_code.equals("000000")) {
				this.msg.errorCode_000000();
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("9999")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("000072")) {
				Intent intent = new Intent(context, UserLogin.class);
				context.startActivity(intent);
			} else if (error_code.equals("001200")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("001300")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("001400")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("001500")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("000045")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("000047")) {
				// this.msg.errorcode_000047();
			} else if (error_code.equals("000005")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("0000")) {
				// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				this.msg.errorCode_0000();
			} else if (error_code.equals("0407")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("0047")) {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("0")) {
				Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("00")) {
				Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
			} else if (error_code.equals("0406")) {
				if (context instanceof ZiXuanTouZhu
						|| context instanceof com.ruyicai.activity.buy.zixuan.ZiXuanTouZhu
						|| context instanceof JcMainActivity
						|| context instanceof BeiJingSingleGameIndentActivity) {
					Intent intent = new Intent(context,
							InsufficientBalanceActivity.class);
					intent.putExtra("lotno", betAndGift.getLotno());
					intent.putExtra("amount", betAndGift.getAmount());
					context.startActivity(intent);
				} else {
					Accoutdialog.getInstance().createAccoutdialog(
							context,
							context.getResources()
									.getString(
											R.string.goucai_Account_dialog_msg)
									.toString());
				}
			} else {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	/**
	 * 
	 * 传递msg信息
	 * 
	 */
	public void handleMsg(String error_code, String message) {
		if (message == null) {
			message = "";
		}
		Bundle data = new Bundle();
		data.putString("error_code", error_code);
		data.putString("message", message);
		Message msg = this.obtainMessage();
		msg.setData(data);
		msg.what = RUYICAIHANDLER;
		this.sendMessage(msg);
	}

	public void handleMegTC(String[] ecode) {
		Message msg = new Message();

	}

}
