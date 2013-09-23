package com.ruyicai.android.controller.activity.home.buylotteryhall.superlotto;

import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

/**
 * 大乐透胆拖页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-21
 */
public class SuperLottoCourageSelectActivity extends SelectNumberActivity
		implements BetBarInterface {

	{
//		set_fBettingBarInterface(this);
	}

	@Override
	protected boolean isAddPlayMethodTextView() {
		return false;
	}

	@Override
	protected boolean isAddPageChangeRadioButtons() {
		return false;
	}

	@Override
	protected void setSelectNumberPanelNum() {
		_fNumOfSelectNumberPanel = 4;
	}

	@Override
	protected void initSelectNumberPanelsWithPage(int aPage_i) {
		for (int panel_i = 0; panel_i < _fNumOfSelectNumberPanel; panel_i++) {
			// 获取当前初始化显示的选号面板对象
			SelectNumberPanel selectNumberPanel = _fSelectNumberPanelList.get(
					aPage_i).get(panel_i);

			switch (panel_i) {
			case 0:
				initFrontAreaCourageSelectNumberPanel(aPage_i,
						selectNumberPanel);
				break;
			case 1:
				initFrontAreaDragSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 2:
				initAfterAreaCourageSelectNumberPanel(aPage_i,
						selectNumberPanel);
				break;
			case 3:
				initAfterAreaDragSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	/**
	 * 初始化后区胆码选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initAfterAreaDragSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("后区拖码：", 2, 12, 1, 12,
					SelectNumberBallType.BLUEBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12 };
			selectNumberPanel.initSelectNumberPanelShow("后区拖码：", 2, 12, 1, 12,
					SelectNumberBallType.BLUEBALL, lossValues, true);
		}
	}

	/**
	 * 初始化后区胆码选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initAfterAreaCourageSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("后区胆码：", 1, 1, 1, 12,
					SelectNumberBallType.BLUEBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12 };
			selectNumberPanel.initSelectNumberPanelShow("后区胆码：", 1, 1, 1, 12,
					SelectNumberBallType.BLUEBALL, lossValues, true);
		}
	}

	/**
	 * 初始化前区托码选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initFrontAreaDragSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("前区拖码：", 2, 22, 1, 35,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33, 34, 35 };
			selectNumberPanel.initSelectNumberPanelShow("前区拖码：", 2, 22, 1, 35,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
	}

	/**
	 * 初始化前区胆拖选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initFrontAreaCourageSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("前区胆码：", 1, 4, 1, 35,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33, 34, 35 };
			selectNumberPanel.initSelectNumberPanelShow("前区胆码：", 1, 4, 1, 35,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
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
