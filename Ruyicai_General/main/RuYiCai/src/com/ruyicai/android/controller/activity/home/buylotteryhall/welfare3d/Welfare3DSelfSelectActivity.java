package com.ruyicai.android.controller.activity.home.buylotteryhall.welfare3d;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.betinformation.BetInformationActivityGroup;
import com.ruyicai.android.controller.activity.viewpagers.SelectNumberActivity;
import com.ruyicai.android.controller.compontent.bar.BetBarInterface;
import com.ruyicai.android.controller.compontent.button.PageChangeRadioButtonsInterface;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;

/**
 * 福彩3D自选选号页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-29
 */
public class Welfare3DSelfSelectActivity extends SelectNumberActivity implements
		PageChangeRadioButtonsInterface {
	{
		// set_fBettingBarInterface(this);
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
				R.string.radiogroup_text_normalselfselect,
				R.string.radiogroup_text_sumselfselect });
	}

	@Override
	protected void setSelectNumberPanelNum() {
		int checkedId = _fPageChangeRadioButtons.getCheckedRadioButtonId();
		switch (checkedId) {
		case R.string.radiogroup_text_normalselfselect:
			_fNumOfSelectNumberPanel = 3;
			break;
		case R.string.radiogroup_text_sumselfselect:
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

				if (checkedId == R.string.radiogroup_text_normalselfselect) {
					initHundredSelectNumberPanel(aPage_i, selectNumberPanel);
				} else {
					initCodeNumSelectNumberPanel(aPage_i, selectNumberPanel);
				}
				break;
			case 1:
				initDecadeSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			case 2:
				initNumberSelectNumberPanel(aPage_i, selectNumberPanel);
				break;
			}
		}
	}

	/**
	 * 初始化注码选号面板
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
	private void initCodeNumSelectNumberPanel(int aPage_i,
			SelectNumberPanel selectNumberPanel) {
		if (aPage_i == 0) {
			selectNumberPanel.initSelectNumberPanelShow("注码：", 1, 16, 0, 28,
					SelectNumberBallType.REDBALL, null, false);
		} else {
			int[] lossValues = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28 };
			selectNumberPanel.initSelectNumberPanelShow("注码：", 1, 16, 0, 28,
					SelectNumberBallType.REDBALL, lossValues, true);
		}
		selectNumberPanel.setRandomButtonVisibiity(View.GONE);
	}

	/**
	 * 初始化个位选号区域
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
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

	/**
	 * 初始化十位选号区域
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
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

	/**
	 * 初始化百位选号区域
	 * 
	 * @param aPage_i
	 *            页面索引
	 * @param selectNumberPanel
	 *            选号面板对象
	 */
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

	// @Override
	// public void setNumberBasketButton() {
	// _fBettingBar._fNumberBasketButton
	// .setOnClickListener(new
	// Welfare3DSelfSelectBettingBarButtonOnClickListenter());
	// }
	//
	// @Override
	// public void setClearSelectedNumberButton() {
	// _fBettingBar._fClearSelectNumberButton
	// .setOnClickListener(new
	// Welfare3DSelfSelectBettingBarButtonOnClickListenter());
	// }
	//
	// @Override
	// public void setAddToNumberBasketButton() {
	// _fBettingBar._fAddToNumberBasketButton
	// .setOnClickListener(new
	// Welfare3DSelfSelectBettingBarButtonOnClickListenter());
	// }
	//
	// @Override
	// public void setBettingButton() {
	// _fBettingBar._fBettingButton
	// .setOnClickListener(new
	// Welfare3DSelfSelectBettingBarButtonOnClickListenter());
	// }

	/**
	 * 选号 页面投注栏按钮点击监听实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-4-18
	 */
	public class Welfare3DSelfSelectBettingBarButtonOnClickListenter implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bettingbar_button_numberbasket:
				Toast.makeText(Welfare3DSelfSelectActivity.this,
						"你点击了福彩3D球自选号码篮按钮", Toast.LENGTH_LONG).show();
				break;
			case R.id.bettingbar_button_clearselectednumber:
				Toast.makeText(Welfare3DSelfSelectActivity.this,
						"你点击了福彩3D自选清除已选择号码按钮", Toast.LENGTH_LONG).show();
				break;
			case R.id.bettingbar_button_addtonumberbasket:
				Toast.makeText(Welfare3DSelfSelectActivity.this,
						"你点击了福彩3D自选加入号码篮按钮", Toast.LENGTH_LONG).show();
				break;
			case R.id.bettingbar_button_betting:
				Intent intent = new Intent(Welfare3DSelfSelectActivity.this,
						BetInformationActivityGroup.class);
				startActivity(intent);
				break;
			}
		}
	}
}
