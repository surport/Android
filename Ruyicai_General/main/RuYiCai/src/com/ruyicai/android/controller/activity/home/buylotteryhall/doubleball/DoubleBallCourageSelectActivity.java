package com.ruyicai.android.controller.activity.home.buylotteryhall.doubleball;

import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

/**
 * 双色球胆拖页面：
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-21
 */
public class DoubleBallCourageSelectActivity extends SelectNumberActivity {
	
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
		_fNumOfSelectNumberPanel = 3;
	}

	@Override
	protected void initSelectNumberPanelsWithPage(int aPage_i) {
		for (int panel_i = 0; panel_i < _fNumOfSelectNumberPanel; panel_i++) {
			// 获取当前初始化显示的选号面板对象
			SelectNumberPanel selectNumberPanel = _fSelectNumberPanelList.get(
					aPage_i).get(panel_i);

			switch (panel_i) {
			case 0:
				initCourageSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 1:
				initDragSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 2:
				initBlueSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	/**
	 * 初始化篮球选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initBlueSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("篮球区：", 1, 16, 1, 16,
					SelectNumberBallType.BLUEBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16 };
			selectNumberPanel.initSelectNumberPanelShow("篮球区：", 1, 16, 1, 16,
					SelectNumberBallType.BLUEBALL, lossValues, true);
		}
	}

	/**
	 * 初始化拖码区显示
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initDragSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("拖码区：", 6, 10, 1, 33,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33 };
			selectNumberPanel.initSelectNumberPanelShow("拖码区：", 6, 10, 1, 33,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
	}

	/**
	 * 初始化胆码区显示
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initCourageSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("胆码区：", 1, 5, 1, 33,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33 };
			selectNumberPanel.initSelectNumberPanelShow("胆码：", 1, 5, 1, 33,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
	}

//	@Override
//	public void setNumberBasketButton() {
//
//	}
//
//	@Override
//	public void setClearSelectedNumberButton() {
//
//	}
//
//	@Override
//	public void setAddToNumberBasketButton() {
//
//	}
//
//	@Override
//	public void setBettingButton() {
//
//	}
}
