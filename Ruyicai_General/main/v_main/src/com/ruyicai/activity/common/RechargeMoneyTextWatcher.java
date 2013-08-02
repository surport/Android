package com.ruyicai.activity.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class RechargeMoneyTextWatcher implements TextWatcher {

	EditText mEdit;
	public RechargeMoneyTextWatcher(EditText edit) {
		this.mEdit = edit;
	}

	@Override
	public void afterTextChanged(Editable s) {
		String str = s.toString().trim();
		if (str.length() == 1 && str.startsWith("0")) {
			mEdit.setText("");
		} else if (str.length() > 1 && str.startsWith("0")) {
			mEdit.setText(str.subSequence(1, str.length()));
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

}
