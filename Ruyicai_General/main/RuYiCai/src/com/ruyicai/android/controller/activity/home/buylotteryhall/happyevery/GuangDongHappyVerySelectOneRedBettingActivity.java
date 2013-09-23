package com.ruyicai.android.controller.activity.home.buylotteryhall.happyevery;

import android.view.View;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.viewpagers.PlayMethodTextViewInterface;
import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

public class GuangDongHappyVerySelectOneRedBettingActivity extends
		SelectNumberActivity implements BetBarInterface,
		PlayMethodTextViewInterface {
	{
//		set_fBettingBarInterface(this);
		set_fPlayMethodTextViewInterface(this);
	}

	@Override
	protected boolean isAddPlayMethodTextView() {
		return true;
	}

	@Override
	protected boolean isAddPageChangeRadioButtons() {
		return false;
	}

	@Override
	public void setPlayMethodTextViewContent() {
		_fPlayMethodTextView
				.setText(R.string.guangdonghappyvery_textview_selectoneredbettingplaymethod);
	}

	@Override
	protected void setSelectNumberPanelNum() {
		_fNumOfSelectNumberPanel = 1;
	}

	@Override
	protected void initSelectNumberPanelsWithPage(int aPage_i) {
		for (int panel_i = 0; panel_i < _fNumOfSelectNumberPanel; panel_i++) {
			// 获取当前初始化显示的选号面板对象
			SelectNumberPanel selectNumberPanel = _fSelectNumberPanelList.get(
					aPage_i).get(panel_i);

			switch (panel_i) {
			case 0:
				initBettingNumSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	private void initBettingNumSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("请选择投注号码：", 1, 16, 19,
					2, SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2 };
			selectNumberPanel.initSelectNumberPanelShow("请选择投注号码：", 1, 16, 19,
					2, SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	@Override
	public void setNumberBasketButton() {

	}

	@Override
	public void setClearSelectedNumberButton() {

	}

	@Override
	public void setAddToNumberBasketButton() {

	}

	@Override
	public void setBettingButton() {

	}
}
