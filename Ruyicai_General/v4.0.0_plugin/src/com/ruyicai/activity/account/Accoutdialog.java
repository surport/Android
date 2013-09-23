package com.ruyicai.activity.account;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.palmdream.RuyicaiAndroid.R;

public class Accoutdialog {

	private static Accoutdialog instance = null;

	private Accoutdialog() {

	}

	public synchronized static Accoutdialog getInstance() {
		if (instance == null) {
			instance = new Accoutdialog();
		}
		return instance;

	}

	public void createAccoutdialog(final Context context, String msg) {
		Builder dialog = new AlertDialog.Builder(context)
				.setTitle("充值提示")
				.setMessage(msg)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								Intent intent = new Intent(context,
										AccountListActivity.class);
								intent.putExtra("isonKey", "fasle");
								context.startActivity(intent);
							}
						});
		dialog.show();
	}
}
