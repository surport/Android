package com.ruyicai.android.controller.activity.loginorregister;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;

/**
 * 注册页面
 * 
 * @author Administrator
 * @since RYC1.0 2013-4-3
 */
public class RegisterActivity extends BaseActivity {
	/** 引用视图:标题栏 */
	@InjectView(R.id.register_title_bar)
	private TitleBar	_fTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);

		_fTitleBar.set_fTitleBarInterface(new RegisterTitleBarInterface());
		_fTitleBar.initTitleBarShow();
	}

	class RegisterTitleBarInterface implements TitleBarInterface {
		@Override
		public void setTitleTextView() {
			_fTitleBar._fTitleTextView.setText(R.string.register_title_text);
		}

		@Override
		public void setTitleButton() {
			_fTitleBar._fLoginOrRegistButton.setVisibility(View.GONE);
			_fTitleBar._fLoginOrRegistButton.setEnabled(false);
		}
	}

}
