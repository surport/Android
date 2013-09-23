package com.ruyicai.android.controller.activity.home.buylotteryhall.arrange3;

import android.view.View;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.button.PageChangeRadioButtonsInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

public class ArrangeThreeGroupThreeActivity extends SelectNumberActivity
		implements BetBarInterface, PageChangeRadioButtonsInterface {

	{
//		set_fBettingBarInterface(this);
		set_fPageChangeRadioButtonsInterface(this);
	}

	@Override
	protected boolean isAddPlayMethodTextView() {
		return false;
	}

	@Override
	protected boolean isAddPageChangeRadioButtons() {
		return true;
	}

	@Override
	public void setPageChangeRadioButtonTextResouceIds() {
		_fPageChangeRadioButtons.set_fRadioButtonTextResouceIds(new int[] {
				R.string.radiogroup_text_singleselect,
				R.string.radiogroup_text_mutileselect,
				R.string.radiogroup_text_sum });
	}

	@Override
	protected void setSelectNumberPanelNum() {
		int checkedId = _fPageChangeRadioButtons.getCheckedRadioButtonId();
		switch (checkedId) {
		case R.string.radiogroup_text_singleselect:
			_fNumOfSelectNumberPanel = 2;
			break;
		case R.string.radiogroup_text_mutileselect:
			_fNumOfSelectNumberPanel = 1;
			break;
		case R.string.radiogroup_text_sum:
			_fNumOfSelectNumberPanel = 1;
			break;
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

				if (checkedId == R.string.radiogroup_text_singleselect) {
					initTwiceSelectNumberPanel(aPage_i, selectNumberPanel);
				} else if (checkedId == R.string.radiogroup_text_mutileselect) {
					initSelectCodeNumSelectNumberPanel(aPage_i,
							selectNumberPanel);
				} else {
					initSelectCodeNumSelectNumberPanel(aPage_i,
							selectNumberPanel);
				}
				break;
			case 1:
				initOnceSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	/**
	 * 初始化复式注码选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initSelectCodeNumSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("注码：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("注码：", 1, 16, 0, 10,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	/**
	 * 初始化出现一次的选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initOnceSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("出现一次的号码：", 1, 16, 0,
					10, SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("出现一次的号码：", 1, 16, 0,
					10, SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	/**
	 * 初始化出现两次的选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initTwiceSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("出现两次的号码：", 1, 16, 0,
					10, SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			selectNumberPanel.initSelectNumberPanelShow("出现两次的号码：", 1, 16, 0,
					10, SelectNumberBallType.REDBALL, lossValues, true);
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
