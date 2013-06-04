package com.ruyicai.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.handler.MyDialogListener;

public class WhetherQuitDialog extends AlertDialog implements OnClickListener {
	private Button okButton;
	private Button cancelButton;
	MyDialogListener iListener;

	public WhetherQuitDialog(Context context) {
		super(context);
	
	}

	public WhetherQuitDialog(Context context, MyDialogListener aListener) {
		super(context);
	
		this.iListener = aListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

//		okButton = (Button) findViewById(R.id.okButton);
//		cancelButton = (Button) findViewById(R.id.cancelButton);
//		okButton.setOnClickListener(this);
//		cancelButton.setOnClickListener(this);
		setTitle(R.string.app_name);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		iListener.onCancelClick();
		cancel();
		return false;
	}

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.okButton:
			iListener.onOkClick(null);
			dismiss();
			break;
		case R.id.cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		}
	}
}
