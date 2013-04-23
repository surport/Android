package com.ruyicai.dialog;

import com.ruyicai.activity.account.AccountListActivity;
import com.ruyicai.activity.account.DirectPayActivity;
import android.app.Activity;
import android.content.Intent;

/**
 * 直接支付对话框
 * 
 * @author PengCX
 * 
 */
public class DirectPayDialog extends BaseDialog {

	public DirectPayDialog(Activity activity, String title, String message) {
		super(activity, title, message);

	}

	@Override
	public void onOkButton() {
		Intent intent = new Intent(activity, DirectPayActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onCancelButton() {
		Intent intent = new Intent(activity, AccountListActivity.class);
		intent.putExtra("isonKey", "fasle");
		activity.startActivity(intent);
	}

}
