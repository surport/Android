package com.ruyicai.android.controller.activity.splash;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.inject.Inject;
import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.activity.functions.FunctionsActivity;
import com.ruyicai.android.controller.activity.home.HomeActivity;

import static com.ruyicai.android.controller.compontent.dialog.DialogType.*;

import com.ruyicai.android.model.bean.PhoneInfo;
import com.ruyicai.android.model.bean.SoftWareInfo;
import com.ruyicai.android.model.preferences.AppSharedPreferences;
import com.ruyicai.android.tools.ImageTools;

/**
 * 该类负责应用程序启动页面的显示，实现了如下功能： 1.显示企业Logo； 2.检查手机网络连接状态； 3.自动跳转到下一个页面；
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-22
 */
public class SplashActivity extends BaseActivity {
	private static final String	TAG						= "SplashActivity";

	/** 页面自动跳转前，Logo显示时间（毫秒) */
	private static final int	LOGO_SHOW_TIME			= 3000;

	/** 软件更新消息ID */
	private static final int	SOFTWARE_UPDATE_MESSAGE	= 5;

	/** 手机信息对象 */
	private PhoneInfo			_fPhoneInfo;
	/** 软件信息对象 */
	private SoftWareInfo		_fSoftWareInfo;
	/** 应用程序共享参数对象 */
	AppSharedPreferences		_fAppSharedPreferences;

	/** 引用视图：显示企业Logo */
	@InjectView(R.id.splash_imageview_logo)
	private ImageView			_fLogoImageView;

	/** 引用对象：WindowManager */
	@Inject
	WindowManager				_fWindowManager;

	private Handler				_fHandler				= new Handler() {

															public void handleMessage(
																	Message msg) {
																switch (msg.what) {
																case SOFTWARE_UPDATE_MESSAGE:
																	// 软件更新，显示软件更新提示对话框
																	showDialog(SOFTWARE_UPDATE_DIALOG._fDialogNumber);
																	break;
																}
															};
														};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		_fAppSharedPreferences = new AppSharedPreferences(this);
		_fPhoneInfo = PhoneInfo.getInstance(this);

		// 初始化Logo图片的显示
		initLogoImageShow();
	};

	@Override
	protected void onStart() {
		super.onStart();

		// 页面启动或者从网络设置页面恢复的时候，都需要重新检查网络状态
		if (!_fPhoneInfo.isConnectedIntenet()) {
			showDialog(NOTCONNECTED_INTENET_DIALOG._fDialogNumber);
		} else {
			// 如果网络有效，则检查软件更新
			// FIXME 为了测试快捷省去了联网请求的步骤
			// checkSoftWareUpdate();
			goToNextScreen();
		}
	}

	/**
	 * 页面退出的时候保存已经第一次启动标识
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// 是否是第一次启动，如果是，则保存已经第一次启动过标识
		if (getFirstLaunchedFlag()) {
			setFirstLaunchedFlag();
		}
	}

	/**
	 * 设置显示的Logo图片
	 */
	private void initLogoImageShow() {
		// 显示启动Logo图片
		int screenWidth = _fWindowManager.getDefaultDisplay().getWidth();
		int screenHeight = _fWindowManager.getDefaultDisplay().getHeight();

		Bitmap logoBitmap = ImageTools
				.scaleBitmapFromResourceBaseDestinationSize(getResources(),
						R.drawable.splash_imageview_logo, screenWidth,
						screenHeight);

		_fLogoImageView.setImageBitmap(logoBitmap);
	}


	/**
	 * 跳转下一个页面：Logo显示3000毫秒，如果是第一启动，则跳转新功能展示界面；如果不是，则跳转主界面。
	 */
	public void goToNextScreen() {
		_fHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (getFirstLaunchedFlag()) {
					goToFunctionsScree();
				} else {
					goToHomeScreen();
				}
			}
		}, LOGO_SHOW_TIME);
	}

	/**
	 * 获取第一次启动的标识
	 * 
	 * @return 是否是第一次启动标识
	 */
	private boolean getFirstLaunchedFlag() {
		boolean isFirst = _fAppSharedPreferences.getBoolean(
				AppSharedPreferences.FIRST_LAUNCHER_KEY, true);
		return isFirst;
	}

	/**
	 * 设置已经第一启动过标识
	 */
	private void setFirstLaunchedFlag() {
		_fAppSharedPreferences.putBoolean(
				AppSharedPreferences.FIRST_LAUNCHER_KEY, false);
	}

	/**
	 * 跳转到主页面
	 */
	private void goToHomeScreen() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 跳转到新功能显示界面
	 */
	private void goToFunctionsScree() {
		Intent intent = new Intent(this, FunctionsActivity.class);
		startActivity(intent);
		finish();
	}
}