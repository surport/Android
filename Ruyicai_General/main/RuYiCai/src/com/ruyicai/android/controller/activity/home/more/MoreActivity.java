package com.ruyicai.android.controller.activity.home.more;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.activity.loginorregister.LoginActivity;
import com.ruyicai.android.controller.adapter.listview.SimpleListViewAdapter;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;

public class MoreActivity extends BaseActivity {

	/** 视图引用：更多标题栏 */
	@InjectView(R.id.more_title_bar)
	private TitleBar	_fTitleBar;
	/** 视图引用：更多列表 */
	@InjectView(R.id.more_listview_items)
	private ListView	_fMoreListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);

		_fTitleBar.set_fTitleBarInterface(new MoreTitleBarInterface());
		_fTitleBar.initTitleBarShow();

		int[] itemsStringResourceIds = getItemsStringResourceIds();
		SimpleListViewAdapter simpleListViewAdapter = new SimpleListViewAdapter(
				this, itemsStringResourceIds);
		_fMoreListView.setAdapter(simpleListViewAdapter);
	}

	/**
	 * 获取更多列表的字符串资源Id数组
	 * 
	 * @return 字符串资源Id数组
	 */
	private int[] getItemsStringResourceIds() {
		int[] itemsStringResourceIds = new int[9];

		itemsStringResourceIds[0] = R.string.more_listitem_customerhotline;
		itemsStringResourceIds[1] = R.string.more_listitem_helpercenter;
		itemsStringResourceIds[2] = R.string.more_listitem_checkupdate;
		itemsStringResourceIds[3] = R.string.more_listitem_freshmanguide;
		itemsStringResourceIds[4] = R.string.more_listitem_share;
		itemsStringResourceIds[5] = R.string.more_listitem_ifeedback;
		itemsStringResourceIds[6] = R.string.more_listitem_aboutwe;
		itemsStringResourceIds[7] = R.string.more_listitem_settting;
		itemsStringResourceIds[8] = R.string.more_listitem_aboutsoftware;

		return itemsStringResourceIds;
	}

	/**
	 * 更多页面实现标题栏显示接口类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-9
	 */
	class MoreTitleBarInterface implements TitleBarInterface {
		@Override
		public void setTitleTextView() {
			_fTitleBar._fTitleTextView.setText(R.string.more_titlebar_text);
		}

		@Override
		public void setTitleButton() {
			_fTitleBar._fLoginOrRegistButton.setVisibility(View.VISIBLE);
			_fTitleBar._fLoginOrRegistButton
					.setText(R.string.more_titlebar_buttontext);
			_fTitleBar._fLoginOrRegistButton
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MoreActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
					});
		}
	}

}
