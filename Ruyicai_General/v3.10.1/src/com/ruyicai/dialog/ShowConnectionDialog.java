/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.ruyicai.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.home.HomeActivity;
import com.ruyicai.util.RWSharedPreferences;

public class ShowConnectionDialog extends Activity {

	Context ctx;
	private RWSharedPreferences shellRW;
	boolean hintPreference = false;
	HomeActivity iHomePage;

	boolean isHint() {
		return hintPreference;
	}

	/* 构造函数 */

	public ShowConnectionDialog(Context context, HomeActivity aHomePage,
			RWSharedPreferences shellRW) {
		ctx = context;
		iHomePage = aHomePage;
		this.shellRW = shellRW;
	}

	public void showConnectionDialog(Context ctx1) {
		final Context ctx = ctx1;
		LinearLayout layout = new LinearLayout(ctx);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		TextView hint = new TextView(ctx);
		hint.setText(R.string.connect_hint);
		hint.setTextAppearance(ctx, android.R.style.TextAppearance_Medium);
		CheckBox checkBox = new CheckBox(ctx);
		checkBox.setText(R.string.no_hint);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				hintPreference = isChecked;
			}
		});
		layout.addView(hint, lp);
		layout.addView(checkBox, lp);

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		shellRW = new RWSharedPreferences(ctx, "addInfo");
		builder.setCancelable(true);
		builder.setTitle(R.string.tishi); // 周黎鸣 7.7 代码修改：增加网络提示的标题

		builder.setView(layout);

		// //调用preferense 接口

		// 周黎鸣 7.7 代码修改：将“继续”改为“确定”
		builder.setNegativeButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						HintCheck(hintPreference);
						Message mg = Message.obtain();
						mg.what = 2;

						iHomePage.mHandler.sendMessage(mg);
					}
				});
		builder.setPositiveButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						hintPreference = false;
						HintCheck(hintPreference);
						iHomePage.finish();
						return;
					}
				});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				iHomePage.finish();
				return;
			}
		});
		builder.show();

	}

	private void HintCheck(boolean isChecked) {
		if (isChecked) {

			shellRW.putStringValue("noHint", "true");

		} else {
			shellRW.putStringValue("noHint", "false");
		}
	}

}
