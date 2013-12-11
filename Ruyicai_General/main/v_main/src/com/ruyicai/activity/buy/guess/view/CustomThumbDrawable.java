package com.ruyicai.activity.buy.guess.view;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.util.PublicMethod;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author yejc
 * 
 */
public class CustomThumbDrawable extends Drawable {
	private Context mContext;
	private String mScore = "";
	private int[] mDrawables = {R.drawable.ruyijc_sleder, R.drawable.ruyijc_sleder2x};
	private boolean mIsShowText = false;

	public CustomThumbDrawable(Context context) {
		mContext = context;
	}

	public String getScore() {
		return mScore;
	}

	public void setScore(String mScore) {
		this.mScore = mScore;
	}

	public boolean isShowText() {
		return mIsShowText;
	}

	public void setIsShowText(boolean mIsShowText) {
		this.mIsShowText = mIsShowText;
		invalidateSelf();
	}

	@Override
	protected final boolean onStateChange(int[] state) {
		invalidateSelf();
		return false;
	}

	@Override
	public final boolean isStateful() {
		return true;
	}

	@Override
	public final void draw(Canvas canvas) {
		int height = this.getBounds().centerY();
		int width = this.getBounds().centerX();
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(PublicMethod.getPxInt(12, mContext));
		if (mIsShowText) {
			BitmapDrawable drawable = (BitmapDrawable)mContext.getResources().getDrawable(mDrawables[1]);
			Bitmap bitmap = drawable.getBitmap();
			int bitWidth = bitmap.getWidth();
			canvas.drawBitmap(bitmap, width - bitWidth/2, 0, paint);
			float textWidth = paint.measureText(mScore)/2;
			canvas.drawText(mScore, width - textWidth, (height)/2, paint);
		} else {
			BitmapDrawable drawable = (BitmapDrawable)mContext.getResources().getDrawable(mDrawables[0]);
			Bitmap bitmap = drawable.getBitmap();
			int bitWidth = bitmap.getWidth();
			canvas.drawBitmap(bitmap, width - bitWidth/2, 0, paint);
		}
	}

	@Override
	public int getIntrinsicHeight() {
		BitmapDrawable drawable = null;
		if (mIsShowText) {
			drawable = (BitmapDrawable) mContext.getResources().getDrawable(
					mDrawables[1]);
		} else {
			drawable = (BitmapDrawable) mContext.getResources().getDrawable(
					mDrawables[0]);
		}
		Bitmap bitmap = drawable.getBitmap();
		return bitmap.getHeight();
	}

	@Override
	public int getIntrinsicWidth() {
		BitmapDrawable drawable = null;
		if (mIsShowText) {
			drawable = (BitmapDrawable) mContext.getResources().getDrawable(
					mDrawables[1]);
		} else {
			drawable = (BitmapDrawable) mContext.getResources().getDrawable(
					mDrawables[0]);
		}
		Bitmap bitmap = drawable.getBitmap();
		return bitmap.getWidth();
	}

	@Override
	public final int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}