package com.ruyicai.android.controller.compontent.dialog.prompt;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.splash.SplashActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 软件更新对话框：软件有新的版本更新的时候退出对话框
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-30
 */
public class SoftUpdateDialog extends PromptDialog {

	public SoftUpdateDialog(Context context) {
		super(context);
	}

	@Override
	public void set_fTitleString() {
		_fTitleString = _fResources
				.getString(R.string.updatedialog_title_prompt);
	}

	@Override
	public void set_fContentView() {
		_fContentView = new TextView(_fContext);
		((TextView) _fContentView).setTextColor(R.color.black);
		((TextView) _fContentView).setTextSize(18);
		((TextView) _fContentView)
				.setText(R.string.updatedialog_message_softwareupdate);
	}

	@Override
	public void set_fPositiveButton() {
		_fPositiveButtonString = _fResources
				.getString(R.string.updatedialog_button_positive);
		_fPositiveButtonClickListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(_fContext, "是的", Toast.LENGTH_LONG).show();
			}
		};
	}

	@Override
	public void set_fNegativeButton() {
		_fNegativeButtonString = _fResources
				.getString(R.string.updatedialog_button_negative);
		_fNegativeButtOnClickListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
				((SplashActivity) _fContext).goToNextScreen();
			}
		};
	}

	@Override
	public void set_fOtherButton() {
		// 没有该按钮，则空实现
	}
}
