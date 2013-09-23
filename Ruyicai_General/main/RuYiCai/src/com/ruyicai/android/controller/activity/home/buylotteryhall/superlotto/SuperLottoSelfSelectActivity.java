package com.ruyicai.android.controller.activity.home.buylotteryhall.superlotto;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.betinformation.BetInformationActivityGroup;
import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

/**
 * 大乐透自选页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-21
 */
public class SuperLottoSelfSelectActivity extends SelectNumberActivity{

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
		_fNumOfSelectNumberPanel = 2;
	}

	@Override
	protected void initSelectNumberPanelsWithPage(int aPage_i) {
		for (int panel_i = 0; panel_i < _fNumOfSelectNumberPanel; panel_i++) {
			// 获取当前初始化显示的选号面板对象
			SelectNumberPanel selectNumberPanel = _fSelectNumberPanelList.get(
					aPage_i).get(panel_i);

			switch (panel_i) {
			case 0:
				initFrontAreaSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 1:
				initAfterAreaSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	/**
	 * 初始化后区选号面板显示
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initAfterAreaSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("后区：", 2, 10, 1, 12,
					SelectNumberBallType.BLUEBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12 };
			selectNumberPanel.initSelectNumberPanelShow("后区：", 2, 10, 1, 12,
					SelectNumberBallType.BLUEBALL, lossValues, true);
		}
	}

	/**
	 * 初始化前区选号面板显示
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initFrontAreaSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("前区：", 5, 13, 1, 35,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 32, 33, 34, 35 };
			selectNumberPanel.initSelectNumberPanelShow("前区：", 5, 13, 1, 35,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
	}

//	@Override
//	public void setNumberBasketButton() {
//		_fBettingBar._fNumberBasketButton
//				.setOnClickListener(new SuperLottoSelfSelectBettingBarButtonOnClickListenter());
//	}
//
//	@Override
//	public void setClearSelectedNumberButton() {
//		_fBettingBar._fClearSelectNumberButton
//				.setOnClickListener(new SuperLottoSelfSelectBettingBarButtonOnClickListenter());
//	}
//
//	@Override
//	public void setAddToNumberBasketButton() {
//		_fBettingBar._fAddToNumberBasketButton
//				.setOnClickListener(new SuperLottoSelfSelectBettingBarButtonOnClickListenter());
//	}
//
//	@Override
//	public void setBettingButton() {
//		_fBettingBar._fBettingButton
//				.setOnClickListener(new SuperLottoSelfSelectBettingBarButtonOnClickListenter());
//	}

	/**
	 * 选号 页面投注栏按钮点击监听实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-18
	 */
	public class SuperLottoSelfSelectBettingBarButtonOnClickListenter implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bettingbar_button_numberbasket:
				Toast.makeText(SuperLottoSelfSelectActivity.this,
						"你点击了大乐透球自选号码篮按钮", Toast.LENGTH_LONG).show();
				break;
			case R.id.bettingbar_button_clearselectednumber:
				Toast.makeText(SuperLottoSelfSelectActivity.this,
						"你点击了大乐透自选清除已选择号码按钮", Toast.LENGTH_LONG).show();
				break;
			case R.id.bettingbar_button_addtonumberbasket:
				Toast.makeText(SuperLottoSelfSelectActivity.this,
						"你点击了大乐透自选加入号码篮按钮", Toast.LENGTH_LONG).show();
				break;
			case R.id.bettingbar_button_betting:
				Intent intent = new Intent(SuperLottoSelfSelectActivity.this,
						BetInformationActivityGroup.class);
				startActivity(intent);
				break;
			}
		}

	}
}
