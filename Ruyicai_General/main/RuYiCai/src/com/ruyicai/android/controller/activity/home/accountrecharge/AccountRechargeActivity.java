package com.ruyicai.android.controller.activity.home.accountrecharge;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;
import com.ruyicai.android.controller.activity.loginorregister.LoginActivity;
import com.ruyicai.android.controller.adapter.listview.DescriptionListViewAdapter;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;

public class AccountRechargeActivity extends BaseActivity {

	/** 引用视图:账户充值标题栏 */
	@InjectView(R.id.accountrecharge_title_bar)
	private TitleBar	_fTitleBar;
	/** 引用视图：充值方式列表 */
	@InjectView(R.id.accountrecharge_listview_rechargemethods)
	private ListView	_fRechargeMethodListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountercharge_activity);

		_fTitleBar
				.set_fTitleBarInterface(new AccountRechargeTitleBarInterface());
		_fTitleBar.initTitleBarShow();

		int[] iconResourceIds = getIconResourceIds();
		int[] itemStringResourceIds = getItemStringResourceIds();
		int[] descriptionResourceIds = getDescriptionResourceIds();

		DescriptionListViewAdapter descriptionListViewAdapter = new DescriptionListViewAdapter(
				this, iconResourceIds, itemStringResourceIds,
				descriptionResourceIds);
		_fRechargeMethodListView.setAdapter(descriptionListViewAdapter);
	}

	/**
	 * 获取选项描述字符串字段Id数组
	 * 
	 * @return 字符串资源Id数组
	 */
	private int[] getDescriptionResourceIds() {
		int[] descriptionResourceIds = new int[8];

		descriptionResourceIds[0] = R.string.accountrecharge_listitem_alipaysecuritydescription;
		descriptionResourceIds[1] = R.string.accountrecharge_listitem_alipaydescription;
		descriptionResourceIds[2] = R.string.accountrecharge_listitem_unionpaydescription;
		descriptionResourceIds[3] = R.string.accountrecharge_listitem_unionpayvoicedescription;
		descriptionResourceIds[4] = R.string.accountrecharge_listitem_lacarradescription;
		descriptionResourceIds[5] = R.string.accountrecharge_listitem_phonerechargecarddescription;
		descriptionResourceIds[6] = R.string.accountrecharge_listitem_bankrechargedescription;
		descriptionResourceIds[7] = R.string.accountrecharge_listitem_banktransferdescription;

		return descriptionResourceIds;
	}

	/**
	 * 获取选项字符串资源Id数组
	 * 
	 * @return 字符串资源Id数组
	 */
	private int[] getItemStringResourceIds() {
		int[] itemStringResourceIds = new int[8];

		itemStringResourceIds[0] = R.string.accountrecharge_listitem_alipaysecurity;
		itemStringResourceIds[1] = R.string.accountrecharge_listitem_alipay;
		;
		itemStringResourceIds[2] = R.string.accountrecharge_listitem_unionpay;
		itemStringResourceIds[3] = R.string.accountrecharge_listitem_unionpayvoice;
		itemStringResourceIds[4] = R.string.accountrecharge_listitem_lacarra;
		itemStringResourceIds[5] = R.string.accountrecharge_listitem_phonerechargecard;
		itemStringResourceIds[6] = R.string.accountrecharge_listitem_bankrecharge;
		itemStringResourceIds[7] = R.string.accountrecharee_listitem_banktransfer;

		return itemStringResourceIds;
	}

	/**
	 * 获取图标资源Id数组
	 * 
	 * @return 图标资源Id数组
	 */
	private int[] getIconResourceIds() {
		int[] iconResourceIds = new int[8];

		iconResourceIds[0] = R.drawable.accountrecharge_imageview_alipaysecurity;
		iconResourceIds[1] = R.drawable.accountrecharge_imageview_alipay;
		iconResourceIds[2] = R.drawable.accountrecharge_imageview_unionbankrecharege;
		iconResourceIds[3] = R.drawable.accountrecharge_imageview_unionbankvoice;
		iconResourceIds[4] = R.drawable.accountrecharge_imageview_lacarra;
		iconResourceIds[5] = R.drawable.accountrecharge_imageview_phonerechargecard;
		iconResourceIds[6] = R.drawable.accountrecharge_imageview_bankrecharge;
		iconResourceIds[7] = R.drawable.accountrecharge_imageview_banktransfer;

		return iconResourceIds;
	}

	/**
	 * 账户充值实现标题栏接口类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-9
	 */
	class AccountRechargeTitleBarInterface implements TitleBarInterface {
		@Override
		public void setTitleTextView() {
			_fTitleBar._fTitleTextView
					.setText(R.string.accountrecharge_titlebar_text);
		}

		@Override
		public void setTitleButton() {
			_fTitleBar._fLoginOrRegistButton.setVisibility(View.VISIBLE);
			_fTitleBar._fLoginOrRegistButton
					.setText(R.string.buylotteryhall_titlebar_buttontext);
			_fTitleBar._fLoginOrRegistButton
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									AccountRechargeActivity.this,
									LoginActivity.class);
							startActivity(intent);
						}
					});
		}
	}

}
