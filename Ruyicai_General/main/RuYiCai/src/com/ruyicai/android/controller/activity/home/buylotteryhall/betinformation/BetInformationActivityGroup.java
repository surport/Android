package com.ruyicai.android.controller.activity.home.buylotteryhall.betinformation;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.SwitchTabsActivityGroup;

import android.view.View;
import android.widget.TabHost.OnTabChangeListener;

/**
 * 投注信息页面：用户显示投注后的投注信息，并设置倍数、追号、合买和赠送页面。用户双色球、大乐透。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-18
 */
public class BetInformationActivityGroup extends SwitchTabsActivityGroup {
	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView
				.setText(R.string.bettinginformation_title_betting);
	}

	@Override
	public void setTitleButton() {

	}

	@Override
	protected void initLotteryInformationBarShow() {
		_fLotteryInformationBar.setVisibility(View.GONE);
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] { BetInformationActivity.class,
				AppendInformationActivity.class,
				UnionInformationActivity.class,
				PresentInformationActivity.class };
	}

	@Override
	protected void set_fSwitchTabTags() {
		_fSwitchTabTagIds = new int[] { R.string.tabhost_textview_betting,
				R.string.tabhost_textview_append,
				R.string.tabhost_textview_union,
				R.string.tabhost_textview_present };
	}

	@Override
	protected void setSwitchTabHostOnTabChangeListener() {
		_fSwitchTabHost
				.setOnTabChangedListener(new BettingInformationOnTabChangeListener());
	}

	class BettingInformationOnTabChangeListener implements OnTabChangeListener {

		@Override
		public void onTabChanged(String tabId) {
			// 根据不同的选项卡改变标题
			if (tabId.equals(getString(R.string.tabhost_textview_betting))) {
				_fTitleBar._fTitleTextView
						.setText(R.string.bettinginformation_title_betting);
			} else if (tabId
					.equals(getString(R.string.tabhost_textview_append))) {
				_fTitleBar._fTitleTextView
						.setText(R.string.bettinginformation_title_append);
			} else if (tabId.equals(getString(R.string.tabhost_textview_union))) {
				_fTitleBar._fTitleTextView
						.setText(R.string.bettinginformation_title_union);
			} else if (tabId
					.equals(getString(R.string.tabhost_textview_present))) {
				_fTitleBar._fTitleTextView
						.setText(R.string.bettinginformation_title_present);
			}
		}
	}
}
