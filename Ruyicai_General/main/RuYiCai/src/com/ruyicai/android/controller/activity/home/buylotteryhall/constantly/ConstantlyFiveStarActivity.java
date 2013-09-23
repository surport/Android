package com.ruyicai.android.controller.activity.home.buylotteryhall.constantly;

import android.view.View;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.viewpagers.PlayMethodTextViewInterface;
import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.button.PageChangeRadioButtonsInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

public class ConstantlyFiveStarActivity extends SelectNumberActivity implements
		BetBarInterface, PlayMethodTextViewInterface,
		PageChangeRadioButtonsInterface {
	{
//		set_fBettingBarInterface(this);
		set_fPageChangeRadioButtonsInterface(this);
		set_fPlayMethodTextViewInterface(this);
	}

	@Override
	protected boolean isAddPlayMethodTextView() {
		return true;
	}

	@Override
	protected boolean isAddPageChangeRadioButtons() {
		return true;
	}

	@Override
	public void setPageChangeRadioButtonTextResouceIds() {
		_fPageChangeRadioButtons.set_fRadioButtonTextResouceIds(new int[] {
				R.string.radiogroup_text_fivestarselfselect,
				R.string.radiogroup_text_fivestarallselect });
	}

	@Override
	public void setPlayMethodTextViewContent() {
		int checkedId = _fPageChangeRadioButtons.getCheckedRadioButtonId();
		if (checkedId == R.string.constantly_textview_fivestar_selfselectplaymethod) {
			_fPlayMethodTextView
					.setText(R.string.constantly_textview_fivestar_selfselectplaymethod);
		} else {
			_fPlayMethodTextView
					.setText(R.string.constantly_textview_fivestar_allselectplaymethod);
		}
	}

	@Override
	protected void setSelectNumberPanelNum() {
		_fNumOfSelectNumberPanel = 5;
	}

	@Override
	protected void initSelectNumberPanelsWithPage(int aPage_i) {
		for (int panel_i = 0; panel_i < _fNumOfSelectNumberPanel; panel_i++) {
			// 获取当前初始化显示的选号面板对象
			SelectNumberPanel selectNumberPanel = _fSelectNumberPanelList.get(
					aPage_i).get(panel_i);

			switch (panel_i) {
			case 0:
				initYriadSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 1:
				initThousandSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 2:
				initHundredSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 3:
				initDecadeSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 4:
				initNumberSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	private void initNumberSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("个位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("个位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	private void initDecadeSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("十位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("十位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	private void initHundredSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("百位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("百位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	private void initThousandSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("千位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("千位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	private void initYriadSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("万位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("万位区：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, lossValues, true);
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
