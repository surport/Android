package com.ruyicai.activity.buy.guess.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("DrawAllocation")
public class RoundCornerImageView extends ImageView {

	private int xRadius=12;
	private int yRadius=12;
	private BitmapShader shader;

	public RoundCornerImageView(Context context) {
		super(context);
	}

	public float getxRadius() {
		return xRadius;
	}

	public void setxRadius(int xRadius) {
		this.xRadius = xRadius;
	}

	public float getyRadius() {
		return yRadius;
	}

	public void setyRadius(int yRadius) {
		this.yRadius = yRadius;
	}

	public RoundCornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@SuppressLint("NewApi")
	protected void onDraw(Canvas canvas) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
		shader = new BitmapShader(bitmapDrawable.getBitmap(),Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		// 设置映射否则图片显示不全
		RectF rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
		int width = bitmapDrawable.getBitmap().getWidth();
		int height = bitmapDrawable.getBitmap().getHeight();
		RectF src = new RectF(0.0f, 0.0f, width, height);
		Matrix matrix = new Matrix();
		matrix.setRectToRect(src, rect, Matrix.ScaleToFit.CENTER);
		shader.setLocalMatrix(matrix);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);
		canvas.drawRoundRect(rect, xRadius, yRadius, paint);
	}
}
