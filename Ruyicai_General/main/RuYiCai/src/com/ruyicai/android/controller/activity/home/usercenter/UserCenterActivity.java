package com.ruyicai.android.controller.activity.home.usercenter;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.activity.loginorregister.LoginActivity;
import com.ruyicai.android.controller.activity.loginorregister.RegisterActivity;
import com.ruyicai.android.controller.adapter.listview.IconListViewAdapter;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;

public class UserCenterActivity extends BaseActivity {
	/** 视图引用：用户中心标题栏 */
	@InjectView(R.id.usercenter_titile_bar)
	private TitleBar	_fTitleBar;
	/** 视图引用：账户资金列表 */
	@InjectView(R.id.usercenter_listview_accountcapital)
	private ListView	_fAccountCapitaListView;
	/** 视图引用：我的彩票列表 */
	@InjectView(R.id.usercenter_listview_mylottery)
	private ListView	_fMyLotteryListView;
	/** 视图引用：账户设置列表 */
	@InjectView(R.id.usercenter_listview_accountset)
	private ListView	_fAccountSetListView;
	/** 视图引用：登录按钮 */
	@InjectView(R.id.usercenter_button_login)
	private Button		_fLoginButton;
	/** 视图引用：注册按钮 */
	@InjectView(R.id.usercenter_button_register)
	private Button		_fRegisterButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercenter_activity);

		_fTitleBar.set_fTitleBarInterface(new UserCenterTitleBarInterface());
		_fTitleBar.initTitleBarShow();

		// 初始化登录区域的显示
		initLoginAreaShow();

		// 初始化账户资金列表的显示
		initAccountCapitalListViewShow();

		// 初始化我的彩票列表的显示
		initMyLotteryListViewShow();

		// 初始化账户设置列表的显示
		initAccountSetListViewShow();
	}

	/**
	 * 初始化登录区域的显示
	 */
	private void initLoginAreaShow() {
		ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();
		_fLoginButton.setOnClickListener(buttonOnClickListener);
		_fRegisterButton.setOnClickListener(buttonOnClickListener);
	}

	/**
	 * 初始化账户资金列表的显示
	 */
	private void initAccountCapitalListViewShow() {
		int[] accountCapitalStringIds = getAccountCapitalStringIds();
		int[] accountCapitalIconIds = getAccountCapitalIconIds();
		IconListViewAdapter accountCapticalAdapter = new IconListViewAdapter(
				this, accountCapitalIconIds, accountCapitalStringIds);
		_fAccountCapitaListView.setAdapter(accountCapticalAdapter);
	}

	/**
	 * 初始化我的彩票列表的显示
	 */
	private void initMyLotteryListViewShow() {
		int[] myLotteryStringIds = getMyLotteryStringIds();
		int[] myLotteryIconIds = getMyLotteryIconIds();
		IconListViewAdapter myLotteryAdapter = new IconListViewAdapter(this,
				myLotteryIconIds, myLotteryStringIds);
		_fMyLotteryListView.setAdapter(myLotteryAdapter);
	}

	/**
	 * 初始化账户设置列表的显示
	 */
	private void initAccountSetListViewShow() {
		int[] accountSetStringIds = getAccountSetStringIds();
		int[] accountSetIconIds = getAccountSetIconIds();
		IconListViewAdapter accountSetAdapter = new IconListViewAdapter(this,
				accountSetIconIds, accountSetStringIds);
		_fAccountSetListView.setAdapter(accountSetAdapter);
	}

	/**
	 * 获取账户资金列表的字符串资源Id数组
	 * 
	 * @return 字符串资源Id数组
	 */
	private int[] getAccountCapitalStringIds() {
		int[] accountCapitalStringIds = new int[5];

		accountCapitalStringIds[0] = R.string.usercenter_listviewitem_accountrecharge;
		accountCapitalStringIds[1] = R.string.usercenter_listviewitem_accountwithdraw;
		accountCapitalStringIds[2] = R.string.usercenter_listviewitem_accountdetail;
		accountCapitalStringIds[3] = R.string.usercenter_listviewitem_capticaldetail;
		accountCapitalStringIds[4] = R.string.usercenter_listviewitem_myintegral;

		return accountCapitalStringIds;
	}

	/**
	 * 获取账户资金列表图片资源Id数组
	 * 
	 * @return 图片资源Id数组
	 */
	private int[] getAccountCapitalIconIds() {
		int[] accountCapitalIconIds = new int[5];

		accountCapitalIconIds[0] = R.drawable.usercenter_imageview_accountrecharge;
		accountCapitalIconIds[1] = R.drawable.usercenter_imageview_accountwidthdraw;
		accountCapitalIconIds[2] = R.drawable.usercenter_imageview_accountdetail;
		accountCapitalIconIds[3] = R.drawable.usercenter_imageview_capticaldetail;
		accountCapitalIconIds[4] = R.drawable.usercenter_imageview_myintegral;

		return accountCapitalIconIds;
	}

	/**
	 * 获取我的彩票的字符串资源Id数组
	 * 
	 * @return 字符串资源Id数组
	 */
	private int[] getMyLotteryStringIds() {
		int[] myLotteryStringIds = new int[5];

		myLotteryStringIds[0] = R.string.usercenter_listviewitem_winquery;
		myLotteryStringIds[1] = R.string.usercenter_listviewitem_betrecord;
		myLotteryStringIds[2] = R.string.usercenter_listviewitem_addonquery;
		myLotteryStringIds[3] = R.string.usercenter_listviewitem_presentquery;
		myLotteryStringIds[4] = R.string.usercenter_listviewitem_mychipped;

		return myLotteryStringIds;
	}

	/**
	 * 获取我的彩票列表的图片资源Id数组
	 * 
	 * @return 图片资源Id数组
	 */
	private int[] getMyLotteryIconIds() {
		int[] myLotteryIconIds = new int[5];

		myLotteryIconIds[0] = R.drawable.usercenter_imageview_winquery;
		myLotteryIconIds[1] = R.drawable.usercenter_imageview_betrecord;
		myLotteryIconIds[2] = R.drawable.usercenter_imageview_addonquery;
		myLotteryIconIds[3] = R.drawable.usercenter_imageview_presentquery;
		myLotteryIconIds[4] = R.drawable.usercenter_imageview_mychipped;

		return myLotteryIconIds;
	}

	/**
	 * 获取账户设置列表的图片资源Id数组
	 * 
	 * @return 图片资源Id数组
	 */
	private int[] getAccountSetIconIds() {
		int[] accountSetIconIds = new int[4];

		accountSetIconIds[0] = R.drawable.usercenter_imageview_modifypassword;
		accountSetIconIds[1] = R.drawable.usercenter_imageview_identifycardbind;
		accountSetIconIds[2] = R.drawable.usercenter_imageview_phonenumberbind;
		accountSetIconIds[3] = R.drawable.usercenter_imageview_onlinemessage;

		return accountSetIconIds;
	}

	/**
	 * 获取账户设置列表的字符串资源Id数组
	 * 
	 * @return 字符串资源Id数组
	 */
	private int[] getAccountSetStringIds() {
		int[] accountSetStringIds = new int[4];

		accountSetStringIds[0] = R.string.usercenter_listviewitem_modifypassword;
		accountSetStringIds[1] = R.string.usercenter_listviewitem_identitycardbind;
		accountSetStringIds[2] = R.string.usercenter_listviewitem_phonenumberbind;
		accountSetStringIds[3] = R.string.usercenter_listviewitem_onlinemessage;

		return accountSetStringIds;
	}

	/**
	 * 按钮事件监听类：实现对注册登录页面的按钮事件处理
	 * 
	 * @author Administrator
	 * @since RYC1.0 2013-4-3
	 */
	class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = null;

			switch (v.getId()) {

			case R.id.usercenter_button_login:
				intent = new Intent(UserCenterActivity.this,
						LoginActivity.class);

				break;
			case R.id.usercenter_button_register:
				intent = new Intent(UserCenterActivity.this,
						RegisterActivity.class);
				break;
			}

			startActivity(intent);
		}

	}

	/**
	 * 用户中心实现标题栏接口类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-9
	 */
	class UserCenterTitleBarInterface implements TitleBarInterface {
		@Override
		public void setTitleTextView() {
			_fTitleBar._fTitleTextView
					.setText(R.string.usercenter_titlebar_text);
		}

		@Override
		public void setTitleButton() {
			_fTitleBar._fLoginOrRegistButton.setVisibility(View.GONE);
			_fTitleBar._fLoginOrRegistButton.setEnabled(false);
		}
	}

}
