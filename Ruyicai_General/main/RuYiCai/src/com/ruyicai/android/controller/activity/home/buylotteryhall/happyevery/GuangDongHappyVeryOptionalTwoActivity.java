package com.ruyicai.android.controller.activity.home.buylotteryhall.happyevery;

import com.ruyicai.android.R;

public class GuangDongHappyVeryOptionalTwoActivity extends
		GuangDongHappyVeryOptionalSelectActivity {

	@Override
	public void setPlayMethodTextViewContent() {
		int checkedId = _fPageChangeRadioButtons.getCheckedRadioButtonId();
		switch (checkedId) {
		case R.string.radiogroup_text_selfselect:
			_fPlayMethodTextView
					.setText(R.string.guangdonghappyvery_textview_optionaltwo_selfselectplaymethod);
			break;
		case R.string.radiogroup_text_courageselect:
			_fPlayMethodTextView
					.setText(R.string.guangdonghappyvery_textview_optionaltwo_courageselectplaymethod);
			break;
		}
	}

}
