package com.ruyicai.android.controller.activity.home.buylotteryhall.welfare3d;

import android.view.View;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.button.PageChangeRadioButtonsInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

/**
 * 福彩3D组六选号页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-29
 */
public class Welfare3DGroupSixActivity extends SelectNumberActivity implements
		BetBarInterface, PageChangeRadioButtonsInterface {

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
				R.string.radiogroup_text_arrangesix,
				R.string.radiogroup_text_arrangesixsum });
	}

	@Override
	protected void setSelectNumberPanelNum() {
		int checkedId = _fPageChangeRadioButtons.getCheckedRadioButtonId();
		switch (checkedId) {
		case R.string.radiogroup_text_arrangesix:
			_fNumOfSelectNumberPanel = 1;
			break;
		case R.string.radiogroup_text_arrangesixsum:
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

				if (checkedId == R.string.radiogroup_text_arrangesix) {
					initCodeNumSelectNumberPanel(aPage_i, selectNumberPanel);
				} else {
					initSumCodeNumSelectNumberPanel(aPage_i, selectNumberPanel);
				}
				break;
			}
		}
	}

	/**
	 * 初始化组六和值注码选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initSumCodeNumSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("注码：", 1, 16, 3, 21,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 };
			selectNumberPanel.initSelectNumberPanelShow("注码：", 1, 16, 3, 21,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	/**
	 * 初始化组六注码选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对像
	 */
	private void initCodeNumSelectNumberPanel(int aPage_i,
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
