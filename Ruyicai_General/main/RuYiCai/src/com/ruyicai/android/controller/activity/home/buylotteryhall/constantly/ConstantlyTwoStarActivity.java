package com.ruyicai.android.controller.activity.home.buylotteryhall.constantly;

import android.view.View;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.viewpagers.PlayMethodTextViewInterface;
import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.button.PageChangeRadioButtonsInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

public class ConstantlyTwoStarActivity extends SelectNumberActivity implements
		BetBarInterface, PageChangeRadioButtonsInterface,
		PlayMethodTextViewInterface {
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
				R.string.radiogroup_text_selfselect,
				R.string.radiogroup_text_groupselect,
				R.string.radiogroup_text_sum });
	}

	@Override
	public void setPlayMethodTextViewContent() {
		// FIXME 如何将各种玩法的标识改进，不适用文本字符串资源Id
		int checkId = _fPageChangeRadioButtons.getCheckedRadioButtonId();
		if (checkId == R.string.radiogroup_text_selfselect) {
			_fPlayMethodTextView
					.setText(R.string.constantly_textview_twostar_selfselectplaymethod);
		} else if (checkId == R.string.radiogroup_text_groupselect) {
			_fPlayMethodTextView
					.setText(R.string.constantly_textview_twostar_groupselectplaymethod);
		} else {
			_fPlayMethodTextView
					.setText(R.string.constantly_textview_twostar_sumplaymethod);
		}

	}

	@Override
	protected void setSelectNumberPanelNum() {
		int checkId = _fPageChangeRadioButtons.getCheckedRadioButtonId();
		if (checkId == R.string.radiogroup_text_selfselect) {
			_fNumOfSelectNumberPanel = 2;
		} else if (checkId == R.string.radiogroup_text_groupselect) {
			_fNumOfSelectNumberPanel = 1;
		} else {
			_fNumOfSelectNumberPanel = 1;
		}
	}

	@Override
	protected void initSelectNumberPanelsWithPage(int aPage_i) {
		for (int panel_i = 0; panel_i < _fNumOfSelectNumberPanel; panel_i++) {
			// 获取当前初始化显示的选号面板对象
			SelectNumberPanel selectNumberPanel = _fSelectNumberPanelList.get(
					aPage_i).get(panel_i);

			switch (panel_i) {
			case 0:
				int checkedId = _fPageChangeRadioButtons
						.getCheckedRadioButtonId();

				if (checkedId == R.string.radiogroup_text_selfselect) {
					initDecadeSelectNumberPanel(aPage_i, selectNumberPanel);
				} else if (checkedId == R.string.radiogroup_text_groupselect) {
					initBettingNumSelectNumberPanel(aPage_i, selectNumberPanel);
				} else {
					initSumBettingNumSelectNumberPanel(aPage_i,
							selectNumberPanel);
				}
				break;
			case 1:
				initNumberSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	private void initSumBettingNumSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("请选择投注号码：", 1, 16, 0,
					19, SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19 };
			selectNumberPanel.initSelectNumberPanelShow("请选择投注号码：", 1, 16, 0,
					19, SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
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

	private void initBettingNumSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("请选择投注号码：", 1, 16, 0,
					10, SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("请选择投注号码：", 1, 16, 0,
					10, SelectNumberBallType.REDBALL, lossValues, true);
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
