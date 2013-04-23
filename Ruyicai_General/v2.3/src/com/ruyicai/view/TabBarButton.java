/**
 * @Title 顶部RadioButton生成类
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */

package com.ruyicai.view;

import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;

import com.ruyicai.util.Constants;

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

	protected Bitmap getBitmapFromRes(int aResId) {
		Resources r = this.getContext().getResources();
		InputStream is = r.openRawResource(aResId);
		BitmapDrawable bmpDraw = new BitmapDrawable(is);

		return bmpDraw.getBitmap();
	}

	public void setState(int imageId, int onimageId, int buttomButtonSize) {
		Resources resource = this.getResources();

		Drawable offDrawable = resource.getDrawable(imageId);

		// 手动把图片进行拉伸^_^
		Bitmap bitmapButtonCommon = getBitmapFromRes(imageId);
		Bitmap bitmapButtonPressed = getBitmapFromRes(onimageId);
		
//		Drawable commonDrawable = new BitmapDrawable( bitmapButtonCommon);
//		Drawable pressedDrawable = new BitmapDrawable( bitmapButtonPressed);
//		setStateImageDrawables(commonDrawable, pressedDrawable);
		int width = offDrawable.getIntrinsicWidth();// bitmapButtonCommon

		Matrix matrix = new Matrix();
		float sw = 0f;
		float sh = 0f;
		// 计算缩放比例
		if(Constants.SCREEN_DENSITY==0.75f){
			sw = Float.valueOf(Constants.SCREEN_WIDTH)/Float.valueOf(480)*0.8f;
			sh = Float.valueOf(Constants.SCREEN_HEIGHT) / Float.valueOf(800)*0.9f;
		}
		else if (Constants.SCREEN_DENSITY==1.0f) {
			sw = Float.valueOf(Constants.SCREEN_WIDTH)/Float.valueOf(480);
			sh = Float.valueOf(Constants.SCREEN_HEIGHT) / Float.valueOf(800);
		} else if(Constants.SCREEN_DENSITY>1.000000f&&Constants.SCREEN_DENSITY<1.5000f){
			sw = ((float) (Constants.SCREEN_WIDTH / buttomButtonSize) / width) *0.75f;
			sh = 0.75f;
			}else if(Constants.SCREEN_DENSITY== 1.5f) {
		
			sw = ((float) (Constants.SCREEN_WIDTH / buttomButtonSize) / width) * 1.5f;
			sh = 1.5f;
		}
		matrix.postScale(sw, sh);
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmapButtonCommon, 0, 0,
				bitmapButtonCommon.getWidth(), bitmapButtonCommon.getHeight(),
				matrix, true);
		Bitmap resizeBitmapPressed = Bitmap.createBitmap(bitmapButtonPressed,
				0, 0, bitmapButtonPressed.getWidth(), bitmapButtonPressed
						.getHeight(), matrix, true);

		Drawable offD = new BitmapDrawable(resizeBitmap);
		Drawable offDPressed = new BitmapDrawable(resizeBitmapPressed);

		setStateImageDrawables(offD, offDPressed);
		bitmapButtonCommon.recycle();
		bitmapButtonPressed.recycle();
//		
	}

	public void setState(Drawable offDrawable, Drawable onDrawable) {

		setStateImageDrawables(offDrawable, onDrawable);
	}

	private void setStateImageDrawables(Drawable onDrawable,
			Drawable offDrawable) {
		StateListDrawable drawables = new StateListDrawable();

		int stateChecked = android.R.attr.state_checked;
		int stateFocused = android.R.attr.state_focused;
		int statePressed = android.R.attr.state_pressed;
		int stateWindowFocused = android.R.attr.state_window_focused;

		// Resources resource = this.getResources();
		// Drawable xDrawable =
		// resource.getDrawable(R.drawable.bottom_bar_highlight);
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
		// drawables.addState(new int[] {}, xDrawable);

		this.setButtonDrawable(drawables);
	}
}
