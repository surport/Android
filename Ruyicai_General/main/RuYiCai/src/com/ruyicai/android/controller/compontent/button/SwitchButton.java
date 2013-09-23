package com.ruyicai.android.controller.compontent.button;

import com.ruyicai.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * 开关按钮
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-20
 */
public class SwitchButton extends ImageButton {
	/** 开关状态 */
	private boolean	_fIsOn	= false;

	{
		setBackgroundResource(R.drawable.switchbutton_off);
		setOnClickListener(new SwitchButtonOnClickListener());
	}

	public SwitchButton(Context context) {
		super(context);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	class SwitchButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (_fIsOn) {
				setBackgroundResource(R.drawable.switchbutton_off);
			} else {
				setBackgroundResource(R.drawable.switchbutton_on);
			}

			_fIsOn = !_fIsOn;
		}
	}
}
