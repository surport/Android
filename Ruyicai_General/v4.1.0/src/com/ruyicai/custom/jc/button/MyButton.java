package com.ruyicai.custom.jc.button;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * 竞彩button按钮的自定义
 * 
 * @author Administrator
 * 
 */
public class MyButton extends ImageView {
	private static final String NAMESPACE = "http://www.ruyicai.com/res/custom";
	private static final String NAME = "http://schemas.android.com/apk/res/android";
	private static final String ATTR_TITLE = "layout_gravity";
	private static final int DEFAULTVALUE_DEGREES = 0;
	private boolean isOnClick = false;// button是否被点击
	private int[] bgId = { R.drawable.jc_btn, R.drawable.jc_btn_b };// 默认背景
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Context context;
	private String textContent = "";
	private int size;
	private float x, y;
	private String codeStr;
	/**add by yejc 20130809 start*/
	private int paintColor = Color.BLACK; 
	private int paintColorArray[] = {Color.BLACK, Color.BLACK};
//	private float width = 0.0f;
	/**add by yejc 20130809 end*/

	public MyButton(Context context) {
		super(context);
		this.context = context;
		initBtn();
	}

	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		initBtn();
		// x = attrs.getAttributeResourceValue(NAME,
		// ATTR_TITLE,DEFAULTVALUE_DEGREES);
	}

	public boolean isOnClick() {
		return isOnClick;
	}

	public void setOnClick(boolean isOnClick) {
		this.isOnClick = isOnClick;
	}

	public void initBg(int[] bgId) {
		this.bgId = bgId;
	}

	/**
	 * 初始化button
	 */
	public void initBtn() {
		isOnClick = false;
		setBackgroundResource(bgId[0]);
		size = PublicMethod.getPxInt(15, context);
		x = PublicMethod.getPxInt(5, context);
		y = PublicMethod.getPxInt(28, context);
	}

	/**
	 * 按钮被点击之后做的事情
	 */
	public void onAction() {
		isOnClick = !isOnClick;
		switchBg();
	}

	/**
	 * 切换背景图片
	 */
	public void switchBg() {
		if (isOnClick) {
			setBackgroundResource(bgId[1]);
			paintColor = paintColorArray[1];
		} else {
			setBackgroundResource(bgId[0]);
			paintColor = paintColorArray[0];
		}
	}

	public void setTextSize(int size) {
		this.size = size;
	}

	public void setTextX(int x) {
		this.x = x;
	}

	public void setTextY(int y) {
		this.y = y;
	}

	public void setCodeStr(String text) {
		codeStr = text;
	}

	public String getCodeStr() {
		return codeStr;
	}

	public void setBtnText(String text) {
		textContent = text;
		postInvalidate();
	}

	public String getBtnText() {
		return textContent;
	}


	public int[] getPaintColorArray() {
		return paintColorArray;
	}

	public void setPaintColorArray(int[] paintColorArray) {
		this.paintColorArray = paintColorArray;
	}
	
	/**add by yejc 20130820 start*/
//	public float getStartX() {
//		float textWidth = mPaint.measureText(textContent);
//		float width = getWidth();
//		return (width - textWidth)/2;
//	}
//	
//	public float getStartY() {
//		Paint.FontMetrics metrics = mPaint.getFontMetrics();
//		float textHeight = (float)Math.ceil(metrics.descent - metrics.ascent);
//		float height = getHeight();
//		return (height - textHeight)/2;
//	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setTypeface(null);
		mPaint.setColor(paintColor);
		mPaint.setTextSize(size);
		float textWidth = mPaint.measureText(textContent);
		float width = getWidth();
		if (textWidth > width) {
			int length = textContent.length()/2;
			String firstLine = textContent.substring(0, length);
			String secondLine = textContent.substring(length, textContent.length());
			float firstStartX = (width - mPaint.measureText(firstLine))/2;
			float secondStartX = (width - mPaint.measureText(secondLine))/2;
			canvas.drawText(firstLine, firstStartX, PublicMethod.getPxInt(15, context), mPaint);
			
			canvas.drawText(secondLine, secondStartX, PublicMethod.getPxInt(32, context), mPaint);
			
		} else {
			float textStartX = (width - textWidth)/2;
			canvas.drawText(textContent, textStartX, y, mPaint);
		}
		

	}
	
}
