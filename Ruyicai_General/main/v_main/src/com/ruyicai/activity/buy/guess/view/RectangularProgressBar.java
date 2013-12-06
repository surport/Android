package com.ruyicai.activity.buy.guess.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 带箭头的进度条
 * 
 * @author yejc
 * 
 */
public class RectangularProgressBar extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint mPaint = null;

	/**
	 * 最大进度
	 */
	private float mMax = 1;

	/**
	 * 当前进度
	 */
	private float mProgress = 0;

	public RectangularProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RectangularProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void init(int color, float progress) {
		mProgress = progress;
		mPaint = new Paint();
		mPaint.setColor(color);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float width = getWidth();// 组建的宽度
		float height = getHeight();// 组建的宽度
		float pWidth = width*mProgress;
		float pHeight = height/2;
		canvas.drawRect(0, 0, pWidth, height, mPaint);
		Path path = new Path();
		path.moveTo(pWidth, 0);// 此点为多边形的起点
		path.lineTo(pWidth + pHeight, pHeight);
		path.lineTo(pWidth, height);
		path.close(); // 使这些点构成封闭的多边形
		canvas.drawPath(path, mPaint);
	}


	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized float getProgress() {
		return mProgress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(float progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > mMax) {
			progress = mMax;
		}
		if (progress <= mMax) {
			this.mProgress = progress;
			postInvalidate();
		}
	}

}
