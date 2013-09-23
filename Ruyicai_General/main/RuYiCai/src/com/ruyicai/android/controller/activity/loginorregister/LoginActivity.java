package com.ruyicai.android.controller.activity.loginorregister;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.adapter.listview.IconListViewAdapter;
import com.ruyicai.android.controller.adapter.listview.SimpleListViewAdapter;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;

public class LoginActivity extends BaseActivity {

	/** 视图引用用户登录标题栏 */
	@InjectView(R.id.login_title_bar)
	private TitleBar	_fTitleBar;
	/** 视图引用：引用注册新用户列表 */
	@InjectView(R.id.login_listview_newuserregister)
	private ListView	_fNewUserRegisterListView;
	/** 视图引用：引用微博登陆列表 */
	@InjectView(R.id.login_listview_microbloglogin)
	private ListView	_fMicroBlogLoginListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		_fTitleBar.set_fTitleBarInterface(new LoginTitleBarInterface());
		_fTitleBar.initTitleBarShow();

		// 初始化新用户注册等列表
		initNewUserRegisterListViewShow();

		// 初始化微博登陆列表
		initMicroBlogLoginListViewShow();
	}

	/**
	 * 初始化微博登陆列表显示
	 */
	private void initMicroBlogLoginListViewShow() {
		// FIXME 当前列表的高度是写死的，是否能活动处理
		int[] iconResourceIds = getMicroBlogLoginListIconIds();
		int[] stringResourceIds2 = getMicroBlogLoginListStringIds();
		IconListViewAdapter listViewIcomAdapter = new IconListViewAdapter(this,
				iconResourceIds, stringResourceIds2);
		_fMicroBlogLoginListView.setAdapter(listViewIcomAdapter);
	}

	/**
	 * 初始化新用户注册等列表显示
	 */
	private void initNewUserRegisterListViewShow() {
		// FIXME 当前列表的高度是写死的，是否能活动处理
		int[] stringResourceIds = getNewUserRegisterListStringIds();
		SimpleListViewAdapter listViewAdapter = new SimpleListViewAdapter(this,
				stringResourceIds);
		_fNewUserRegisterListView.setAdapter(listViewAdapter);
	}

	/**
	 * 获取新用户注册列表字符串资源数组
	 * 
	 * @return 字符串资源数组
	 */
	private int[] getNewUserRegisterListStringIds() {
		int[] stringResourceIds = new int[3];

		stringResourceIds[0] = R.string.login_listviewitem_newuserregister;
		stringResourceIds[1] = R.string.login_listviewitem_reterivepassword;
		stringResourceIds[2] = R.string.login_listviewitem_customerhotline;

		return stringResourceIds;
	}

	/**
	 * 获取微博登陆列表的字符串资源Id数组
	 * 
	 * @return 字符串资源Id数组
	 */
	private int[] getMicroBlogLoginListStringIds() {
		int[] stringResourceIds = new int[2];

		stringResourceIds[0] = R.string.login_listviewitem_sinamicroblog;
		stringResourceIds[1] = R.string.login_listviewitem_qqaccountlogin;

		return stringResourceIds;
	}

	/**
	 * 获取微博登陆列表的图标资源Id数组
	 * 
	 * @return 图标资源Id数组
	 */
	private int[] getMicroBlogLoginListIconIds() {
		int[] iconResourceIds = new int[2];

		iconResourceIds[0] = R.drawable.login_imageview_sina;
		iconResourceIds[1] = R.drawable.login_imageview_qq;

		return iconResourceIds;
	}

	/**
	 * 登录页面实现标题栏显示接口类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-9
	 */
	class LoginTitleBarInterface implements TitleBarInterface {
		@Override
		public void setTitleTextView() {
			_fTitleBar._fTitleTextView.setText(R.string.login_title_text);
		}

		@Override
		public void setTitleButton() {
			_fTitleBar._fLoginOrRegistButton.setVisibility(View.GONE);
			_fTitleBar._fLoginOrRegistButton.setEnabled(false);
		}
	}
}
