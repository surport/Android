package com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.viewpagers.PlayMethodTextViewInterface;

/**
 * 江西11选5任选五选号页面： 1.实现玩法介绍的设置
 * 
 * @author xiang_000
 * @since RYC1.0 2013-5-2
 */
public class JiangXiElevenSelectFiveOptionalFiveActivity extends
		JiangXiElevenSelectFiveOptionalSelectActivity {

	@Override
	public void setPlayMethodTextViewContent() {
		if (_fPageChangeRadioButtons.getCheckedRadioButtonId() == R.string.radiogroup_text_selfselect) {
			_fPlayMethodTextView
					.setText(R.string.jiangxi11xuan5_textview_selfselect_optionalfivepalymethod);
		} else {
			_fPlayMethodTextView
					.setText(R.string.jiangxi11xuan5_textview_courageselect_optionalfivepalymethod);
		}

	}

}
