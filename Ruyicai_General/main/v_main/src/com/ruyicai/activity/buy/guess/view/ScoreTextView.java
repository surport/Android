package com.ruyicai.activity.buy.guess.view;

import com.ruyicai.util.PublicMethod;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 带箭头的进度条
 * 
 * @author yejc
 * 
 */
public class ScoreTextView extends TextView {
	/**
	 * 画笔对象的引用
	 */
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);;
	
	private String[] mScore = {"200", "500", "1000", "1500", "2000"};
	

	public ScoreTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
//		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		mPaint.setColor(Color.BLACK);
	}
	
	public ScoreTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setTextSize(PublicMethod.getPxInt(12, getContext()));
		mPaint.setColor(Color.BLACK);
		float width = getWidth();// 组建的宽度
//		float height = getHeight();// 组建的宽度
		float constantWidth = width - PublicMethod.getPxInt(40, getContext());
		if (constantWidth > 0) {
			float unit = constantWidth/36;
			float firstStartX = unit*6 + PublicMethod.getPxInt(25, getContext())
					- mPaint.measureText(mScore[1])/2;
			
			float secondStartX = unit*16 + PublicMethod.getPxInt(20, getContext())
					- mPaint.measureText(mScore[2])/2;
			
			float thirdStartX = unit*26 + PublicMethod.getPxInt(20, getContext())
					- mPaint.measureText(mScore[3])/2;
			
			float fourthStartX = unit*36 + PublicMethod.getPxInt(20, getContext())
					- mPaint.measureText(mScore[4])/2;
			canvas.drawText(mScore[0], 5f, 20f, mPaint);
			canvas.drawText(mScore[1], firstStartX, 20f, mPaint);
			canvas.drawText(mScore[2], secondStartX, 20f, mPaint);
			canvas.drawText(mScore[3], thirdStartX, 20f, mPaint);
			canvas.drawText(mScore[4], fourthStartX, 20f, mPaint);
		}
	}
}
