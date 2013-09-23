package com.ruyicai.android.controller.activity.home.buylotteryhall.elevenluckgold;

import com.ruyicai.android.R;

public class ElvenLuckGoldOptionalThreeActivity extends
		ElevenLuckGoldOptionalSelectActivity {

	@Override
	public void setPlayMethodTextViewContent() {
		if (_fPageChangeRadioButtons.getCheckedRadioButtonId() == R.string.radiogroup_text_selfselect) {
			_fPlayMethodTextView
					.setText(R.string.elevenluckgold_textview_optionalthree_selfselectplaymethod);
		} else {
			_fPlayMethodTextView
					.setText(R.string.elevenluckgold_textview_optionalthree_courageselectplaymethod);
		}
	}

}
