package com.palmdream.RuyicaiAndroid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.palmdream.netintface.iHttp;

public class WhetherQuitDialog extends Dialog implements OnClickListener {
	private Button okButton;
	private Button cancelButton;
	MyDialogListener iListener;

	public WhetherQuitDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WhetherQuitDialog(Context context, MyDialogListener aListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.iListener = aListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whether_quit_dialog);

		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		setTitle(R.string.app_name);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		iListener.onCancelClick();
		cancel();
		return false;// super.onKeyDown(keyCode, event);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.okButton:
			iListener.onOkClick(null);
			iHttp.conMethord =0;
			iHttp.retValue="";
			dismiss();
			break;
		case R.id.cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		}
	}
}
