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
	 * 当前进度
	 */
	private float mProgress = 0;

	/**
	 * 进度的颜色值
	 */
	private int mProgressColor;

	public RectangularProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RectangularProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint = new Paint();
//		this.setWillNotDraw(false);
//		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
//				R.styleable.RectangularProgressBar);
//		// 获取自定义属性和默认值
//		mBackGroundColor = mTypedArray.getColor(
//				R.styleable.RectangularProgressBar_rectangularBackgroundcolor, Color.RED);
//		mProgressColor = mTypedArray.getColor(
//				R.styleable.RectangularProgressBar_rectangularProgressColor, Color.GREEN);
//		
//		mTypedArray.recycle();
	}
	
	public void init(int progressColor,  float progress) {
		mProgress = progress;
		mProgressColor = progressColor;
		mPaint.setColor(mProgressColor);
//		postInvalidate();
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
}
