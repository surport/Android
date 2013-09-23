package com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs;

import android.view.View;
import android.view.View.OnClickListener;

import com.ruyicai.android.controller.compontent.dropdownmenu.TitleDropDownMenu;

/**
 * 彩种投注选项卡切换页面：主要处理在选项卡切换基类的基础上，其它的事物，如标题按钮的下拉菜单的生成
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-21
 */
public abstract class LotterySwitchTabsActivityGroup extends
		SwitchTabsActivityGroup {

	@Override
	// FIXME 该函数的名称和逻辑有待改进，可以只返回布尔值。
	protected void initLotteryInformationBarShow() {
		_fLotteryInformationBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void setTitleButton() {
		_fTitleBar._fSpreadButton.setVisibility(View.VISIBLE);
		_fTitleBar._fSpreadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_fTitleBar._fDropDownMenu == null) {
					_fTitleBar._fDropDownMenu = new TitleDropDownMenu(
							LotterySwitchTabsActivityGroup.this);
					_fTitleBar._fDropDownMenu.ShowAsDropDownMenu(v);
				} else {
					_fTitleBar._fDropDownMenu.dismissDropDownMenu();
					_fTitleBar._fDropDownMenu = null;
				}
			}
		});
	}
}