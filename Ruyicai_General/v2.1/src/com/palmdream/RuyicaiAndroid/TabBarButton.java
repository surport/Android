/**
 * @Title 顶部RadioButton生成类
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */

package com.palmdream.RuyicaiAndroid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class TabBarButton extends RadioButton {

	Context context;

	public TabBarButton(Context context) {
		super(context);
		this.context = context;
	}

	public TabBarButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void setState(String label) {
		RadioStateDrawable offDrawable = new RadioStateDrawable(context, label,
				false, RadioStateDrawable.SHADE_GRAY);
		RadioStateDrawable onDrawable = new RadioStateDrawable(context, label,
				true, RadioStateDrawable.SHADE_YELLOW);
		setStateImageDrawables(onDrawable, offDrawable);
	}

	public void setStateLabel(String label, int iTextSize, int offState,
			int onState) {
		RadioStateDrawable offDrawable = new RadioStateDrawable(context, label,
				true, offState, iTextSize);
		RadioStateDrawable onDrawable = new RadioStateDrawable(context, label,
				true, onState, iTextSize);
		setStateImageDrawables(onDrawable, offDrawable);
	}

	public void setState(String label, int imageId) {
		RadioStateDrawable offDrawable = new RadioStateDrawable(context,
				imageId, label, false, RadioStateDrawable.SHADE_GRAY);
		RadioStateDrawable onDrawable = new RadioStateDrawable(context,
				imageId, label, true, RadioStateDrawable.SHADE_YELLOW);
		setStateImageDrawables(onDrawable, offDrawable);
	}

	public void setState(String label, int imageId, int offState, int onState) {
		RadioStateDrawable offDrawable = new RadioStateDrawable(context,
				imageId, label, false, offState);
		RadioStateDrawable onDrawable = new RadioStateDrawable(context,
				imageId, label, true, onState);
		setStateImageDrawables(onDrawable, offDrawable);
	}

	public void setState(String label, int onId, int offId) {
		Resources resource = this.getResources();
		Drawable offDrawable = resource.getDrawable(offId);
		Drawable onDrawable = resource.getDrawable(onId);
		setStateImageDrawables(onDrawable, offDrawable);
	}

	public void setState(int imageId, int onimageId) {
		Resources resource = this.getResources();
		Drawable offDrawable = resource.getDrawable(imageId);
		Drawable onDrawable = resource.getDrawable(onimageId);
		setStateImageDrawables(offDrawable, onDrawable);
	}

	private void setStateImageDrawables(Drawable onDrawable,
			Drawable offDrawable) {
		StateListDrawable drawables = new StateListDrawable();

		int stateChecked = android.R.attr.state_checked;
		int stateFocused = android.R.attr.state_focused;
		int statePressed = android.R.attr.state_pressed;
		int stateWindowFocused = android.R.attr.state_window_focused;

		Resources resource = this.getResources();
		Drawable xDrawable = resource
				.getDrawable(R.drawable.bottom_bar_highlight);

		drawables.addState(new int[] { stateChecked, -stateWindowFocused },
				offDrawable);
		drawables.addState(new int[] { -stateChecked, -stateWindowFocused },
				offDrawable);
		drawables
				.addState(new int[] { stateChecked, statePressed }, onDrawable);
		drawables.addState(new int[] { -stateChecked, statePressed },
				onDrawable);
		drawables
				.addState(new int[] { stateChecked, stateFocused }, onDrawable);
		drawables.addState(new int[] { -stateChecked, stateFocused },
				offDrawable);
		drawables.addState(new int[] { stateChecked }, onDrawable);
		drawables.addState(new int[] { -stateChecked }, offDrawable);
		drawables.addState(new int[] {}, xDrawable);
		this.setButtonDrawable(drawables);
	}
}
