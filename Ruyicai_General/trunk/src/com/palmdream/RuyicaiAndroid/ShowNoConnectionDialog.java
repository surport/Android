/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.palmdream.RuyicaiAndroid;

import com.palmdream.RuyicaiAndroid.R;
import com.palmdream.RuyicaiAndroid.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

public class ShowNoConnectionDialog extends Activity {

	Context ctx;
	HomePage iParent;

	/* ¹¹Ôìº¯Êý */
	ShowNoConnectionDialog(Context context, HomePage aParent) {
		ctx = context;
		iParent = aParent;
	}

	public void showNoConnectionDialog() {
		// final Context ctx = ctx1;
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setCancelable(true);
		builder.setMessage(R.string.no_connection);
		builder.setTitle(R.string.no_connection_title);
		builder.setPositiveButton(R.string.settings,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ctx.startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
						iParent.finish();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						iParent.finish();
						return;
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				iParent.finish();
				return;
			}
		});

		builder.show();
	}

}
